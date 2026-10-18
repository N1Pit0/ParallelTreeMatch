package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.parser.InputParserImpl;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.InputArrayFactory;

import java.util.Scanner;

public class UserDataLoader implements DataLoader {
    private final static InputParser parser = new InputParserImpl();
    private final static Scanner scanner = new Scanner(System.in);

    public InputArray acceptInput(String prompt) {
        InputArray result = null;
        do {
            try {
                System.out.println(prompt);
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
