package com.bachelor.data;

import com.bachelor.InputParser;
import com.bachelor.data.json.InputError;
import com.bachelor.data.json.InputPair;
import com.bachelor.data.json.LoadResult;
import com.bachelor.exceptions.DataLoadException;
import com.bachelor.exceptions.InvalidInputException;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.InputArrayFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonFileDataLoader implements DataLoader {

    private static final String SUBJECT = "subject";
    private static final String PATTERN = "pattern";

    private final ObjectMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    private final InputParser parser;
    private final Path file;

    public JsonFileDataLoader(InputParser parser, Path file) {
        this.parser = Objects.requireNonNull(parser);
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public LoadResult load() throws DataLoadException {
        JsonNode root = readJson();

        if (!root.isArray()) {
            throw new DataLoadException("Root element must be a JSON array [...], but was "
                    + describe(root) + ": " + file.toAbsolutePath());
        }

        List<InputPair> pairs = new ArrayList<>();
        List<InputError> errors = new ArrayList<>();

        int entryNumber = 0;
        for (JsonNode entry : root) {
            entryNumber++;
            processEntry(entryNumber, entry, pairs, errors);
        }

        return new LoadResult(pairs, errors);
    }

    private JsonNode readJson() throws DataLoadException {
        if (!Files.isRegularFile(file)) {
            throw new DataLoadException("File not found: " + file.toAbsolutePath());
        }

        JsonNode root;
        try {
            root = mapper.readTree(file.toFile());
        } catch (JsonProcessingException e) {
            throw new DataLoadException("Invalid JSON in " + file.toAbsolutePath()
                    + position(e.getLocation()) + ": " + e.getOriginalMessage(), e);
        } catch (IOException e) {
            throw new DataLoadException("Could not read " + file.toAbsolutePath() + ": " + e.getMessage(), e);
        }

        if (root == null || root.isMissingNode()) {
            throw new DataLoadException("File is empty: " + file.toAbsolutePath());
        }
        return root;
    }

    private void processEntry(int entryNumber, JsonNode entry,
                              List<InputPair> pairs, List<InputError> errors) {
        if (!entry.isObject()) {
            errors.add(new InputError(entryNumber,
                    "Entry must be an object with '" + SUBJECT + "' and '" + PATTERN
                            + "', but was " + describe(entry)));
            return;
        }

        List<String> problems = new ArrayList<>();

        String subjectText = readString(entry, SUBJECT, problems);
        String patternText = readString(entry, PATTERN, problems);

        InputArray subject = (subjectText != null) ? parseTree(SUBJECT, subjectText, problems) : null;
        InputArray pattern = (patternText != null) ? parseTree(PATTERN, patternText, problems) : null;

        if (problems.isEmpty()) {
            pairs.add(new InputPair(entryNumber, subjectText, patternText, subject, pattern));
        } else {
            problems.forEach(p -> errors.add(new InputError(entryNumber, p)));
        }
    }

    /** Returns the field's text, or null (and records a problem) if it is missing or not a string. */
    private static String readString(JsonNode entry, String field, List<String> problems) {
        JsonNode value = entry.get(field);

        if (value == null) {
            problems.add("Missing field '" + field + "'");
            return null;
        }
        if (!value.isTextual()) {
            problems.add("Field '" + field + "' must be a string, but was " + describe(value));
            return null;
        }
        return value.asText();
    }

    /** Runs the tree parser. Returns null (and records a problem) if the string is not a valid tree. */
    private InputArray parseTree(String field, String text, List<String> problems) {
        try {
            return InputArrayFactory.create(parser.parse(text));
        } catch (InvalidInputException e) {
            problems.add("Field '" + field + "' is not a valid tree: " + e.getMessage());
            return null;
        }
    }

    private static String position(JsonLocation location) {
        return location == null ? ""
                : " at line " + location.getLineNr() + ", column " + location.getColumnNr();
    }

    private static String describe(JsonNode node) {
        return node.isNull() ? "null" : "a " + node.getNodeType().name().toLowerCase();
    }
}