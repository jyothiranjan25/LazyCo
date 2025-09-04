package com.example.lazyco.backend.core.WebMVC;

import static com.example.lazyco.backend.core.Utils.CommonConstrains.BACKEND_PACKAGE;

import com.example.lazyco.backend.core.DatabaseConf.PostgresConfig;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.QueryParams.QueryParamsArgumentResolver;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({PostgresConfig.class})
@ComponentScan(basePackages = {BACKEND_PACKAGE})
@PropertySources({@PropertySource("classpath:application.properties")})
public class BackendWebConf implements WebMvcConfigurer {

  private Gson gson;
  private QueryParamsArgumentResolver queryParamsArgumentResolver;

  @Autowired
  public void injectDependencies(
      Gson gson, QueryParamsArgumentResolver queryParamsArgumentResolver) {
    this.gson = gson;
    this.queryParamsArgumentResolver = queryParamsArgumentResolver;
  }

  @Bean
  public StandardServletMultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(queryParamsArgumentResolver);
  }

  @Bean
  public GsonHttpMessageConverter gsonHttpMessageConverter() {
    GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
    converter.setGson(gson);
    return converter;
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(gsonHttpMessageConverter());

    final ByteArrayHttpMessageConverter arrayHttpMessageConverter =
        new ByteArrayHttpMessageConverter();
    final List<MediaType> list = new ArrayList<>();
    list.add(MediaType.ALL);
    list.add(MediaType.IMAGE_JPEG);
    list.add(MediaType.IMAGE_PNG);
    list.add(MediaType.APPLICATION_OCTET_STREAM);
    list.add(new MediaType("text", "csv"));
    arrayHttpMessageConverter.setSupportedMediaTypes(list);
    converters.add(arrayHttpMessageConverter);
  }
}
