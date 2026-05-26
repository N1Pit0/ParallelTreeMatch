package com.bachelor.parser.dto;

import com.bachelor.preprocess.Variable;

public record NodeDto(String name, Variable variable, int edgeLabel, int father, int outdegree){}

