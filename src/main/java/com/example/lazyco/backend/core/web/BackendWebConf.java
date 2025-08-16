package com.example.lazyco.backend.core.web;

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
@ComponentScan(basePackages = {"com.example.lazyco.backend"})
@ImportResource({
        "classpath:ApplicationContext.xml",
        "classpath:DatabaseContext.xml",
})
@EnableTransactionManagement
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySources({@PropertySource("classpath:application.properties")})
public class BackendWebConf {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        // This bean is necessary to resolve @Value and ${...}
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();

        // Add the properties file to the list of resources
        List<ClassPathResource> resources = new ArrayList<>();
        resources.add(new ClassPathResource("application.properties"));

        // Set the location of the properties file
        configurer.setLocations(resources.toArray(new ClassPathResource[0]));
        configurer.setFileEncoding("UTF-8"); // Set the file encoding
        configurer.setIgnoreUnresolvablePlaceholders(false); // Ignore unresolvable placeholders
        configurer.setIgnoreResourceNotFound(false); // Ignore resource not found
        return configurer;
    }
}
