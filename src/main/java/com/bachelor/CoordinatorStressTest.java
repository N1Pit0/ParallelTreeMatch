package com.bachelor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoordinatorStressTest {

    private static final String OUTPUT_FILE = "stress_test_exceptions.log";
    private static final int ITERATIONS = 3000;

    public static void main(String[] args) {
        int successCount = 0;
        int failureCount = 0;

        try (FileWriter fw = new FileWriter(OUTPUT_FILE, true);
             PrintWriter writer = new PrintWriter(fw)) {

            writer.println("=".repeat(80));
            writer.println("STRESS TEST STARTED: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Total Iterations: " + ITERATIONS);
            writer.println("=".repeat(80));
            writer.flush();

            for (int i = 1; i <= ITERATIONS; i++) {
                try {
                    System.out.println("Running iteration " + i + "/" + ITERATIONS);
                    Coordinator.main(new String[]{});
                    successCount++;
                    System.out.println("✓ Iteration " + i + " completed successfully");
                } catch (Exception e) {
                    failureCount++;
                    writer.println();
                    writer.println("EXCEPTION at Iteration " + i + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    writer.println("Exception Type: " + e.getClass().getName());
                    writer.println("Message: " + e.getMessage());
                    writer.println("Stack Trace:");
                    e.printStackTrace(writer);
                    writer.println("-".repeat(80));
                    writer.flush();

                    System.out.println("✗ Iteration " + i + " failed: " + e.getClass().getName() + " - " + e.getMessage());
                }
            }

            writer.println();
            writer.println("=".repeat(80));
            writer.println("STRESS TEST COMPLETED: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Total Iterations: " + ITERATIONS);
            writer.println("Successful: " + successCount);
            writer.println("Failed: " + failureCount);
            writer.println("Success Rate: " + String.format("%.2f%%", (successCount * 100.0 / ITERATIONS)));
            writer.println("=".repeat(80));
            writer.flush();

            System.out.println();
            System.out.println("=".repeat(80));
            System.out.println("STRESS TEST COMPLETED");
            System.out.println("Successful: " + successCount + "/" + ITERATIONS);
            System.out.println("Failed: " + failureCount + "/" + ITERATIONS);
            System.out.println("Success Rate: " + String.format("%.2f%%", (successCount * 100.0 / ITERATIONS)));
            System.out.println("Exceptions logged to: " + OUTPUT_FILE);
            System.out.println("=".repeat(80));

        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

