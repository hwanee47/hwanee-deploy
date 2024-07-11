package com.deploy.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum NotificationType {
    SLACK, MAIL;

    @JsonCreator
    public static NotificationType parsing(String inputValue) {
        NotificationType notificationType = Stream.of(NotificationType.values())
                .filter(type -> type.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
        return notificationType;
    }
}
