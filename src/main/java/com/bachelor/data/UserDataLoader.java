package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.data.json.InputPair;
import com.bachelor.data.json.LoadResult;
import com.bachelor.exceptions.DataLoadException;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.InputArrayFactory;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UserDataLoader implements DataLoader {

    // Shared and never closed: closing it would close System.in
    private static final Scanner scanner = new Scanner(System.in);

    private final InputParser parser;

    UserDataLoader(InputParser parser) {
        this.parser = Objects.requireNonNull(parser);
    }

    @Override
    public LoadResult load() throws DataLoadException {
        ParsedInput subject = acceptInput("Please enter a subject tree:");
        ParsedInput pattern = acceptInput("Please enter a pattern tree:");

        return LoadResult.of(List.of(
                new InputPair(1, subject.text(), pattern.text(), subject.tree(), pattern.tree())));
    }

    private record ParsedInput(String text, InputArray tree) {}

    private ParsedInput acceptInput(String prompt) throws DataLoadException {
        while (true) {
            System.out.println(prompt);

            if (!scanner.hasNextLine()) {
                throw new DataLoadException("No more input available (input stream was closed)");
            }

            String line = scanner.nextLine();
            try {
                return new ParsedInput(line.trim(), InputArrayFactory.create(parser.parse(line)));
            } catch (InvalidInputException e) {
                System.out.println("Invalid input: " + e.getLocalizedMessage());
                System.out.println("Please try again.");
            }
        }
    }
}