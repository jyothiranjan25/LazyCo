package com.example.lazyco.backend.core.web;

import com.example.lazyco.backend.core.databaseconf.DatabaseConfig;
import com.example.lazyco.backend.schema.PackageSchema;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({DatabaseConfig.class})
@ComponentScan(basePackages = {PackageSchema.BACKEND_PACKAGE})
@PropertySources({@PropertySource("classpath:application.properties")})
public class BackendWebConf {}

