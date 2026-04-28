package com.mbbank.mbbSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map directory paths to their respective index.html files
        registry.addViewController("/MB/employee").setViewName("forward:/MB/employee/index.html");
        registry.addViewController("/MB/employee/").setViewName("forward:/MB/employee/index.html");
        
        registry.addViewController("/MB/customer").setViewName("forward:/MB/customer/index.html");
        registry.addViewController("/MB/customer/").setViewName("forward:/MB/customer/index.html");
        
        registry.addViewController("/MB/sysadmin").setViewName("forward:/MB/sysadmin/index.html");
        registry.addViewController("/MB/sysadmin/").setViewName("forward:/MB/sysadmin/index.html");
    }
}
