package com.porikroma.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.porikroma.config.AppConfig;
import com.porikroma.util.SessionManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ApiService {
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public ApiService() {
        this.baseUrl = AppConfig.getApiBaseUrl();
        
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(AppConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(AppConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();
        
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    public <T> ApiResponse<T> get(String endpoint, Class<T> responseType) throws ApiException {
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .get()
                .build();
        
        return executeRequest(request, responseType);
    }
    
    public <T> ApiResponse<T> post(String endpoint, Object requestBody, Class<T> responseType) throws ApiException {
        RequestBody body = createJsonRequestBody(requestBody);
        
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .post(body)
                .build();
        
        return executeRequest(request, responseType);
    }
    
    public <T> ApiResponse<T> put(String endpoint, Object requestBody, Class<T> responseType) throws ApiException {
        RequestBody body = createJsonRequestBody(requestBody);
        
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .put(body)
                .build();
        
        return executeRequest(request, responseType);
    }
    
    public <T> ApiResponse<T> delete(String endpoint, Class<T> responseType) throws ApiException {
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .delete()
                .build();
        
        return executeRequest(request, responseType);
    }
    
    private RequestBody createJsonRequestBody(Object object) throws ApiException {
        try {
            String json = objectMapper.writeValueAsString(object);
            return RequestBody.create(json, MediaType.get("application/json"));
        } catch (Exception e) {
            throw new ApiException("Failed to serialize request body", e);
        }
    }
    
    private <T> ApiResponse<T> executeRequest(Request request, Class<T> responseType) throws ApiException {
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            
            if (response.isSuccessful()) {
                T data = null;
                if (responseType != Void.class && !responseBody.isEmpty()) {
                    data = objectMapper.readValue(responseBody, responseType);
                }
                return new ApiResponse<>(true, response.code(), null, data);
            } else {
                String errorMessage = extractErrorMessage(responseBody);
                return new ApiResponse<>(false, response.code(), errorMessage, null);
            }
            
        } catch (IOException e) {
            log.error("API request failed", e);
            throw new ApiException("Network error: " + e.getMessage(), e);
        }
    }
    
    private String extractErrorMessage(String responseBody) {
        try {
            if (responseBody != null && !responseBody.isEmpty()) {
                var errorNode = objectMapper.readTree(responseBody);
                if (errorNode.has("message")) {
                    return errorNode.get("message").asText();
                }
                if (errorNode.has("error")) {
                    return errorNode.get("error").asText();
                }
            }
        } catch (Exception e) {
            log.debug("Could not parse error response", e);
        }
        return "Unknown error occurred";
    }
    
    private static class AuthInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            
            String authHeader = SessionManager.getInstance().getAuthorizationHeader();
            if (authHeader != null) {
                Request authenticatedRequest = originalRequest.newBuilder()
                        .header("Authorization", authHeader)
                        .build();
                return chain.proceed(authenticatedRequest);
            }
            
            return chain.proceed(originalRequest);
        }
    }
    
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.nanoTime();
            
            log.debug("Sending request: {} {}", request.method(), request.url());
            
            Response response = chain.proceed(request);
            
            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            
            log.debug("Received response: {} {} in {}ms", 
                    response.code(), request.url(), duration);
            
            return response;
        }
    }
    
    public static class ApiResponse<T> {
        private final boolean success;
        private final int statusCode;
        private final String errorMessage;
        private final T data;
        
        public ApiResponse(boolean success, int statusCode, String errorMessage, T data) {
            this.success = success;
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
            this.data = data;
        }
        
        public boolean isSuccess() { return success; }
        public int getStatusCode() { return statusCode; }
        public String getErrorMessage() { return errorMessage; }
        public T getData() { return data; }
    }
    
    public static class ApiException extends Exception {
        public ApiException(String message) {
            super(message);
        }
        
        public ApiException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}