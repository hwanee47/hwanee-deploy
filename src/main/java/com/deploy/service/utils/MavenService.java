package com.deploy.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Slf4j
@Service
public class MavenService implements BuildService{

    private Logger logger = LoggerFactory.getLogger(MavenService.class);

    public void setHistoryLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String executeBuild(String projectPath) throws Exception {
        return null;
    }
}
