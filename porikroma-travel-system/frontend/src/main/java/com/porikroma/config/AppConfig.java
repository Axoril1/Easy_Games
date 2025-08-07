package com.porikroma.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class AppConfig {
    
    @Getter
    private static String apiBaseUrl;
    
    @Getter
    private static String appName;
    
    @Getter
    private static String appVersion;
    
    @Getter
    private static int connectionTimeout;
    
    @Getter
    private static int readTimeout;
    
    public static void initialize() {
        Properties props = new Properties();
        
        try (InputStream input = AppConfig.class.getResourceAsStream("/application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            log.warn("Could not load application.properties, using defaults", e);
        }
        
        apiBaseUrl = props.getProperty("api.base.url", "http://localhost:8080/api");
        appName = props.getProperty("app.name", "Porikroma Travel Management");
        appVersion = props.getProperty("app.version", "1.0.0");
        connectionTimeout = Integer.parseInt(props.getProperty("http.connection.timeout", "30000"));
        readTimeout = Integer.parseInt(props.getProperty("http.read.timeout", "30000"));
        
        log.info("Application configuration initialized:");
        log.info("API Base URL: {}", apiBaseUrl);
        log.info("App Name: {}", appName);
        log.info("App Version: {}", appVersion);
    }
}