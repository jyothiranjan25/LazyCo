package com.example.lazyco.backend.entities.User;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.crypto.SecretKey;

public class JwtUtil {

  /** Secret key for signing JWTs. */
  private static final SecretKey SECRET_KEY = JwtSecretKeyProvider.loadOrCreateSecretKey();

  /** Expiration time for JWT tokens (in milliseconds). */
  private static final long TOKEN_EXPIRATION_TIME = 86400000;

  /** Name of the JWT cookie. */
  private static final String JWT_COOKIE_NAME = "jwt_token";

  /** ThreadLocal to hold session data. */
  private static final ThreadLocal<Map<String, Object>> SESSION_DATA =
      ThreadLocal.withInitial(HashMap::new);

  /** Map to hold invalidated tokens. */
  private static final ConcurrentHashMap<String, Date> invalidatedTokens =
      new ConcurrentHashMap<>();

  /**
   * Generates a JWT token with the given subject and empty claims.
   *
   * @param subject The subject of the token.
   * @return The generated JWT token.
   */
  public static String generateToken(String subject) {
    return generateToken(subject, new HashMap<>());
  }

  /**
   * Generates a JWT token with the given subject and claims.
   *
   * @param subject The subject of the token.
   * @param claims Additional claims to include in the token.
   * @return The generated JWT token.
   */
  public static String generateToken(String subject, Map<String, Object> claims) {
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
  public static boolean requestIsInvalid(HttpServletRequest request, String criteria) {
    return requestIsInvalid(request, criteria, null);
  }

  public static boolean requestIsInvalid(
      HttpServletRequest request, String criteria, String token) {
    return switch (criteria) {
      case CommonConstrains.LOGGED_USER -> {
        UserDTO user = checkAndGetUser(request, token);
        if (Objects.nonNull(user)) {
          SESSION_DATA.get().put(criteria, user);
          yield false;
        }
        yield true;
      }
      case CommonConstrains.LOGGED_USER_ROLE -> {
        UserRoleDTO role = checkAndGetUserRole(request, token);
        if (Objects.nonNull(role)) {
          SESSION_DATA.get().put(criteria, role);
          yield false;
        }
        yield true;
      }
      default -> Objects.isNull(getClaimsFromRequest(request));
    };
  }

  /**
   * Checks and retrieves the user from the JWT token in the request.
   *
   * @param request The HTTP servlet request.
   * @return The user extracted from the JWT token.
   */
  public static UserDTO checkAndGetUser(HttpServletRequest request, String token) {
    Claims claims = getClaimsFromRequest(request, token);
    if (Objects.isNull(claims)) return null;
    String userId = claims.getSubject();
    return getBean(UserService.class).getUser(userId);
  }

  /**
   * Checks and retrieves the user role from the JWT token in the request.
   *
   * @param request The HTTP servlet request.
   * @return The user role extracted from the JWT token.
   */
  public static UserRoleDTO checkAndGetUserRole(HttpServletRequest request, String token) {
    Claims claims = getClaimsFromRequest(request, token);
    return checkAndGetUserRole(claims);
  }

  /**
   * Retrieves the claims from the JWT token in the request.
   *
   * @param request The HTTP servlet request.
   * @return The claims extracted from the JWT token.
   */
  private static Claims getClaimsFromRequest(HttpServletRequest request) {
    return getClaimsFromRequest(request, null);
  }

  /**
   * Retrieves the claims from the JWT token in the HTTP request.
   *
   * @param request The HTTP servlet request.
   * @return The claims extracted from the JWT token, or null if the token is invalid or expired.
   */
  private static Claims getClaimsFromRequest(HttpServletRequest request, String token) {
    if (token == null) {
      token = extractTokenFromRequest(request);
    }

    try {
      if (isTokenExpired(token) || isTokenInvalidated(token)) return null;
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
  public static String extractTokenFromRequest(HttpServletRequest request) {

    String token = null;

    // Retrieve the Authorization header from the request
    String authorizationHeader = request.getHeader("Authorization");

    // Check if the Authorization header is present and starts with "Bearer "
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      // If present, extract the token part after "Bearer " and return
      token = authorizationHeader.substring(7);
    }

    // If the token is not found in the Authorization header, check for it in cookies
    if (token == null) {
      token = extractJWTTokenFromCookie(request);
    }

    if (token != null && validateJwtToken(token)) {
      return token;
    } else {
      throw new ExceptionWrapper("Authorization token is missing or invalid");
    }
  }

  /**
   * Extracts the JWT token from the cookie in the HTTP request.
   *
   * @param request The HTTP servlet request.
   * @return The extracted JWT token, or null if not found.
   */
  public static String extractJWTTokenFromCookie(HttpServletRequest request) {
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
  private static boolean isTokenExpired(String token) throws JwtException {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the expiration date from the JWT token.
   *
   * @param token The JWT token to extract the expiration date from.
   * @return The expiration date of the token.
   */
  public static Date extractExpiration(String token) throws JwtException {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts a specific claim from the JWT token.
   *
   * @param token The JWT token to extract the claim from.
   * @param claimsResolver A function to resolve the claim.
   * @return The extracted claim.
   */
  public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
      throws JwtException {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Adds the JWT token to the HTTP response as a cookie.
   *
   * @param response The HTTP servlet response.
   * @param token The JWT token to add.
   */
  public static void addToCookie(HttpServletResponse response, String token) {
    // Create a new cookie with the JWT token and specified name
    Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);

    // Retrieve the service responsible for managing configurations
    boolean isSafe = fetchSafetyFromDatabase();

    // Set the HTTP-only flag to prevent JavaScript access to the cookie
    cookie.setHttpOnly(isSafe);

    // Set the secure flag to true if cookies should only be sent over HTTPS
    cookie.setSecure(isSafe);

    // Set the cookie path to "/" to make it accessible across the entire application
    cookie.setPath("/");

    // Add the cookie to the HTTP response
    response.addCookie(cookie);
  }

  private static boolean fetchSafetyFromDatabase() {
    // Determine if cookies should be set as HTTP-only and secure based on configuration
    boolean isSafe = false; // Default value for safety
    /*
     *  need to work on this part
     *  if the value is true then it fails the login
     *  don't set true until you are using the separate app for frontend
     */
    String configValue = AbstractAction.getConfigProperties("cookie.safe");
    if (Objects.nonNull(configValue)) {
      // If configuration value exists, parse it to boolean
      isSafe = Boolean.parseBoolean(configValue);
    }
    return isSafe;
  }

  /**
   * Retrieves the logged-in user from the session data.
   *
   * @return The logged-in user.
   */
  public static UserDTO getLoggedInUser() {
    return (UserDTO) SESSION_DATA.get().get(CommonConstrains.LOGGED_USER);
  }

  public static void setLoggedInUser(UserDTO user) {
    SESSION_DATA.get().put(CommonConstrains.LOGGED_USER, user);
  }

  /**
   * Retrieves the logged-in user role from the session data.
   *
   * @return The logged-in user role.
   */
  public static UserRoleDTO getLoggedInUserRole() {
    return (UserRoleDTO) SESSION_DATA.get().get(CommonConstrains.LOGGED_USER_ROLE);
  }

  public static void setLoggedInUserRole(UserRoleDTO userRole) {
    SESSION_DATA.get().put(CommonConstrains.LOGGED_USER_ROLE, userRole);
  }

  public static Map<String, Object> getSessionData() {
    return SESSION_DATA.get();
  }

  public static void setSessionData(Map<String, Object> sessionData) {
    SESSION_DATA.set(sessionData);
  }

  /** Clears the session data. */
  public static void clearSessionData() {
    SESSION_DATA.get().clear();
    SESSION_DATA.remove();
  }

  private static UserRoleDTO checkAndGetUserRole(Claims claims) {
    if (Objects.isNull(claims) || !claims.containsKey(CommonConstrains.LOGGED_USER_ROLE))
      return null;
    UserRoleDTO filter = new UserRoleDTO();
    filter.setId(Long.valueOf(claims.get(CommonConstrains.LOGGED_USER_ROLE).toString()));
    List<UserRoleDTO> roles = getBean(UserRoleService.class).get(filter);
    if (roles.isEmpty()) {
      return null;
    } else {
      return roles.get(0);
    }
  }

  public static String extractUsername(String token) throws JwtException {
    return extractClaim(token, Claims::getSubject);
  }

  public static String extractUsername(HttpServletRequest request) throws JwtException {
    String token = extractTokenFromRequest(request);
    return extractUsername(token);
  }

  private static boolean isTokenExpired(HttpServletRequest request) throws JwtException {
    String token = extractTokenFromRequest(request);
    return isTokenExpired(token);
  }

  public static Boolean validateToken(HttpServletRequest request, String username)
      throws JwtException {
    final String tokenUsername = extractUsername(request);
    return (username.equals(tokenUsername)) && !isTokenExpired(request);
  }

  // validate token
  public Boolean validateToken(String token, UserDTO user) {
    final String username = extractUsername(token);
    return (username.equals(user.getUsername()) && !isTokenExpired(token));
  }

  // Cookie methods
  public static void clearCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie(JWT_COOKIE_NAME, null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
  }

  // Blacklist methods
  /**
   * Invalidates the JWT token by adding it to the blacklist.
   *
   * @param token The JWT token to invalidate.
   */
  public static void invalidateToken(String token) {
    Date expiration = extractExpiration(token);
    addTokenToBlacklist(token, expiration);
  }

  /**
   * Adds the JWT token to the blacklist with the specified expiration date.
   *
   * @param token The JWT token to add to the blacklist.
   * @param expiration The expiration date of the token.
   */
  public static void addTokenToBlacklist(String token, Date expiration) {
    invalidatedTokens.put(token, expiration);
  }

  /**
   * Checks if the JWT token is invalidated (blacklisted).
   *
   * @param token The JWT token to check.
   * @return True if the token is invalidated, otherwise false.
   */
  public static boolean isTokenInvalidated(String token) {
    return isTokenBlacklisted(token);
  }

  /**
   * Checks if the JWT token is blacklisted.
   *
   * @param token The JWT token to check.
   * @return True if the token is blacklisted, otherwise false.
   */
  public static boolean isTokenBlacklisted(String token) {
    Date expiration = invalidatedTokens.get(token);
    return expiration != null && expiration.after(new Date());
  }

  /** Removes expired tokens from the blocklist. */
  public static void removeExpiredTokens() {
    invalidatedTokens.entrySet().removeIf(entry -> entry.getValue().before(new Date()));
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

  // Validate JWT token
  public static boolean validateJwtToken(String token) {
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
  private static Claims extractAllClaims(String token) throws JwtException {
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

  /**
   * Adds additional claims to an existing token and regenerates it.
   *
   * @param claims The additional claims to add.
   * @return The regenerated token with additional claims.
   */
  public static String addClaimsAndRegenerateToken(Map<String, Object> claims) {
    Claims existingClaims = getClaimsFromRequest(null);
    if (Objects.isNull(existingClaims)) return null;
    existingClaims.forEach(claims::putIfAbsent);
    return generateToken(existingClaims.getSubject(), claims);
  }
}
