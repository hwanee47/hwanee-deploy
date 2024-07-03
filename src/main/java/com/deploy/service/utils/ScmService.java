package com.deploy.service.utils;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface ScmService {

    String getType();

    boolean isConnected(String url, String username, String password) throws GitAPIException;

    boolean isConnected(String url, String username, String password, String branch) throws GitAPIException;

    void cloneProject(String url, String branch, String username, String password, String clonePath) throws GitAPIException, IOException;

}
