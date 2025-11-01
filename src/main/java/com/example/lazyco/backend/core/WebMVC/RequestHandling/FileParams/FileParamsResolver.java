package com.example.lazyco.backend.core.WebMVC.RequestHandling.FileParams;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.RequestHandlingHelper;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class FileParamsResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(FileParams.class);
  }

  /**
   * Resolves method arguments annotated with @FileParams by extracting file parameters from the
   * multipart request and populating the corresponding DTO.
   */
  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    StandardMultipartHttpServletRequest multipartRequest =
        (StandardMultipartHttpServletRequest) webRequest.getNativeRequest();
    try {
      AbstractDTO dto = RequestHandlingHelper.populateDTOFromRequest(parameter, multipartRequest);
      dto.setFileMap(RequestHandlingHelper.extractFileFromRequest(multipartRequest));
      return dto;
    } catch (Exception e) {
      throw new ExceptionWrapper("Failed to resolve file parameters", e);
    }
  }
}
