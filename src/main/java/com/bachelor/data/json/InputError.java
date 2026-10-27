package com.bachelor.data.json;

/** A problem with a single entry. entryNumber is 1-based. */
public record InputError(int entryNumber, String message) {

    @Override
    public String toString() {
        return "Entry " + entryNumber + ": " + message;
    }
}