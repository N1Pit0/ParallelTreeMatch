package com.bachelor.data.json;

import java.util.List;

public record LoadResult(List<InputPair> pairs, List<InputError> errors) {

    public LoadResult {
        pairs = List.copyOf(pairs);
        errors = List.copyOf(errors);
    }

    /** For loaders that never produce entry errors. */
    public static LoadResult of(List<InputPair> pairs) {
        return new LoadResult(pairs, List.of());
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public long skippedEntryCount() {
        return errors.stream().mapToInt(InputError::entryNumber).distinct().count();
    }
}