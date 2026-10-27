package com.bachelor.parser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A parsed term such as f(a, B). Pure syntax: no algorithm-specific data.
 *   variable : uppercase symbol, no arguments      (e.g. A)
 *   constant : lowercase symbol, no arguments      (e.g. a)
 *   function : lowercase symbol, >= 1 argument     (e.g. f(a, B))
 */
public record Term(char symbol, List<Term> arguments) {

    public Term {
        arguments = List.copyOf(arguments); // immutable
    }

    public static Term leaf(char symbol) {
        return new Term(symbol, List.of());
    }

    public boolean isVariable() { return symbol >= 'A' && symbol <= 'Z'; }
    public boolean isFunction() { return !arguments.isEmpty(); }
    public boolean isConstant() { return !isVariable() && !isFunction(); }
    public int arity()          { return arguments.size(); }

    /** Normalized form without spaces, e.g. "f(a,B)". */
    @Override
    public String toString() {
        if (arguments.isEmpty()) return String.valueOf(symbol);
        return symbol + arguments.stream()
                .map(Term::toString)
                .collect(Collectors.joining(",", "(", ")"));
    }
}