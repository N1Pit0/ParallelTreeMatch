package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.data.json.InputPair;
import com.bachelor.data.json.LoadResult;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.InputArrayFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SampleDataLoader implements DataLoader {

    private record Sample(String subject, String pattern) {}

    private static final List<Sample> SAMPLES = List.of(
            new Sample("f(f(a,b), f(f(a,a),a))",          "f(f(a,A), B)"),
            new Sample("f(a,f(a,b,c),f(a,f(a,b,c),e),f)", "f(A,f(a,B,C),D)")
    );

    private final InputParser parser;

    SampleDataLoader(InputParser parser) {
        this.parser = Objects.requireNonNull(parser);
    }

    @Override
    public LoadResult load() {
        List<InputPair> pairs = new ArrayList<>();
        int entryNumber = 0;

        for (Sample sample : SAMPLES) {
            entryNumber++;
            pairs.add(new InputPair(entryNumber,
                    sample.subject(), sample.pattern(),
                    parse(sample.subject()), parse(sample.pattern())));
        }
        return LoadResult.of(pairs);
    }

    /** Samples are hardcoded, so an invalid one is a programming error, not a user error. */
    private InputArray parse(String text) {
        try {
            return InputArrayFactory.create(parser.parse(text));
        } catch (InvalidInputException e) {
            throw new IllegalStateException("Built-in sample is invalid: " + text, e);
        }
    }
}