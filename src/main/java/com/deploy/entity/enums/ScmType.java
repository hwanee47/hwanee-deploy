package com.deploy.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum ScmType {
    SVN, GIT;

    @JsonCreator
    public static ScmType parsing(String inputValue) {
        return Stream.of(ScmType.values())
                .filter(type -> type.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
