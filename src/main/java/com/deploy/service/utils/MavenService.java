package com.deploy.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Slf4j
@Service
public class MavenService implements BuildService{

    @Override
    public void executeBuild(String projectPath) throws Exception {


    }
}
