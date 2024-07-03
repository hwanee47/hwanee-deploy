package com.deploy.exception;

import org.eclipse.jgit.api.errors.GitAPIException;

public class NotFoundGitBranchException extends GitAPIException {

    public NotFoundGitBranchException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundGitBranchException(String message) {
        super(message);
    }
}
