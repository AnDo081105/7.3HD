package com.example.demo.controller;

import com.examp                .andExp                .andExp                .andExpect(jsonPath(OPERATION_JSON_PATH).value("multiplication"))
                .andExpect(jsonPath(OPERAND1_JSON_PATH).value(a))
                .andExpect(jsonPath(OPERAND2_JSON_PATH).value(b))
                .andExpect(jsonPath(RESULT_JSON_PATH).value(expectedResult));jsonPath(OPERATION_JSON_PATH).value("subtraction"))
                .andExpect(jsonPath(OPERAND1_JSON_PATH).value(a))
                .andExpect(jsonPath(OPERAND2_JSON_PATH).value(b))
                .andExpect(jsonPath(RESULT_JSON_PATH).value(expectedResult));jsonPath(OPERATION_JSON_PATH).value("addition"))
                .andExpect(jsonPath(OPERAND1_JSON_PATH).value(a))
                .andExpect(jsonPath(OPERAND2_JSON_PATH).value(b))
                .andExpect(jsonPath(RESULT_JSON_PATH).value(expectedResult));emo.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for DemoController
 * These tests verify the REST API endpoints
 */
@WebMvcTest(DemoController.class)
class DemoControllerTest {

    private static final String OPERATION_JSON_PATH = "$.operation";
    private static final String OPERAND1_JSON_PATH = "$.operand1";
    private static final String OPERAND2_JSON_PATH = "$.operand2";
    private static final String RESULT_JSON_PATH = "$.result";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    @Test
    @DisplayName("Should return health status")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Jenkins Pipeline Demo"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    @DisplayName("Should perform addition correctly")
    void testAddEndpoint() throws Exception {
        // Given
        double a = 5.0;
        double b = 3.0;
        double expectedResult = 8.0;
        when(calculatorService.add(a, b)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/add")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("addition"))
                .andExpect(jsonPath("$.operand1").value(a))
                .andExpect(jsonPath("$.operand2").value(b))
                .andExpect(jsonPath("$.result").value(expectedResult));
    }

    @Test
    @DisplayName("Should perform subtraction correctly")
    void testSubtractEndpoint() throws Exception {
        // Given
        double a = 10.0;
        double b = 3.0;
        double expectedResult = 7.0;
        when(calculatorService.subtract(a, b)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/subtract")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("subtraction"))
                .andExpect(jsonPath("$.operand1").value(a))
                .andExpect(jsonPath("$.operand2").value(b))
                .andExpect(jsonPath("$.result").value(expectedResult));
    }

    @Test
    @DisplayName("Should perform multiplication correctly")
    void testMultiplyEndpoint() throws Exception {
        // Given
        double a = 4.0;
        double b = 3.0;
        double expectedResult = 12.0;
        when(calculatorService.multiply(a, b)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/multiply")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("multiplication"))
                .andExpect(jsonPath("$.operand1").value(a))
                .andExpect(jsonPath("$.operand2").value(b))
                .andExpect(jsonPath("$.result").value(expectedResult));
    }

    @Test
    @DisplayName("Should perform division correctly")
    void testDivideEndpoint() throws Exception {
        // Given
        double a = 15.0;
        double b = 3.0;
        double expectedResult = 5.0;
        when(calculatorService.divide(a, b)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/divide")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpected(jsonPath(OPERATION_JSON_PATH).value("division"))
                .andExpected(jsonPath(OPERAND1_JSON_PATH).value(a))
                .andExpected(jsonPath(OPERAND2_JSON_PATH).value(b))
                .andExpect(jsonPath(RESULT_JSON_PATH).value(expectedResult));
    }

    @Test
    @DisplayName("Should return error when dividing by zero")
    void testDivideByZeroEndpoint() throws Exception {
        // Given
        double a = 10.0;
        double b = 0.0;

        // When & Then
        mockMvc.perform(get("/api/divide")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Division by zero is not allowed"));
    }

    @Test
    @DisplayName("Should handle missing parameters")
    void testMissingParameters() throws Exception {
        mockMvc.perform(get("/api/add")
                .param("a", "5.0"))
                .andExpect(status().isBadRequest());
    }
}