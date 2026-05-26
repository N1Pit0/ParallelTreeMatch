package com.bachelor.parser;

import com.bachelor.preprocess.Variable;

public record NodeDto(String name, Variable variable, int edgeLabel, int father, int outdegree){}

