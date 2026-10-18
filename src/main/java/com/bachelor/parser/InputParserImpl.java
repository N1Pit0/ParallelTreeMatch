package com.bachelor.parser;

import com.bachelor.InputParser;
import com.bachelor.exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * term := VARIABLE                              A-Z, never has arguments
 *       | CONSTANT                              a-z without arguments
 *       | FUNCTION "(" term ("," term)* ")"     a-z with at least one argument
 * </pre>
 * Each symbol is exactly one letter. Whitespace between symbols is ignored.
 */
public class InputParserImpl implements InputParser {

    @Override
    public Term parse(String input) throws InvalidInputException {
        if (input == null || input.isBlank()) {
            throw new InvalidInputException("Input cannot be null or empty");
        }
        TermReader reader = new TermReader(input);
        Term term = reader.parseTerm();
        reader.expectEnd();
        return term;
    }

    /** State of a single parse run, so InputParserImpl stays stateless and thread-safe. */
    private static final class TermReader {

        private static final int END = -1;

        private final String input;
        private int pos = 0;

        TermReader(String input) {
            this.input = input;
        }

        Term parseTerm() throws InvalidInputException {
            int c = peek();
            if (c == END) {
                throw error("Unexpected end of input, expected a letter");
            }

            char symbol = (char) c;

            if (isUpper(symbol)) {
                pos++;
                if (peek() == '(') {
                    throw error("Variable '" + symbol + "' cannot have arguments");
                }
                return Term.leaf(symbol);
            }

            if (isLower(symbol)) {
                pos++;
                return (peek() == '(') ? parseArguments(symbol) : Term.leaf(symbol);
            }

            throw error("Expected a letter but found '" + symbol + "'");
        }

        /** "(" term ("," term)* ")" — called when the next character is "(". */
        private Term parseArguments(char symbol) throws InvalidInputException {
            pos++; // consume '('

            if (peek() == ')') {
                throw error("Function '" + symbol + "' cannot have an empty argument list");
            }

            List<Term> arguments = new ArrayList<>();
            arguments.add(parseTerm());

            while (peek() == ',') {
                pos++; // consume ','
                arguments.add(parseTerm());
            }

            int c = peek();
            if (c == END) {
                throw error("Missing closing parenthesis for '" + symbol + "('");
            }
            if (c != ')') {
                throw error("Expected ',' or ')' but found '" + (char) c + "'");
            }
            pos++; // consume ')'

            return new Term(symbol, arguments);
        }

        void expectEnd() throws InvalidInputException {
            int c = peek();
            if (c != END) {
                throw error("Unexpected '" + (char) c + "' after the end of the expression");
            }
        }

        private int peek() {
            while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
                pos++;
            }
            return pos < input.length() ? input.charAt(pos) : END;
        }

        private InvalidInputException error(String message) {
            return new InvalidInputException(message + " at position " + (pos + 1) + ":\n"
                    + "  " + input + "\n"
                    + "  " + " ".repeat(pos) + "^");
        }

        private static boolean isUpper(char c) { return c >= 'A' && c <= 'Z'; }
        private static boolean isLower(char c) { return c >= 'a' && c <= 'z'; }
    }
}