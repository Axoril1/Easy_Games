package com.porikroma.util;

import com.porikroma.model.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class SessionManager {
    
    private static SessionManager instance;
    private static final String SESSION_DIR = System.getProperty("user.home") + "/.porikroma";
    private static final String SESSION_FILE = SESSION_DIR + "/session.properties";
    
    @Getter
    private String currentToken;
    
    @Getter
    private String refreshToken;
    
    @Getter
    private User currentUser;
    
    @Getter
    private boolean loggedIn = false;
    
    private SessionManager() {
        loadSession();
    }
    
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void login(String token, String refreshToken, User user) {
        this.currentToken = token;
        this.refreshToken = refreshToken;
        this.currentUser = user;
        this.loggedIn = true;
        
        saveSession();
        log.info("User {} logged in successfully", user.getUsername());
    }
    
    public void logout() {
        this.currentToken = null;
        this.refreshToken = null;
        this.currentUser = null;
        this.loggedIn = false;
        
        clearSession();
        log.info("User logged out successfully");
    }
    
    public void updateToken(String newToken) {
        this.currentToken = newToken;
        saveSession();
    }
    
    private void saveSession() {
        try {
            // Create directory if it doesn't exist
            Path sessionDir = Paths.get(SESSION_DIR);
            if (!Files.exists(sessionDir)) {
                Files.createDirectories(sessionDir);
            }
            
            Properties props = new Properties();
            if (currentToken != null) {
                props.setProperty("token", currentToken);
            }
            if (refreshToken != null) {
                props.setProperty("refreshToken", refreshToken);
            }
            if (currentUser != null) {
                props.setProperty("userId", String.valueOf(currentUser.getId()));
                props.setProperty("username", currentUser.getUsername());
                props.setProperty("email", currentUser.getEmail());
                props.setProperty("firstName", currentUser.getFirstName());
                props.setProperty("lastName", currentUser.getLastName());
            }
            
            try (FileOutputStream fos = new FileOutputStream(SESSION_FILE)) {
                props.store(fos, "Porikroma Session Data");
            }
            
        } catch (IOException e) {
            log.error("Failed to save session", e);
        }
    }
    
    private void loadSession() {
        try {
            File sessionFile = new File(SESSION_FILE);
            if (!sessionFile.exists()) {
                return;
            }
            
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(sessionFile)) {
                props.load(fis);
            }
            
            this.currentToken = props.getProperty("token");
            this.refreshToken = props.getProperty("refreshToken");
            
            if (currentToken != null && !currentToken.isEmpty()) {
                // Reconstruct user object
                String userIdStr = props.getProperty("userId");
                if (userIdStr != null) {
                    this.currentUser = new User();
                    this.currentUser.setId(Long.parseLong(userIdStr));
                    this.currentUser.setUsername(props.getProperty("username"));
                    this.currentUser.setEmail(props.getProperty("email"));
                    this.currentUser.setFirstName(props.getProperty("firstName"));
                    this.currentUser.setLastName(props.getProperty("lastName"));
                    
                    this.loggedIn = true;
                    log.info("Session restored for user: {}", currentUser.getUsername());
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to load session", e);
            clearSession();
        }
    }
    
    private void clearSession() {
        try {
            File sessionFile = new File(SESSION_FILE);
            if (sessionFile.exists()) {
                sessionFile.delete();
            }
        } catch (Exception e) {
            log.error("Failed to clear session file", e);
        }
    }
    
    public String getAuthorizationHeader() {
        if (currentToken != null && !currentToken.isEmpty()) {
            return "Bearer " + currentToken;
        }
        return null;
    }
}