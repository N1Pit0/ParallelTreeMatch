package com.bachelor.data;

import com.bachelor.InputParser;

import java.util.Arrays;
import java.util.function.Function;

public enum DataSourceType {

    SAMPLE(SampleDataLoader::new),
    USER(UserDataLoader::new);

    private final Function<InputParser, DataLoader> creator;

    DataSourceType(Function<InputParser, DataLoader> creator) {
        this.creator = creator;
    }

    public DataLoader createLoader(InputParser parser) {
        return creator.apply(parser);
    }

    public static DataSourceType from(String value) {
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown com.bachelor.application.datasource '" + value
                    + "'. Valid values: " + Arrays.toString(values()));
        }
    }
}