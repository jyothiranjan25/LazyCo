package com.example.lazyco.backend.core.WebMVC;

import com.example.lazyco.backend.core.DatabaseConf.DatabaseConfig;
import com.example.lazyco.backend.schema.PackageSchema;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({DatabaseConfig.class})
@ComponentScan(basePackages = {PackageSchema.BACKEND_PACKAGE})
@PropertySources({@PropertySource("classpath:application.properties")})
public class BackendWebConf {}

