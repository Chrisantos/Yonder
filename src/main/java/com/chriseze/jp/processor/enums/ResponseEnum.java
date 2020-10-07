package com.chriseze.jp.processor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS("Request was successful", 0),
    ERROR("Error occurred processing your request", -1),
    INVALID_REQUEST("Invalid request", -2),
    NO_USER("No such user with the provided email address", -3),
    ;

    private String description;
    private int code;
}
