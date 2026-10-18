package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.InputArrayFactory;

import java.util.Scanner;

public class UserDataLoader implements DataLoader {
    private final InputParser parser;
    private final static Scanner scanner = new Scanner(System.in);

    UserDataLoader(InputParser parser) {
        this.parser = parser;
    }

    public InputArray acceptInput(String prompt) {
        InputArray result = null;
        do {
            System.out.println(prompt);

            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("No more input available (input stream was closed)");
            }

            try {
                result = InputArrayFactory.create(parser.parse(scanner.nextLine()));
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
