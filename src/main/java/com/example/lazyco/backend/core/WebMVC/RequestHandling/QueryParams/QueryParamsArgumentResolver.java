package com.example.lazyco.backend.core.WebMVC.RequestHandling.QueryParams;

import com.example.lazyco.backend.core.JSONUtils.JSONUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class QueryParamsArgumentResolver implements HandlerMethodArgumentResolver {

  private Gson gson;

  @Autowired
  public void injectDependencies(Gson gson) {
    this.gson = gson;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    // This resolver supports all DTOs that have a specific annotation or follow a naming convention
    return parameter.hasParameterAnnotation(QueryParams.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    JSONObject paramMap = getRequestParametersJSON(request);
    Class<?> parameterType = parameter.getParameterType();
    return gson.fromJson(paramMap.toString(), parameterType);
  }

  public static JSONObject getRequestParametersJSON(HttpServletRequest request) {
    JSONObject paramMap = new JSONObject();

    // Process query parameters
    Enumeration<String> requestParams = request.getParameterNames();
    while (requestParams.hasMoreElements()) {
      String key = requestParams.nextElement();
      String parameter = request.getParameter(key);
      if (parameter != null) {
        parameter = parameter.trim();
      }
      JSONUtils.addParameterToJson(paramMap, key, parameter);
    }
    return paramMap;
  }
}
