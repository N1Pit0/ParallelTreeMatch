package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.config.Config;

public final class DataLoaderFactory {
    private DataLoaderFactory() {}

    public static DataLoader create(InputParser parser) {
        DataSourceType type = DataSourceType.from(Config.get("com.bachelor.application.datasource"));

        if (Config.isProd() && type == DataSourceType.SAMPLE) {
            throw new IllegalStateException("Sample data is not allowed in the prod profile");
        }

        return type.createLoader(parser);
    }
}
