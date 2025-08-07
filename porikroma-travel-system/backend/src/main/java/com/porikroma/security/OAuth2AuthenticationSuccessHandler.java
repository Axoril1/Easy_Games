package com.porikroma.security;

import com.porikroma.model.Role;
import com.porikroma.model.User;
import com.porikroma.repository.RoleRepository;
import com.porikroma.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Value("${app.oauth2.authorizedRedirectUri:http://localhost:8081/oauth2/redirect}")
    private String redirectUri;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException {
        
        String targetUrl = determineTargetUrl(request, response, authentication);
        
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
    
    protected String determineTargetUrl(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      Authentication authentication) {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub");
        
        // Find or create user
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;
        
        if (existingUser.isPresent()) {
            user = existingUser.get();
            if (user.getGoogleId() == null) {
                user.setGoogleId(googleId);
                user.setIsOauthUser(true);
                userRepository.save(user);
            }
        } else {
            // Create new user
            user = new User();
            user.setEmail(email);
            user.setUsername(email); // Use email as username for OAuth users
            user.setFirstName(extractFirstName(name));
            user.setLastName(extractLastName(name));
            user.setGoogleId(googleId);
            user.setIsOauthUser(true);
            user.setIsEmailVerified(true);
            user.setIsActive(true);
            
            // Assign default role
            Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRoles(Set.of(userRole));
            
            user = userRepository.save(user);
        }
        
        String token = jwtUtils.generateJwtToken(user.getUsername(), user.getId(), user.getEmail());
        
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString();
    }
    
    private String extractFirstName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "User";
        }
        String[] parts = name.split(" ");
        return parts[0];
    }
    
    private String extractLastName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        String[] parts = name.split(" ");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }
        return "";
    }
}