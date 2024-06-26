package com.deploy.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum StepType {
    SCM, BUILD, COMMAND;


    @JsonCreator
    public static StepType parsing(String inputValue) {
        return Stream.of(StepType.values())
                .filter(type -> type.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
