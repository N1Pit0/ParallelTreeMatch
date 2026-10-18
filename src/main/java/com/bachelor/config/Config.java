package com.bachelor.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public final class Config {

    private static final String BASE_FILE = "application.properties";
    private static final String PROFILE_KEY = "com.bachelor.application.profile";
    private static final String ENVIRONMENT_VARIABLE_PROFILE_KEY = "APP_PROFILE";
    private static final String ALLOWED_PROFILES_KEY = "com.bachelor.application.profiles";
    private static final String MATCHING_ALGORITHM_KEY = "com.bachelor.application.matchingalgorithm.type";
    private static final String ENVIRONMENT_VARIABLE_MATCHING_ALGORITHM_KEY = "APP_MATCHING_ALGORITHM";
    private static final String ALLOWED_MATCHING_ALGORITHMS = "com.bachelor.application.matchingalgorithm.types";

    private static final Properties PROPS = new Properties();
    private static final String ACTIVE_PROFILE;
    private static final String MATCHING_ALGORITHM;

    static {
        loadInto(BASE_FILE, true);

        //Check order: System values -> Env values -> properties file values
        String profile = System.getProperty(PROFILE_KEY);
        if (profile == null) profile = System.getenv(ENVIRONMENT_VARIABLE_PROFILE_KEY);
        if (profile == null) profile = PROPS.getProperty(PROFILE_KEY, "dev");
        profile = profile.trim();

        List<String> allowedProfiles = split(PROPS.getProperty(ALLOWED_PROFILES_KEY, "dev,test,prod"));
        if (!allowedProfiles.contains(profile)) {
            throw new IllegalStateException(
                    "Unknown profile '" + profile + "'. Allowed: " + allowedProfiles);
        }

        loadInto("application-" + profile + ".properties", false);

        ACTIVE_PROFILE = profile;

        //Check order: System values -> Env values -> properties file values
        String algorithm = System.getProperty(MATCHING_ALGORITHM_KEY);
        if (algorithm == null) algorithm = System.getenv(ENVIRONMENT_VARIABLE_MATCHING_ALGORITHM_KEY);
        if (algorithm == null) algorithm = PROPS.getProperty(MATCHING_ALGORITHM_KEY, "term");
        algorithm = algorithm.trim();

        List<String> allowedMatchingAlgorithms = split(PROPS.getProperty(ALLOWED_MATCHING_ALGORITHMS, "term,sequential"));
        if (!allowedMatchingAlgorithms.contains(algorithm)) {
            throw new IllegalStateException(
                    "Unknown algorithm '" + algorithm + "'. Allowed: " + allowedMatchingAlgorithms);
        }

        MATCHING_ALGORITHM = algorithm;
    }

    private Config() {}

    private static void loadInto(String fileName, boolean required) {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                if (required) throw new IllegalStateException(fileName + " not found on classpath");
                return;
            }
            Config.PROPS.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + fileName, e);
        }
    }

    public static String getActiveProfile() { return ACTIVE_PROFILE; }
    public static boolean isDev()  { return "dev".equals(ACTIVE_PROFILE); }
    public static boolean isTest() { return "test".equals(ACTIVE_PROFILE); }
    public static boolean isProd() { return "prod".equals(ACTIVE_PROFILE); }

    public static String getMatchingAlgorithm() { return MATCHING_ALGORITHM; }

    private static String raw(String key) {
        String value = System.getProperty(key, PROPS.getProperty(key));
        return value == null ? null : value.trim();
    }

    public static String get(String key) {
        String value = raw(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required property: " + key + " (profile: " + ACTIVE_PROFILE + ")");
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        String value = raw(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static int getInt(String key, int defaultValue) {
        String value = raw(key);
        return (value == null || value.isEmpty()) ? defaultValue : Integer.parseInt(value);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = raw(key);
        return (value == null || value.isEmpty()) ? defaultValue : Boolean.parseBoolean(value);
    }

    public static List<String> getList(String key) {
        String value = raw(key);
        return (value == null || value.isEmpty()) ? List.of() : split(value);
    }

    private static List<String> split(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}