package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.parser.InputParserImpl;
import com.bachelor.preprocess.InputArray;

import java.util.Scanner;

// Leave this here for easy copy-paste
// String subjectString = "f(f(a,b), f(f(a,a),a))";
// String patternString = "f(f(a,A), B)";
//    String subjectString = "f(a,f(a,b,c),f(a,f(a,b,c),e),f)";
//    String patternString = "f(A,f(a,B,C),D)";

public class UserDataLoader implements DataLoader {
    private final static InputParser parser = new InputParserImpl();
    private final static Scanner scanner = new Scanner(System.in);

    public InputArray acceptInput(String prompt) {
        InputArray result = null;
        do {
            try {
                System.out.println(prompt);
                result = parser.parseToArray(scanner.nextLine());
            } catch (InvalidInputException e) {
                System.out.println("Invalid input: " + e.getLocalizedMessage());
                System.out.println("Please try again.");
            }
        } while (result == null);

        return result;
    }

    @Override
    public InputArray loadSubjectTree() {
        return acceptInput("Please enter a subject tree:");
    }

    @Override
    public InputArray loadPatternTree() {
        return acceptInput("Please enter a pattern tree:");
    }
}
