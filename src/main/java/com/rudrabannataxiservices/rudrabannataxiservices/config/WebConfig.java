package com.rudrabannataxiservices.rudrabannataxiservices.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = "/opt/rudra-taxi/uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);

        registry.addResourceHandler("/cars/**")
                .addResourceLocations("file:" + uploadPath + "cars/");

        registry.addResourceHandler("/gallery/**")
                .addResourceLocations("file:" + uploadPath + "gallery/");

        registry.addResourceHandler("/banners/**")
                .addResourceLocations("file:" + uploadPath + "banners/");
    }
}
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(
//            ResourceHandlerRegistry registry
//    ) {
//
//        // Current deployed app path
//        String appPath =
//                System.getProperty("catalina.base")
//                        + "/webapps/rudrabannataxiservices/";
//
//        // Upload folder path
//        String uploadPath = appPath + "uploads/";
//
//        System.out.println("📁 Upload Path: " + uploadPath);
//
//        // Main Uploads
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:" + uploadPath)
//                .setCachePeriod(3600);
//
//        // Cars Images
//        registry.addResourceHandler("/cars/**")
//                .addResourceLocations(
//                        "file:" + uploadPath + "cars/"
//                )
//                .setCachePeriod(3600);
//
//        // Gallery Images
//        registry.addResourceHandler("/gallery/**")
//                .addResourceLocations(
//                        "file:" + uploadPath + "gallery/"
//                )
//                .setCachePeriod(3600);
//
//        // Banner Images
//        registry.addResourceHandler("/banners/**")
//                .addResourceLocations(
//                        "file:" + uploadPath + "banners/"
//                )
//                .setCachePeriod(3600);
//    }
//}