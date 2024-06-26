package com.deploy.service;

import com.deploy.repository.StepRepository;
import com.deploy.service.utils.BuildService;
import com.deploy.service.utils.ScmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;


    /**
     * 클론 프로젝트
     */
    public void cloneProject(ScmService service, String url, String branch, String username, String password, String clonePath) {

        try {
            service.cloneProject(url, branch, username, password, clonePath);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 빌드 프로젝트
     */
    public void executeBuild(BuildService service, String projectPath) {
        try {
            service.executeBuild(projectPath);
        } catch (Exception e) {

        }
    }



    public void deployToRemote() {

    }



}
