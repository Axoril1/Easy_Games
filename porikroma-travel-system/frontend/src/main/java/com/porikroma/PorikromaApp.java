package com.porikroma;

import com.formdev.flatlaf.FlatLightLaf;
import com.porikroma.config.AppConfig;
import com.porikroma.service.ApiService;
import com.porikroma.service.AuthService;
import com.porikroma.ui.LoginFrame;
import com.porikroma.ui.MainFrame;
import com.porikroma.util.SessionManager;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class PorikromaApp {
    
    public static void main(String[] args) {
        // Set system properties for better UI experience
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Set FlatLaf look and feel
                FlatLightLaf.setup();
                
                // Initialize configuration
                AppConfig.initialize();
                
                // Initialize services
                ApiService apiService = new ApiService();
                AuthService authService = new AuthService(apiService);
                SessionManager sessionManager = SessionManager.getInstance();
                
                // Check if user is already logged in
                if (sessionManager.isLoggedIn()) {
                    log.info("User already logged in, showing main application");
                    showMainApplication(authService, apiService);
                } else {
                    log.info("No active session, showing login screen");
                    showLoginScreen(authService, apiService);
                }
                
            } catch (Exception e) {
                log.error("Failed to start application", e);
                JOptionPane.showMessageDialog(null, 
                    "Failed to start Porikroma: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    private static void showLoginScreen(AuthService authService, ApiService apiService) {
        LoginFrame loginFrame = new LoginFrame(authService, apiService);
        centerWindow(loginFrame);
        loginFrame.setVisible(true);
    }
    
    private static void showMainApplication(AuthService authService, ApiService apiService) {
        MainFrame mainFrame = new MainFrame(authService, apiService);
        centerWindow(mainFrame);
        mainFrame.setVisible(true);
    }
    
    private static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();
        
        if (windowSize.width == 0 || windowSize.height == 0) {
            window.pack();
            windowSize = window.getSize();
        }
        
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;
        
        window.setLocation(x, y);
    }
}