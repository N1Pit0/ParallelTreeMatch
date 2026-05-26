package com.bachelor;

import com.bachelor.algorithm.TreeMatch;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.parser.InputParserImpl;
import com.bachelor.preprocess.*;

import java.util.Scanner;
import java.util.concurrent.*;

import static com.bachelor.preprocess.GenTour.buildAndPreprocess;

public class Coordinator {
    static InputParser parser = new InputParserImpl();
    static Scanner scanner = new Scanner(System.in);

    // Leave this here for easy copy-paste
    // String subjectString = "f(f(a,b), f(f(a,a),a))";
    // String patternString = "f(f(a,A), B)";

    private static InputArray acceptInput(String prompt) {
        InputArray result = null;
        while (result == null) {
            try {
                System.out.println(prompt);
                result = parser.parseToArray(scanner.nextLine());
            } catch (InvalidInputException e) {
                System.out.println("Invalid input: " + e.getLocalizedMessage());
                System.out.println("Please try again.");
            }
        }
        return result;
    }

    public static void main(@SuppressWarnings("unused") String[] args) {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {

            // Get valid subject tree
            InputArray subjectTree = acceptInput("Please enter a subject tree:");

            // Get valid pattern tree
            InputArray patternTree = acceptInput("Please enter a pattern tree:");

            EulerChain pattern = buildAndPreprocess(executor, patternTree, 5);
            EulerChain subject = buildAndPreprocess(executor, subjectTree, 5);
            TreeMatch.performTreeMatch(executor, subject, pattern);

        } finally {
            scanner.close();
        }
    }
}

