package com.bachelor.data.json;

import java.util.List;

public record LoadResult(List<InputPair> pairs, List<InputError> errors) {

    public LoadResult {
        pairs = List.copyOf(pairs);
        errors = List.copyOf(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /** Number of skipped entries (one entry can have several errors). */
    public long skippedEntryCount() {
        return errors.stream().mapToInt(InputError::entryNumber).distinct().count();
    }
}