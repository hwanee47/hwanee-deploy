package com.deploy.service;

import com.deploy.dto.request.StepCreateReq;
import com.deploy.entity.Credential;
import com.deploy.entity.Job;
import com.deploy.entity.ScmConfig;
import com.deploy.entity.Step;
import com.deploy.repository.CredentialRepository;
import com.deploy.repository.JobRepository;
import com.deploy.repository.ScmConfigRepository;
import com.deploy.repository.StepRepository;
import com.deploy.service.utils.BuildService;
import com.deploy.service.utils.ScmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StepService {

    private final JobRepository jobRepository;
    private final StepRepository stepRepository;
    private final CredentialRepository credentialRepository;
    private final ScmConfigRepository scmConfigRepository;


    @Transactional
    public Long createStep(StepCreateReq stepCreateReq) {

        // 엔티티 조회
        Job job = jobRepository.findById(stepCreateReq.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("No such Job data."));

        Credential credential = null;
        ScmConfig scmConfig = null;

        if (stepCreateReq.getCredentialId() != null) {
            credential = credentialRepository.findById(stepCreateReq.getCredentialId())
                    .orElse(null);
        }

        if (stepCreateReq.getScmConfigId() != null) {
            scmConfig = scmConfigRepository.findById(stepCreateReq.getScmConfigId())
                    .orElse(null);
        }


        // maxStepIndex 조회
        Long maxStepIndex = stepRepository.maxStepIndex(job.getId());

        // 엔티티 생성
        Step step = Step.createStep(maxStepIndex+1, stepCreateReq.getType(), stepCreateReq.getCommand(), job, credential, scmConfig);

        // 엔티티 저장
        stepRepository.save(step);

        return step.getId();
    }




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
