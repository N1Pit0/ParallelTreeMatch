package com.bachelor.data;

import java.util.Arrays;
import java.util.function.Supplier;

public enum DataSourceType {

    SAMPLE(SampleDataLoader::new),
    USER(UserDataLoader::new);

    private final Supplier<DataLoader> creator;

    DataSourceType(Supplier<DataLoader> creator) {
        this.creator = creator;
    }

    public DataLoader createLoader() {
        return creator.get();
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