package com.deploy.service.utils;

public interface BuildService {

    String executeBuild(String projectPath, boolean isTest) throws Exception;

}
