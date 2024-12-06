//package com.promptdb.auth.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//@Configuration
//public class RequestConfig implements WebMvcConfigurer {
//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//        // Custom RequestMappingHandlerMapping to add a global prefix
//        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
//        handlerMapping.setUseSuffixPatternMatch(false);  // Optional: Controls suffix pattern matching (e.g., .json)
//        handlerMapping.setAlwaysUseFullPath(true); // This will ensure the full path is used (important for prefix)
//        handlerMapping.setPrefix("/api"); // Set the global prefix here
//        return handlerMapping;
//    }
//}
