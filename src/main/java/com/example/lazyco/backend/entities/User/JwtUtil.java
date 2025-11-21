package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.Cache.CacheSingleton;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final UserService userService;
  private final UserRoleService userRoleService;
  private final AbstractAction abstractAction;

  public JwtUtil(
      UserService userService, UserRoleService userRoleService, AbstractAction abstractAction) {
    this.userService = userService;
    this.userRoleService = userRoleService;
    this.abstractAction = abstractAction;
  }

  @Value("${cookie.safe:false}")
  private boolean cookieSafe;

  /** Secret key for signing JWTs. */
  private final SecretKey SECRET_KEY = JwtSecretKeyProvider.loadOrCreateSecretKey();

  /** Expiration time for JWT tokens (in milliseconds). */
  private final long TOKEN_EXPIRATION_TIME = Duration.ofHours(12).toMillis();

  /** Name of the JWT cookie. */
  private final String JWT_COOKIE_NAME = "jwt_token";

  /**
   * Generates a JWT token with the given subject and empty claims.
   *
   * @param subject The subject of the token.
   * @return The generated JWT token.
   */
  public String generateToken(String subject) {
    return generateToken(subject, new HashMap<>());
  }

  /**
   * Adds additional claims to an existing token and regenerates it.
   *
   * @param claims The additional claims to add.
   * @return The regenerated token with additional claims.
   */
  public String addClaimsAndRegenerateToken(
      Map<String, Object> claims, HttpServletRequest request) {
    Claims existingClaims = getClaimsFromRequest(request);
    if (Objects.isNull(existingClaims)) return null;
    Map<String, Object> mutableClaims = new HashMap<>(claims);
    existingClaims.forEach(mutableClaims::putIfAbsent);
    return generateToken(existingClaims.getSubject(), mutableClaims);
  }

  /**
   * Generates a JWT token with the given subject and claims.
   *
   * @param subject The subject of the token.
   * @param claims Additional claims to include in the token.
   * @return The generated JWT token.
   */
  public String generateToken(String subject, Map<String, Object> claims) {
    return Jwts.builder()
        .header()
        .type("JWT")
        .and()
        .subject(subject)
        .claims(claims)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
        .signWith(SECRET_KEY, Jwts.SIG.HS256)
        .compact();
  }

  /**
   * Checks if the request is invalid based on the provided criteria.
   *
   * @param request The HTTP servlet request.
   * @param criteria The criteria for validating the request.
   * @return True if the request is invalid, otherwise false.
   */
  public boolean requestIsInvalid(HttpServletRequest request, String criteria) {
    return requestIsInvalid(request, criteria, null);
  }

  public boolean requestIsInvalid(HttpServletRequest request, String criteria, String token) {
    return switch (criteria) {
      case CommonConstants.LOGGED_USER -> {
        AppUserDTO user = checkAndGetUser(request, token);
        if (Objects.nonNull(user)) {
          abstractAction.setLoggedAppUser(user);
          yield false;
        }
        yield true;
      }
      case CommonConstants.LOGGED_USER_ROLE -> {
        UserRoleDTO role = checkAndGetUserRole(request, token);
        if (Objects.nonNull(role)) {
          abstractAction.setLoggedUserRole(role);
          yield false;
        }
        yield true;
      }
      default -> {
        Claims claims = getClaimsFromRequest(request, token);
        yield Objects.isNull(claims);
      }
    };
  }

  /**
   * Checks and retrieves the user from the JWT token in the request.
   *
   * @param request The HTTP servlet request.
   * @return The user extracted from the JWT token.
   */
  public AppUserDTO checkAndGetUser(HttpServletRequest request, String token) {
    Claims claims = getClaimsFromRequest(request, token);
    if (Objects.isNull(claims)) return null;
    Long userId = Long.valueOf(claims.get(CommonConstants.LOGGED_USER).toString());
    return CacheSingleton.getAppUserCache()
        .get(
            CommonConstants.LOGGED_USER.concat(":" + userId),
            () -> userService.getUserById(userId));
  }

  /**
   * Checks and retrieves the user role from the JWT token in the request.
   *
   * @param request The HTTP servlet request.
   * @return The user role extracted from the JWT token.
   */
  public UserRoleDTO checkAndGetUserRole(HttpServletRequest request, String token) {
    Claims claims = getClaimsFromRequest(request, token);
    if (Objects.isNull(claims) || !claims.containsKey(CommonConstants.LOGGED_USER_ROLE))
      return null;
    Long roleId = Long.valueOf(claims.get(CommonConstants.LOGGED_USER_ROLE).toString());
    return CacheSingleton.getUserRoleCache()
        .get(
            CommonConstants.LOGGED_USER_ROLE.concat(":" + roleId),
            () -> userRoleService.getById(roleId));
  }

  /**
   * Retrieves the claims from the JWT token in the request.
   *
   * @param request The HTTP servlet request.
   * @return The claims extracted from the JWT token.
   */
  private Claims getClaimsFromRequest(HttpServletRequest request) {
    return getClaimsFromRequest(request, null);
  }

  /**
   * Retrieves the claims from the JWT token in the HTTP request.
   *
   * @param request The HTTP servlet request.
   * @return The claims extracted from the JWT token, or null if the token is invalid or expired.
   */
  private Claims getClaimsFromRequest(HttpServletRequest request, String token) {
    if (token == null) {
      token = extractTokenFromRequest(request);
    }

    try {
      if (isTokenExpired(token)) return null;
      return extractAllClaims(token);
    } catch (JwtException e) {
      return null;
    }
  }

  /**
   * Extracts the JWT token from the HTTP request.
   *
   * @param request The HTTP servlet request.
   * @return The extracted JWT token, or null if not found.
   */
  public String extractTokenFromRequest(HttpServletRequest request) {
    String token = null;

    // Check Authorization header first
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);
    }

    // Check cookies if not in header
    if (token == null) {
      token = extractJWTTokenFromCookie(request);
    }

    // Return token if valid, otherwise return null (don't throw exception)
    if (token != null && validateJwtToken(token)) {
      return token;
    }

    return null; // Changed from throwing exception
  }

  /**
   * Adds the JWT token to the HTTP response as a cookie.
   *
   * @param response The HTTP servlet response.
   * @param token The JWT token to add.
   */
  public void addToCookie(HttpServletResponse response, String token) {
    Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);

    boolean isSafe = cookieSafe;

    // Always set HttpOnly to prevent XSS attacks
    cookie.setHttpOnly(true);

    // Set Secure flag based on configuration (true for production)
    cookie.setSecure(isSafe);

    // Set SameSite attribute via response header (since Cookie class doesn't support it directly)
    if (isSafe) {
      // For production: SameSite=None requires Secure=true
      response.addHeader(
          "Set-Cookie",
          String.format(
              "%s=%s; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=%d",
              JWT_COOKIE_NAME, token, TOKEN_EXPIRATION_TIME / 1000));
    } else {
      // For development: SameSite=Lax
      response.addHeader(
          "Set-Cookie",
          String.format(
              "%s=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
              JWT_COOKIE_NAME, token, TOKEN_EXPIRATION_TIME / 1000));
    }
  }

  public void clearCookie(HttpServletResponse response) {
    boolean isSafe = cookieSafe;

    if (isSafe) {
      // Production: must match exact cookie attributes
      response.addHeader(
          "Set-Cookie",
          String.format(
              "%s=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0", JWT_COOKIE_NAME));
    } else {
      // Development
      response.addHeader(
          "Set-Cookie",
          String.format("%s=; Path=/; HttpOnly; SameSite=Lax; Max-Age=0", JWT_COOKIE_NAME));
    }
  }

  /**
   * Extracts the JWT token from the cookie in the HTTP request.
   *
   * @param request The HTTP servlet request.
   * @return The extracted JWT token, or null if not found.
   */
  public String extractJWTTokenFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (JWT_COOKIE_NAME.equals(cookie.getName())) {
          try {
            return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
          } catch (Exception e) {
            ApplicationLogger.error(
                "Error while extracting JWT token from cookie: " + e.getMessage());
            return null;
          }
        }
      }
    }
    return null;
  }

  /**
   * Checks if the JWT token is expired.
   *
   * @param token The JWT token to check.
   * @return True if the token is expired, otherwise false.
   */
  private boolean isTokenExpired(String token) throws JwtException {
    return extractExpiration(token).before(DateTimeZoneUtils.getCurrentDate());
  }

  /**
   * Extracts the expiration date from the JWT token.
   *
   * @param token The JWT token to extract the expiration date from.
   * @return The expiration date of the token.
   */
  public Date extractExpiration(String token) throws JwtException {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts a specific claim from the JWT token.
   *
   * @param token The JWT token to extract the claim from.
   * @param claimsResolver A function to resolve the claim.
   * @return The extracted claim.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String extractUsername(String token) throws JwtException {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractUsername(HttpServletRequest request) throws JwtException {
    String token = extractTokenFromRequest(request);
    if (token == null) {
      return null;
    }
    return extractUsername(token);
  }

  private boolean isTokenExpired(HttpServletRequest request) throws JwtException {
    String token = extractTokenFromRequest(request);
    return isTokenExpired(token);
  }

  public Boolean validateToken(String token) throws JwtException {
    return validateJwtToken(token);
  }

  // Get username from JWT token
  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .verifyWith(SECRET_KEY)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public Boolean validateToken(HttpServletRequest request, String username) throws JwtException {
    final String tokenUsername = extractUsername(request);
    return (username.equals(tokenUsername)) && !isTokenExpired(request);
  }

  // validate token
  public Boolean validateToken(String token, UserDTO user) {
    final String username = extractUsername(token);
    return (username.equals(user.getUsername()) && !isTokenExpired(token));
  }

  // Validate JWT token
  public boolean validateJwtToken(String token) {
    try {
      Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
      return true;
    } catch (SecurityException e) {
      ApplicationLogger.warn("Invalid JWT signature: " + e.getMessage());
    } catch (MalformedJwtException e) {
      ApplicationLogger.warn("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      ApplicationLogger.warn("JWT token is expired: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      ApplicationLogger.warn("JWT token is unsupported: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      ApplicationLogger.warn("JWT claims string is empty: " + e.getMessage());
    } catch (io.jsonwebtoken.security.SignatureException e) {
      ApplicationLogger.warn("Session expired or token is invalid. Please login again.");
    } catch (Exception e) {
      ApplicationLogger.warn(e.getMessage());
    }
    return false;
  }

  /**
   * Extracts all claims from the JWT token.
   *
   * @param token The JWT token to extract claims from.
   * @return The extracted claims.
   */
  private Claims extractAllClaims(String token) throws JwtException {
    // Parse the JWT token and extract the claims
    try {
      Jws<Claims> jws = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
      return jws.getPayload();
    } catch (SecurityException e) {
      ApplicationLogger.warn("Invalid JWT signature: " + e.getMessage());
    } catch (MalformedJwtException e) {
      ApplicationLogger.warn("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      ApplicationLogger.warn("JWT token is expired: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      ApplicationLogger.warn("JWT token is unsupported: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      ApplicationLogger.warn("JWT claims string is empty: " + e.getMessage());
    } catch (io.jsonwebtoken.security.SignatureException e) {
      ApplicationLogger.warn("Session expired or token is invalid. Please login again.");
    } catch (Exception e) {
      ApplicationLogger.warn("JWT token is invalid: " + e.getMessage());
    }
    return null;
  }
}
