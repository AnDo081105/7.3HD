package com.example.demo.controller;

import com.example.demo.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for demonstration purposes
 * Provides basic arithmetic operations and health check
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    private static final String OPERATION_KEY = "operation";
    private static final String OPERAND1_KEY = "operand1";
    private static final String OPERAND2_KEY = "operand2";
    private static final String RESULT_KEY = "result";

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Jenkins Pipeline Demo");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> add(
            @RequestParam double a, 
            @RequestParam double b) {
        
        double result = calculatorService.add(a, b);
        Map<String, Object> response = new HashMap<>();
        response.put(OPERATION_KEY, "addition");
        response.put(OPERAND1_KEY, a);
        response.put(OPERAND2_KEY, b);
        response.put(RESULT_KEY, result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subtract")
    public ResponseEntity<Map<String, Object>> subtract(
            @RequestParam double a, 
            @RequestParam double b) {
        
        double result = calculatorService.subtract(a, b);
        Map<String, Object> response = new HashMap<>();
        response.put(OPERATION_KEY, "subtraction");
        response.put(OPERAND1_KEY, a);
        response.put(OPERAND2_KEY, b);
        response.put(RESULT_KEY, result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/multiply")
    public ResponseEntity<Map<String, Object>> multiply(
            @RequestParam double a, 
            @RequestParam double b) {
        
        double result = calculatorService.multiply(a, b);
        Map<String, Object> response = new HashMap<>();
        response.put(OPERATION_KEY, "multiplication");
        response.put(OPERAND1_KEY, a);
        response.put(OPERAND2_KEY, b);
        response.put(RESULT_KEY, result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/divide")
    public ResponseEntity<Map<String, Object>> divide(
            @RequestParam double a, 
            @RequestParam double b) {
        
        if (b == 0) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Division by zero is not allowed");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        double result = calculatorService.divide(a, b);
        Map<String, Object> response = new HashMap<>();
        response.put(OPERATION_KEY, "division");
        response.put(OPERAND1_KEY, a);
        response.put(OPERAND2_KEY, b);
        response.put(RESULT_KEY, result);
        return ResponseEntity.ok(response);
    }
}