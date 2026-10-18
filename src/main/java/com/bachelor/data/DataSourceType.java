package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.config.Config;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Function;

public enum DataSourceType {

    SAMPLE(SampleDataLoader::new),
    USER(UserDataLoader::new),
    FILE(parser -> new JsonFileDataLoader(parser,
            Path.of(Config.get("com.bachelor.application.input.file"))));

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