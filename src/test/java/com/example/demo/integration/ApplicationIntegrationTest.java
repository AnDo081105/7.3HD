package com.example.demo.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests that test the full application stack
 * These tests run the entire Spring Boot application
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class ApplicationIntegrationTest {

    private static final String BASE_URL_TEMPLATE = "http://localhost:";
    private static final String RESULT_KEY = "result";

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @DisplayName("Application should start successfully")
    void contextLoads() {
        // Test passes if the application context loads without errors
    }

    @Test
    @DisplayName("Health endpoint should return UP status")
    void testHealthEndpoint() {
        String url = BASE_URL_TEMPLATE + port + "/api/health";
        
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("Jenkins Pipeline Demo", response.getBody().get("service"));
        assertEquals("1.0.0", response.getBody().get("version"));
    }

    @Test
    @DisplayName("Calculator endpoints should work end-to-end")
    void testCalculatorEndpoints() {
        String baseUrl = BASE_URL_TEMPLATE + port + "/api";
        
        // Test addition
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> addResponse = (ResponseEntity<Map<String, Object>>) restTemplate.getForEntity(
            baseUrl + "/add?a=5&b=3", Map.class);
        assertEquals(HttpStatus.OK, addResponse.getStatusCode());
        assertEquals(8.0, addResponse.getBody().get(RESULT_KEY));
        
        // Test subtraction
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> subtractResponse = (ResponseEntity<Map<String, Object>>) restTemplate.getForEntity(
            baseUrl + "/subtract?a=10&b=3", Map.class);
        assertEquals(HttpStatus.OK, subtractResponse.getStatusCode());
        assertEquals(7.0, subtractResponse.getBody().get(RESULT_KEY));
        
        // Test multiplication
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> multiplyResponse = (ResponseEntity<Map<String, Object>>) restTemplate.getForEntity(
            baseUrl + "/multiply?a=4&b=3", Map.class);
        assertEquals(HttpStatus.OK, multiplyResponse.getStatusCode());
        assertEquals(12.0, multiplyResponse.getBody().get(RESULT_KEY));
        
        // Test division
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> divideResponse = (ResponseEntity<Map<String, Object>>) restTemplate.getForEntity(
            baseUrl + "/divide?a=15&b=3", Map.class);
        assertEquals(HttpStatus.OK, divideResponse.getStatusCode());
        assertEquals(5.0, divideResponse.getBody().get(RESULT_KEY));
    }

    @Test
    @DisplayName("Division by zero should return error")
    void testDivisionByZeroError() {
        String url = BASE_URL_TEMPLATE + port + "/api/divide?a=10&b=0";
        
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Division by zero is not allowed", response.getBody().get("error"));
    }
}