package com.deploy.service;

import com.deploy.dto.request.StepCreateReq;
import com.deploy.dto.request.StepUpdateReq;
import com.deploy.entity.Credential;
import com.deploy.entity.Job;
import com.deploy.entity.ScmConfig;
import com.deploy.entity.Step;
import com.deploy.entity.embed.BuildSet;
import com.deploy.entity.enums.BuildType;
import com.deploy.entity.enums.StepType;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
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


    /**
     * Step 추가
     * @param stepCreateReq
     * @return
     */
    @Transactional
    public Long createStep(StepCreateReq stepCreateReq) {

        // 유효성 검사
        validation(stepCreateReq.getType(), stepCreateReq.getBuildType(), stepCreateReq.getCredentialId(), stepCreateReq.getScmConfigId());

        // 엔티티 조회
        Job job = jobRepository.findById(stepCreateReq.getJobId())
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

        Credential credential = null;
        ScmConfig scmConfig = null;

        if (stepCreateReq.getCredentialId() != null) {
            credential = credentialRepository.findById(stepCreateReq.getCredentialId())
                    .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_CREDENTIAL));
        }

        if (stepCreateReq.getScmConfigId() != null) {
            scmConfig = scmConfigRepository.findById(stepCreateReq.getScmConfigId())
                    .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_SCMCONFIG));
        }


        // maxStepIndex 조회
        Long maxStepIndex = stepRepository.maxStepIndex(job.getId());

        // 엔티티 생성
        Step step = Step.createStep(
                maxStepIndex+1,
                stepCreateReq.getType(),
                new BuildSet(stepCreateReq.getBuildType(), null),
                stepCreateReq.getCommand(),
                job,
                credential,
                scmConfig);

        // 엔티티 저장
        stepRepository.save(step);

        return step.getId();
    }


    /**
     * Step 수정
     * @param request
     * @return
     */
    @Transactional
    public Long updateStep(Long id, StepUpdateReq request) {

        // 엔티티 조회
        Step findStep = stepRepository.findById(id)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_STEP));

        Credential credential = null;
        ScmConfig scmConfig = null;

        if (request.getCredentialId() != null) {
            credential = credentialRepository.findById(request.getCredentialId())
                    .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_CREDENTIAL));
        }

        if (request.getScmConfigId() != null) {
            scmConfig = scmConfigRepository.findById(request.getScmConfigId())
                    .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_SCMCONFIG));
        }


        // update
        findStep.changeInfo(
                request.getType(),
                new BuildSet(request.getBuildType(), null),
                credential,
                scmConfig
        );

        return id;

    }

    // 유효성 검사
    private void validation(StepType type, BuildType buildType, Long credentialId, Long scmConfigId) {

        if (type.equals(StepType.SCM)) {
            if (scmConfigId == null) {
                throw new AppBizException("유형이 'SCM'의 경우 SCM Config값은 필수입니다.");
            }
        }

        if (type.equals(StepType.DEPLOY)) {
            if (credentialId == null) {
                throw new AppBizException("유형이 'DEPLOY'의 경우 Credential값은 필수입니다.");
            }
        }

        if (type.equals(StepType.BUILD)) {
            if (buildType == null) {
                throw new AppBizException("유형이 'BUILD'의 경우 BuildType값은 필수입니다.");
            }
        }
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
