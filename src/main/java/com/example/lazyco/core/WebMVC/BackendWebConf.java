package com.example.lazyco.core.WebMVC;

import static com.example.lazyco.core.Utils.CommonConstants.APPLICATION_PROPERTIES;
import static com.example.lazyco.core.Utils.CommonConstants.BACKEND_PACKAGE;

import com.example.lazyco.core.WebMVC.Interceptor.LoginControllerInterceptor;
import com.example.lazyco.core.WebMVC.Interceptor.RateLimitInterceptor;
import com.example.lazyco.core.WebMVC.Interceptor.RestControllerInterceptor;
import com.example.lazyco.core.WebMVC.Interceptor.RoleControllerInterceptor;
import com.example.lazyco.core.WebMVC.RequestHandling.CSVParams.CsvParamsResolver;
import com.example.lazyco.core.WebMVC.RequestHandling.FileParams.FileParamsResolver;
import com.example.lazyco.core.WebMVC.RequestHandling.QueryParams.QueryParamsResolver;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = {BACKEND_PACKAGE})
@PropertySources({@PropertySource("classpath:" + APPLICATION_PROPERTIES)})
@AllArgsConstructor
public class BackendWebConf implements WebMvcConfigurer {

  private Gson gson;
  private QueryParamsResolver queryParamsResolver;
  private FileParamsResolver fileParamsResolver;
  private CsvParamsResolver csvParamsResolver;
  private RateLimitInterceptor rateLimitInterceptor;
  private RestControllerInterceptor restControllerInterceptor;
  private LoginControllerInterceptor loginControllerInterceptor;
  private RoleControllerInterceptor roleControllerInterceptor;
  private Endpoints endpoints;

  @Bean
  public StandardServletMultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(queryParamsResolver);
    argumentResolvers.add(fileParamsResolver);
    argumentResolvers.add(csvParamsResolver);
  }

  @Bean
  public GsonHttpMessageConverter gsonHttpMessageConverter() {
    GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
    converter.setGson(gson);
    return converter;
  }

  @Override
  public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {

    // Add ByteArray converter for images and files
    final ByteArrayHttpMessageConverter arrayHttpMessageConverter =
        new ByteArrayHttpMessageConverter();
    final List<MediaType> list = new ArrayList<>();
    list.add(MediaType.ALL);
    list.add(MediaType.IMAGE_JPEG);
    list.add(MediaType.IMAGE_PNG);
    list.add(MediaType.APPLICATION_OCTET_STREAM);
    list.add(new MediaType("text", "csv"));
    arrayHttpMessageConverter.setSupportedMediaTypes(list);
    builder.addCustomConverter(arrayHttpMessageConverter);

    // String converter for text support
    builder.addCustomConverter(new StringHttpMessageConverter());

    // add StringHttpMessageConverter with US_ASCII charset
    StringHttpMessageConverter asciiConverter =
        new StringHttpMessageConverter(StandardCharsets.US_ASCII);
    builder.addCustomConverter(asciiConverter);

    // Add Gson converter
    builder.addCustomConverter(gsonHttpMessageConverter());
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    // This bean is necessary to resolve @Value and ${...}
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();

    // Add the properties file to the list of resources
    List<ClassPathResource> resources = new ArrayList<>();
    resources.add(new ClassPathResource(APPLICATION_PROPERTIES));

    // Set the location of the properties file
    configurer.setLocations(resources.toArray(new ClassPathResource[0]));
    configurer.setFileEncoding("UTF-8"); // Set the file encoding
    configurer.setIgnoreUnresolvablePlaceholders(false); // Ignore unresolvable placeholders
    configurer.setIgnoreResourceNotFound(false); // Ignore resource not found
    return configurer;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(rateLimitInterceptor);
    registry.addInterceptor(restControllerInterceptor);
    registry
        .addInterceptor(loginControllerInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(endpoints.getPublicEndpoints());
    registry
        .addInterceptor(roleControllerInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(endpoints.getExcludedRoleCheckEndpoints());
  }
}
