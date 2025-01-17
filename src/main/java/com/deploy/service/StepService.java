package com.deploy.service;

import com.deploy.dto.request.StepCreateReq;
import com.deploy.dto.request.StepToggleReq;
import com.deploy.dto.request.StepUpdateReq;
import com.deploy.entity.*;
import com.deploy.entity.embed.BuildSet;
import com.deploy.entity.enums.BuildType;
import com.deploy.entity.enums.StepType;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.*;
import com.deploy.service.event.DeployCompletedEvent;
import com.deploy.service.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StepService {
    private Logger logger = LoggerFactory.getLogger(StepService.class);

    private final JobRepository jobRepository;
    private final StepRepository stepRepository;
    private final CredentialRepository credentialRepository;
    private final ScmConfigRepository scmConfigRepository;
    private final ScmServiceFactory scmServiceFactory;
    private final BuildServiceFactory buildServiceFactory;
    private final BuildFileRepository buildFileRepository;
    private final RemoteService remoteService;
    private final AesService aesService;
    private final BuildFileService buildFileService;
    private final ApplicationEventPublisher eventPublisher;

    private final String DEFAULT_CLONE_PATH = System.getProperty("user.home") + "/deployApp/source_code/";


    public void setHistoryLogger(Logger logger) {
        this.logger = logger;
        this.scmServiceFactory.setHistoryLogger(logger);
        this.buildServiceFactory.setHistoryLogger(logger);
        this.remoteService.setHistoryLogger(logger);
    }

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
                new BuildSet(stepCreateReq.getBuildType(), null, stepCreateReq.isTest()),
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
                new BuildSet(request.getBuildType(), null, request.isTest()),
                credential,
                scmConfig,
                request.getCommand()
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
     * Step type 별 역할수행
     * @param step
     */
    @Transactional
    public String executeStep(Step step, String prevResult) throws Exception {

        StepType stepType = step.getStepType();
        String result = null;

        switch (stepType) {
            case SCM:
                String clonePath = DEFAULT_CLONE_PATH + step.getJob().getName();
                result = executeSCM(step, clonePath);

                break;
            case BUILD:
                String projectPath = prevResult;
                result = executeBuild(step, projectPath);

                // 빌드파일 엔티티 저장
                buildFileService.createBuildFile(step.getJob(), result);

                break;
            case DEPLOY:
                String sourcePath = prevResult;
                String targetPath = "/home/ec2-user";
                String targetFileName = "my-app.jar";
                result = executeDeploy(step, sourcePath, targetPath, targetFileName);

                break;
            case COMMAND:
                result = executeCommand(step);

                break;
        }

        return result;
    }

    /**
     * 배포 후 커맨드 실행
     * @param step
     * @return
     * @throws Exception
     */
    private String executeCommand(Step step) throws Exception {
        // Credential 조회
        Credential credential = step.getCredential();
        String targetHost = credential.getTargetHost();
        Integer targetPort = credential.getTargetPort();
        String targetUsername = credential.getTargetUsername();
        String targetPassword = aesService.decrypt(credential.getTargetPassword());
        String privateKey = aesService.decrypt(credential.getPrivateKey());

        String command = step.getCommand();

        // 초기화
        remoteService.init(targetHost, targetPort, targetUsername, targetPassword, privateKey);

        return remoteService.executeScript(command);
    }

    /**
     * 배포 수행
     * @param step
     * @param sourcePath
     * @param targetPath
     * @param targetFileName
     * @throws Exception
     */
    private String executeDeploy(Step step, String sourcePath, String targetPath, String targetFileName) throws Exception {

        // validation
        validationParam(sourcePath, targetPath);

        // Credential 조회
        Credential credential = step.getCredential();
        String targetHost = credential.getTargetHost();
        Integer targetPort = credential.getTargetPort();
        String targetUsername = credential.getTargetUsername();
        String targetPassword = aesService.decrypt(credential.getTargetPassword());
        String privateKey = aesService.decrypt(credential.getPrivateKey());

        // 초기화
        remoteService.init(targetHost, targetPort, targetUsername, targetPassword, privateKey);

        File uploadFile = new File(sourcePath);

        // 업로드
        if (!StringUtils.hasText(targetFileName))
            targetFileName = "app.jar";

        boolean upload = remoteService.upload(uploadFile, targetPath, targetFileName);

        return upload ? targetPath : "";
    }


    /**
     * 빌드 수행
     * @param step
     * @param projectPath
     * @return
     * @throws Exception
     */
    private String executeBuild(Step step, String projectPath) throws Exception {

        // validation
        validationParam(projectPath);

        String buildFile;
        BuildService buildService = buildServiceFactory.getBuildService(step.getBuildSet().getBuildType());

        // 빌드 프로젝트
        buildFile = buildService.executeBuild(projectPath, step.getBuildSet().isBuildTest());
        return buildFile;
    }


    /**
     * 소스코드 관리
     * @param step
     * @param clonePath
     * @return
     * @throws Exception
     */
    private String executeSCM(Step step, String clonePath) throws Exception {

        // SCM config 조회
        ScmConfig scmConfig = step.getScmConfig();

        ScmService scmService = scmServiceFactory.getScmService(scmConfig.getScmType());
        String url = scmConfig.getUrl();
        String branch = scmConfig.getBranch();
        String username = scmConfig.getUsername();
        String password = aesService.decrypt(scmConfig.getPassword());



        // 클론 프로젝트
        scmService.cloneProject(url, branch, username, password, clonePath);

        return clonePath;
    }


    /**
     * 파라미터 검증
     * @param params
     */
    private void validationParam(String... params) {
        for (String param : params) {
            if (!StringUtils.hasText(param)) {
                throw new IllegalArgumentException("["+param+"] 값은 필수입니다.");
            }
        }
    }


    /**
     * 버전 지정 배포
     * @param buildFileId
     */
    public void designateDeploy(Long buildFileId) throws Exception {

        // 엔티티 조회
        BuildFile findBuildFile = buildFileRepository.findById(buildFileId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_BUILDFILE));
        Job job = jobRepository.findById(findBuildFile.getJob().getId())
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

        if (job.getSteps().isEmpty()) {
            throw new AppBizException("Step config를 먼저 설정해주세요.");
        }

        List<Step> deploySteps = job.getSteps().stream()
                .filter(step -> step.getStepType().equals(StepType.DEPLOY) | step.getStepType().equals(StepType.COMMAND))
                .collect(Collectors.toList());

        if (deploySteps.isEmpty()) {
            throw new AppBizException("Step config의 배포(DEPLOY)를 설정해주세요");
        }

        String sourcePath = findBuildFile.getBuildFilePath();
        String sourceFileName = Paths.get(findBuildFile.getBuildFilePath()).getFileName().toString();
        String targetPath = "/home/ec2-user";
        String targetFileName = "my-app.jar";

        try {
            // 배포
            executeDeploy(deploySteps.get(0), sourcePath, targetPath, targetFileName);

            // 배포후 처리
            executeCommand(deploySteps.get(1));

            // 메세지 발송
            log.info("job.getNotification().getId() = {}", job.getNotification().getId());
            eventPublisher.publishEvent(new DeployCompletedEvent(job.getNotification().getId(), true, sourceFileName));
        } catch (Exception e) {
            log.error("Fail designate deploy. message={}", e.getMessage());
            // 메세지 발송
            eventPublisher.publishEvent(new DeployCompletedEvent(job.getNotification().getId(), false, sourceFileName));
            throw e;
        }

    }


    /**
     * Step on/off 기능
     * @param id
     * @param request
     */
    @Transactional
    public void toggleSwitch(Long id, StepToggleReq request) {
        // 엔티티 조회
        Step findStep = stepRepository.findById(id)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_STEP));

        // on/off
        findStep.toggleSwitch(request.getIsOn());
    }

}
