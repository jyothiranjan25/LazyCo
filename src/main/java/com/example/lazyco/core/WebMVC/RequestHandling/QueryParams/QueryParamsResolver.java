package com.example.lazyco.core.WebMVC.RequestHandling.QueryParams;

import com.example.lazyco.core.GosnConf.GsonSingleton;
import com.example.lazyco.core.JSONUtils.JSONUtils;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class QueryParamsResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(QueryParams.class);
  }

  /**
   * Resolves method arguments annotated with @QueryParams by extracting query parameters from the
   * HTTP request and deserializing them into the target DTO.
   */
  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {

    try {
      HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
      JSONObject paramMap = getRequestParametersJSON(request);

      if (paramMap.isEmpty()) {
        return createDefaultInstance(parameter.getParameterType());
      }

      Class<?> parameterType = parameter.getParameterType();
      return GsonSingleton.getInstance().fromJson(paramMap.toString(), parameterType);

    } catch (JsonSyntaxException e) {
      // Log the error and return a default instance
      ApplicationLogger.error("Failed to deserialize query parameters", e);
      return createDefaultInstance(parameter.getParameterType());
    } catch (Exception e) {
      // Handle any other unexpected errors
      ApplicationLogger.error("Failed to deserialize query parameters", e);
      return createDefaultInstance(parameter.getParameterType());
    }
  }

  /**
   * Extracts all query parameters from the HTTP request and converts them to a JSON object.
   *
   * @param request the HTTP servlet request
   * @return a JSONObject containing all query parameters, or an empty JSONObject if none exist
   */
  public static JSONObject getRequestParametersJSON(HttpServletRequest request) {
    if (request == null) {
      return new JSONObject();
    }

    JSONObject paramMap = new JSONObject();

    try {
      // Process query parameters
      Enumeration<String> requestParams = request.getParameterNames();
      while (requestParams.hasMoreElements()) {
        String key = requestParams.nextElement();
        if (key == null) {
          continue; // Skip null keys
        }

        String parameter = request.getParameter(key);
        if (parameter != null) {
          parameter = parameter.trim();
          if (parameter.isEmpty()) {
            parameter = null; // Convert empty strings to null
          }
        }

        JSONUtils.addParameterToJson(paramMap, key, parameter);
      }
    } catch (Exception e) {
      ApplicationLogger.debug("Error processing request parameters: " + e.getMessage(), e);
      // Return empty JSON object on error to allow graceful degradation
      return new JSONObject();
    }

    return paramMap;
  }

  /**
   * Creates a default instance of the target class when parameter resolution fails.
   *
   * @param targetClass the class to instantiate
   * @return a new instance of the target class, or null if instantiation fails
   */
  private Object createDefaultInstance(Class<?> targetClass) {
    if (targetClass == null) {
      return null;
    }

    try {
      // Try to create a new instance using the default constructor
      return targetClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      ApplicationLogger.error(
          "Failed to create default instance of " + targetClass.getSimpleName(), e);
      return null;
    }
  }
}
