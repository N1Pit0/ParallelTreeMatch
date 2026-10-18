package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.*;
import jdk.jshell.spi.ExecutionControl;

public class SampleDataLoader implements DataLoader {
    private final InputParser parser;

    SampleDataLoader(InputParser parser) {
        this.parser = parser;
    }

    @Override
    public InputArray loadSubjectTree() throws InvalidInputException {
        return InputArrayFactory.create(parser.parse("f(f(a,b), f(f(a,a),a))"));
    }

    @Override
    public InputArray loadPatternTree() throws InvalidInputException {
        return InputArrayFactory.create(parser.parse("f(f(a,A), B)"));
    }

    //TODO: Use adapter pattern here
    @Override
    public InputArray acceptInput(String prompt) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("method not available for term variable");
    }
}
