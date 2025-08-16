package com.example.lazyco.backend.core;

import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@ImportResource({
        "classpath:ApplicationContext.xml",
        "classpath:DatabaseContext.xml",
})
@PropertySources({@PropertySource("classpath:application.properties")})
public class BackendWebConf {
}
