package com.example.demo.controller;

import com.example.demo.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for DemoController
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DemoControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

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
        double a = 5.0;
        double b = 3.0;
        double expectedResult = 8.0;
        when(calculatorService.add(a, b)).thenReturn(expectedResult);

        mockMvc.perform(get("/api/add")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(expectedResult));
    }

    @Test
    @DisplayName("Should perform subtraction correctly")
    void testSubtractEndpoint() throws Exception {
        double a = 10.0;
        double b = 3.0;
        double expectedResult = 7.0;
        when(calculatorService.subtract(a, b)).thenReturn(expectedResult);

        mockMvc.perform(get("/api/subtract")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(expectedResult));
    }

    @Test
    @DisplayName("Should perform multiplication correctly")
    void testMultiplyEndpoint() throws Exception {
        double a = 4.0;
        double b = 3.0;
        double expectedResult = 12.0;
        when(calculatorService.multiply(a, b)).thenReturn(expectedResult);

        mockMvc.perform(get("/api/multiply")
                .param("a", String.valueOf(a))
                .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(expectedResult));
    }
}