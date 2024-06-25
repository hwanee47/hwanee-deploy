package com.deploy.service;

import org.eclipse.jgit.api.errors.GitAPIException;

public interface ScmService {

    String getType();

    boolean isConnected(String url, String branch, String username, String password) throws GitAPIException;
}
