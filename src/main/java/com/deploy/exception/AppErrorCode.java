package com.deploy.exception;

import lombok.Getter;

@Getter
public enum AppErrorCode {
    NOT_FOUND_ENTITY_IN_JOB("400", "No such data in Job"),
    NOT_FOUND_ENTITY_IN_STEP("400", "No such data in Step"),
    NOT_FOUND_ENTITY_IN_CREDENTIAL("400", "No such data in Credential"),
    NOT_FOUND_ENTITY_IN_SCMCONFIG("400", "No such data in ScmConfig"),
    NOT_FOUND_ENTITY_IN_USER("400", "No such data in User")
    ;

    private final String code;
    private final String message;

    AppErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}