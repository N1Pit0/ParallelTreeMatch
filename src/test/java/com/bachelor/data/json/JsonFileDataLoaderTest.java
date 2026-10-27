package com.bachelor.data.json;

import com.bachelor.InputParser;
import com.bachelor.data.*;
import com.bachelor.exceptions.DataLoadException;
import com.bachelor.parser.InputParserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileDataLoaderTest {

    @TempDir
    Path tempDir;

    private final InputParser parser = new InputParserImpl();


    /** Writes the given content to a temporary file and returns its path. */
    private Path write(String content) throws IOException {
        Path file = tempDir.resolve("input.json");
        Files.writeString(file, content);
        return file;
    }

    /** Writes the JSON to a temporary file and loads it. */
    private LoadResult load(String json) throws IOException, DataLoadException {
        return new JsonFileDataLoader(parser, write(json)).load();
    }

    /** Writes the content to a temporary file and asserts that loading fails as a whole. */
    private DataLoadException loadExpectingFailure(String content) throws IOException {
        Path file = write(content);
        return assertThrows(DataLoadException.class,
                () -> new JsonFileDataLoader(parser, file).load());
    }

    @Nested
    @DisplayName("Valid input")
    class ValidInput {

        @Test
        void loadsAllEntries() throws Exception {
            LoadResult result = load("""
                    [
                      { "subject": "f(f(a,b), f(f(a,a),a))",          "pattern": "f(f(a,A), B)" },
                      { "subject": "f(a,f(a,b,c),f(a,f(a,b,c),e),f)", "pattern": "f(A,f(a,B,C),D)" }
                    ]
                    """);

            assertFalse(result.hasErrors());
            assertEquals(2, result.pairs().size());
            assertEquals(0, result.skippedEntryCount());
        }

        @Test
        void keepsOriginalTextAndParsedTrees() throws Exception {
            LoadResult result = load("""
                    [ { "subject": "f(a, b)", "pattern": "f(A, b)" } ]
                    """);

            InputPair pair = result.pairs().getFirst();
            assertAll(
                    () -> assertEquals(1, pair.entryNumber()),
                    () -> assertEquals("f(a, b)", pair.subjectText()),
                    () -> assertEquals("f(A, b)", pair.patternText()),
                    () -> assertNotNull(pair.subject()),
                    () -> assertNotNull(pair.pattern())
            );
        }

        @Test
        void entryNumbersFollowFileOrder() throws Exception {
            LoadResult result = load("""
                    [
                      { "subject": "a", "pattern": "a" },
                      { "subject": "b", "pattern": "b" },
                      { "subject": "c", "pattern": "c" }
                    ]
                    """);

            assertEquals(List.of(1, 2, 3),
                    result.pairs().stream().map(InputPair::entryNumber).toList());
            assertEquals(List.of("a", "b", "c"),
                    result.pairs().stream().map(InputPair::subjectText).toList());
        }

        @Test
        void ignoresAdditionalFields() throws Exception {
            LoadResult result = load("""
                    [ { "id": 7, "comment": "extra fields are fine", "subject": "a", "pattern": "A" } ]
                    """);

            assertFalse(result.hasErrors());
            assertEquals(1, result.pairs().size());
        }

        @Test
        void acceptsEmptyArray() throws Exception {
            LoadResult result = load("[]");

            assertTrue(result.pairs().isEmpty());
            assertFalse(result.hasErrors());
        }

        @Test
        void acceptsWhitespaceAroundJson() throws Exception {
            LoadResult result = load("""
                       [ { "subject": "a", "pattern": "a" } ]
                    """);

            assertEquals(1, result.pairs().size());
        }
    }

    @Nested
    @DisplayName("Entry-level problems")
    class EntryLevelProblems {

        @Test
        void invalidEntryDoesNotAffectOthers() throws Exception {
            LoadResult result = load("""
                    [
                      { "subject": "f(a,b)",  "pattern": "f(A,b)" },
                      { "subject": "f(a,,b)", "pattern": "f(A,b)" },
                      { "subject": "g(a)",    "pattern": "g(X)" }
                    ]
                    """);

            assertEquals(List.of(1, 3),
                    result.pairs().stream().map(InputPair::entryNumber).toList());

            assertEquals(1, result.errors().size());
            InputError error = result.errors().getFirst();
            assertEquals(2, error.entryNumber());
            assertTrue(error.message().startsWith("Field 'subject' is not a valid tree"),
                    error.message());
        }

        @Test
        void invalidPatternIsReported() throws Exception {
            LoadResult result = load("""
                    [ { "subject": "f(a,b)", "pattern": "A(b)" } ]
                    """);

            assertTrue(result.pairs().isEmpty());
            assertTrue(result.errors().getFirst().message().startsWith("Field 'pattern' is not a valid tree"));
            assertTrue(result.errors().getFirst().message().contains("cannot have arguments"));
        }

        @Test
        void reportsAllProblemsOfOneEntry() throws Exception {
            LoadResult result = load("""
                    [ { "subject": "f(a,,b)", "pattern": "A(b)" } ]
                    """);

            assertTrue(result.pairs().isEmpty());
            assertEquals(2, result.errors().size(), "both fields should be reported");
            assertEquals(1, result.skippedEntryCount(), "but it is still only one skipped entry");

            assertTrue(result.errors().get(0).message().contains("'subject'"));
            assertTrue(result.errors().get(1).message().contains("'pattern'"));
        }

        @ParameterizedTest(name = "missing {0}")
        @CsvSource({
                "subject, '{ \"pattern\": \"a\" }'",
                "pattern, '{ \"subject\": \"a\" }'"
        })
        void reportsMissingField(String field, String entry) throws Exception {
            LoadResult result = load("[ " + entry + " ]");

            assertTrue(result.pairs().isEmpty());
            assertEquals("Missing field '" + field + "'", result.errors().getFirst().message());
        }

        @Test
        void reportsBothMissingFields() throws Exception {
            LoadResult result = load("[ {} ]");

            assertEquals(2, result.errors().size());
            assertEquals("Missing field 'subject'", result.errors().get(0).message());
            assertEquals("Missing field 'pattern'", result.errors().get(1).message());
        }

        @ParameterizedTest(name = "subject = {0}")
        @CsvSource(delimiter = '|', value = {
                "42           | a number",
                "true         | a boolean",
                "null         | null",
                "[\"a\"]      | a array",
                "{\"x\": 1}   | a object"
        })
        void reportsWrongFieldType(String jsonValue, String expectedDescription) throws Exception {
            LoadResult result = load("[ { \"subject\": " + jsonValue + ", \"pattern\": \"a\" } ]");

            assertTrue(result.pairs().isEmpty());
            assertEquals("Field 'subject' must be a string, but was " + expectedDescription,
                    result.errors().getFirst().message());
        }

        @ParameterizedTest(name = "entry = {0}")
        @ValueSource(strings = { "\"a string\"", "42", "null", "[1, 2]", "true" })
        void reportsEntryThatIsNotAnObject(String entry) throws Exception {
            LoadResult result = load("[ " + entry + " ]");

            assertTrue(result.pairs().isEmpty());
            assertEquals(1, result.errors().size());
            assertTrue(result.errors().getFirst().message().startsWith("Entry must be an object"),
                    result.errors().getFirst().message());
        }

        @Test
        void reportsEmptyTreeString() throws Exception {
            LoadResult result = load("""
                    [ { "subject": "", "pattern": "   " } ]
                    """);

            assertTrue(result.pairs().isEmpty());
            assertEquals(2, result.errors().size());
        }

        @Test
        void mixedFileLoadsOnlyValidEntries() throws Exception {
            LoadResult result = load("""
                    [
                      { "subject": "f(a,b)",  "pattern": "f(A,b)" },
                      { "subject": "f(a,,b)", "pattern": "f(A)" },
                      { "subject": "f(a,b)" },
                      { "subject": 42,        "pattern": "A(b)" },
                      "not an object",
                      { "subject": "g(a)",    "pattern": "g(X)" }
                    ]
                    """);

            assertEquals(List.of(1, 6),
                    result.pairs().stream().map(InputPair::entryNumber).toList());
            assertEquals(5, result.errors().size());   // entry 4 has two errors
            assertEquals(4, result.skippedEntryCount());
            assertEquals(List.of(2, 3, 4, 4, 5),
                    result.errors().stream().map(InputError::entryNumber).toList());
        }

        @Test
        void errorToStringContainsEntryNumber() throws Exception {
            LoadResult result = load("""
                    [ { "subject": "a", "pattern": "a" }, { "subject": "a" } ]
                    """);

            assertEquals("Entry 2: Missing field 'pattern'", result.errors().getFirst().toString());
        }
    }

    @Nested
    @DisplayName("File-level problems")
    class FileLevelProblems {

        @Test
        void rejectsInvalidJsonAndReportsLine() throws IOException {
            DataLoadException e = loadExpectingFailure("""
                    [
                      { "subject": "a", "pattern": "a" },
                      { "subject": "b"  "pattern": "b" }
                    ]
                    """);

            assertTrue(e.getMessage().startsWith("Invalid JSON"), e.getMessage());
            assertTrue(e.getMessage().contains("line 3"), e.getMessage());
            assertNotNull(e.getCause(), "original Jackson exception should be kept as cause");
        }

        @Test
        void rejectsUnclosedArray() throws IOException {
            DataLoadException e = loadExpectingFailure("""
                    [ { "subject": "a", "pattern": "a" }
                    """);

            assertTrue(e.getMessage().startsWith("Invalid JSON"), e.getMessage());
        }

        @Test
        void rejectsContentAfterArray() throws IOException {
            DataLoadException e = loadExpectingFailure("""
                    [ { "subject": "a", "pattern": "a" } ] extra
                    """);

            assertTrue(e.getMessage().startsWith("Invalid JSON"), e.getMessage());
        }

        @Test
        void rejectsSecondRootValue() throws IOException {
            loadExpectingFailure("""
                    [ { "subject": "a", "pattern": "a" } ]
                    [ { "subject": "b", "pattern": "b" } ]
                    """);
        }

        @ParameterizedTest(name = "root = {0}")
        @ValueSource(strings = {
                "{ \"subject\": \"a\", \"pattern\": \"a\" }",
                "\"just a string\"",
                "42",
                "null"
        })
        void rejectsNonArrayRoot(String json) throws IOException {
            DataLoadException e = loadExpectingFailure(json);

            assertTrue(e.getMessage().contains("must be a JSON array"), e.getMessage());
        }

        @ParameterizedTest(name = "content = \"{0}\"")
        @ValueSource(strings = { "", "   ", "\n\n" })
        void rejectsEmptyFile(String content) throws IOException {
            DataLoadException e = loadExpectingFailure(content);

            assertTrue(e.getMessage().startsWith("File is empty"), e.getMessage());
        }

        @Test
        void rejectsMissingFile() {
            Path missing = tempDir.resolve("does-not-exist.json");

            DataLoadException e = assertThrows(DataLoadException.class,
                    () -> new JsonFileDataLoader(parser, missing).load());

            assertTrue(e.getMessage().startsWith("File not found"), e.getMessage());
            assertTrue(e.getMessage().contains("does-not-exist.json"), e.getMessage());
        }

        @Test
        void rejectsDirectoryInsteadOfFile() {
            DataLoadException e = assertThrows(DataLoadException.class,
                    () -> new JsonFileDataLoader(parser, tempDir).load());

            assertTrue(e.getMessage().startsWith("File not found"), e.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        void rejectsNullParser() {
            assertThrows(NullPointerException.class,
                    () -> new JsonFileDataLoader(null, tempDir.resolve("x.json")));
        }

        @Test
        void rejectsNullFile() {
            assertThrows(NullPointerException.class,
                    () -> new JsonFileDataLoader(parser, null));
        }
    }
}