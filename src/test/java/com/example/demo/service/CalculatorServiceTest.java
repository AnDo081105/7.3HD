package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CalculatorService
 * These tests verify the business logic of arithmetic operations
 */
@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    @DisplayName("Should add two positive numbers correctly")
    void testAddPositiveNumbers() {
        // Given
        double a = 5.0;
        double b = 3.0;
        double expected = 8.0;

        // When
        double result = calculatorService.add(a, b);

        // Then
        assertEquals(expected, result, 0.001);
    }

    @Test
    @DisplayName("Should add negative numbers correctly")
    void testAddNegativeNumbers() {
        // Given
        double a = -5.0;
        double b = -3.0;
        double expected = -8.0;

        // When
        double result = calculatorService.add(a, b);

        // Then
        assertEquals(expected, result, 0.001);
    }

    @Test
    @DisplayName("Should subtract numbers correctly")
    void testSubtract() {
        // Given
        double a = 10.0;
        double b = 3.0;
        double expected = 7.0;

        // When
        double result = calculatorService.subtract(a, b);

        // Then
        assertEquals(expected, result, 0.001);
    }

    @Test
    @DisplayName("Should multiply numbers correctly")
    void testMultiply() {
        // Given
        double a = 4.0;
        double b = 3.0;
        double expected = 12.0;

        // When
        double result = calculatorService.multiply(a, b);

        // Then
        assertEquals(expected, result, 0.001);
    }

    @Test
    @DisplayName("Should divide numbers correctly")
    void testDivide() {
        // Given
        double a = 15.0;
        double b = 3.0;
        double expected = 5.0;

        // When
        double result = calculatorService.divide(a, b);

        // Then
        assertEquals(expected, result, 0.001);
    }

    @Test
    @DisplayName("Should throw exception when dividing by zero")
    void testDivideByZero() {
        // Given
        double a = 10.0;
        double b = 0.0;

        // When & Then
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> 
            calculatorService.divide(a, b)
        );

        assertEquals("Division by zero is not allowed", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle decimal numbers correctly")
    void testDecimalOperations() {
        // Given
        double a = 2.5;
        double b = 1.5;

        // When & Then
        assertEquals(4.0, calculatorService.add(a, b), 0.001);
        assertEquals(1.0, calculatorService.subtract(a, b), 0.001);
        assertEquals(3.75, calculatorService.multiply(a, b), 0.001);
        assertEquals(1.666, calculatorService.divide(a, b), 0.001);
    }

    @Test
    @DisplayName("Should handle zero as operand correctly")
    void testZeroOperand() {
        // Given
        double a = 5.0;
        double b = 0.0;

        // When & Then
        assertEquals(5.0, calculatorService.add(a, b), 0.001);
        assertEquals(5.0, calculatorService.subtract(a, b), 0.001);
        assertEquals(0.0, calculatorService.multiply(a, b), 0.001);
        // Division by zero is handled separately in testDivideByZero
    }
}