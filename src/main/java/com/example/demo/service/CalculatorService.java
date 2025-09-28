package com.example.demo.service;

import org.springframework.stereotype.Service;

/**
 * Service class that provides basic arithmetic operations
 * This class contains the business logic for the calculator functionality
 */
@Service
public class CalculatorService {

    /**
     * Adds two numbers
     * @param a first operand
     * @param b second operand
     * @return sum of a and b
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtracts second number from first
     * @param a first operand
     * @param b second operand
     * @return difference of a and b
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplies two numbers
     * @param a first operand
     * @param b second operand
     * @return product of a and b
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divides first number by second
     * @param a dividend
     * @param b divisor
     * @return quotient of a and b
     * @throws ArithmeticException if b is zero
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return a / b;
    }
}