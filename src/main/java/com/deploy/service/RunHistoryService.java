package com.deploy.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import com.deploy.dto.response.RunHistoryDetailRes;
import com.deploy.dto.response.RunHistoryRes;
import com.deploy.entity.Job;
import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import com.deploy.entity.Step;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.RunHistoryDetailRepository;
import com.deploy.repository.RunHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunHistoryService {

    private final RunHistoryRepository runHistoryRepository;
    private final RunHistoryDetailRepository runHistoryDetailRepository;

    private final String DEFAULT_LOG_PATH = System.getProperty("user.home") + "/deployApp/logs/";

    /**
     * Job 히스토리 조회
     * @param jobId
     * @param pageable
     * @return
     */
    public Page<RunHistoryRes> searchByJobId(Long jobId, Pageable pageable) {
        Page<RunHistory> list = runHistoryRepository.searchByJobId(jobId, pageable);

        List<RunHistoryRes> results = list.stream()
                .map(RunHistory -> RunHistoryRes.builder()
                        .id(RunHistory.getId())
                        .seq(RunHistory.getJobRunSeq())
                        .isSuccess(RunHistory.isSuccess())
                        .totalRunTime(RunHistory.getTotalRunTime())
                        .createdAt(RunHistory.getCreatedAt())
                        .status(RunHistory.getStatus())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, list.getTotalElements());
    }


    /**
     * Job 히스토리 상세 조회
     * @param historyId
     * @param pageable
     * @return
     */
    public Page<RunHistoryDetailRes> searchByHistoryId(Long jobId, Long historyId, Pageable pageable) {
        Page<RunHistoryDetail> list = runHistoryRepository.searchByHistoryId(jobId, historyId, pageable);

        List<RunHistoryDetailRes> results = list.stream()
                .map(RunHistoryDetail -> RunHistoryDetailRes.builder()
                        .id(RunHistoryDetail.getId())
                        .historyId(historyId)
                        .isSuccess(RunHistoryDetail.isSuccess())
                        .runTime(RunHistoryDetail.getRunTime())
                        .runFailLog(RunHistoryDetail.getRunFailLog())
                        .stepType(RunHistoryDetail.getStep().getStepType())
                        .status(RunHistoryDetail.getStatus())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, list.getTotalElements());
    }


    /**
     * Run History 생성
     * @param job
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createRunHistory(Job job) {

        Long maxSeq = runHistoryRepository.findMaxSeq(job.getId());
        RunHistory runHistory = RunHistory.createRunHistory(job, maxSeq, "");
        runHistoryRepository.save(runHistory);

        return runHistory.getId();
    }


    /**
     * Run History Detail 생성
     * @param runHistoryId
     * @param step
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createRunHistoryDetail(Long runHistoryId, Step step) {

        // 엔티티 조회
        RunHistory findRunHistory = runHistoryRepository.findById(runHistoryId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORY));

        RunHistoryDetail runHistoryDetail = RunHistoryDetail.createRunHistoryDetail(step);
        runHistoryDetail.setRunHistory(findRunHistory);

        runHistoryDetailRepository.save(runHistoryDetail);

        return runHistoryDetail.getId();
    }


    /**
     * Run History Detail 성공처리
     * @param runHistoryDetailId
     * @param prevResult
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void successDetail(Long runHistoryDetailId, String prevResult) {

        // 엔티티 조회
        RunHistoryDetail findRunHistoryDetail = runHistoryDetailRepository.findById(runHistoryDetailId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORYDETAIL));

        findRunHistoryDetail.success(prevResult);

    }


    /**
     * Run History Detail 실패처리
     * @param runHistoryDetailId
     * @param message
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failDetail(Long runHistoryDetailId, String message) {

        // 엔티티 조회
        RunHistoryDetail findRunHistoryDetail = runHistoryDetailRepository.findById(runHistoryDetailId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORYDETAIL));

        findRunHistoryDetail.fail(message);

    }


    /**
     * Run History Detail 실행시간 수정
     * @param runHistoryDetailId
     * @param runTime
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeDetailRunTime(Long runHistoryDetailId, Long runTime) {

        // 엔티티 조회
        RunHistoryDetail findRunHistoryDetail = runHistoryDetailRepository.findById(runHistoryDetailId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORYDETAIL));

        findRunHistoryDetail.changeRunTime(runTime);
    }


    /**
     * Run History 완료
     * @param runHistoryId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void completeRun(Long runHistoryId, String logFilepath) {

        // 엔티티 조회
        RunHistory findRunHistory = runHistoryRepository.findById(runHistoryId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORY));

        findRunHistory.completeRun(logFilepath);
    }


    /**
     * Run fail
     * @param runHistoryId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failRun(Long runHistoryId) {

        // 엔티티 조회
        RunHistory findRunHistory = runHistoryRepository.findById(runHistoryId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORY));

        findRunHistory.failRun();
    }


    /**
     * History 용 로거 생성
     * @param job
     * @return
     */
    public Logger createHistoryLogger(Job job) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 파일 이름 설정 (jobId와 현재 시간을 사용)
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String logFileName = DEFAULT_LOG_PATH + job.getName() + "/" + dateTime + ".log";

        // 패턴 레이아웃 설정
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("[%d{yyyy-MM-dd HH:mm:ss:SSS}] %highlight([%-5level]) %magenta([%thread]) %cyan([%logger{5}\\(%line\\)]) - %msg%n%ex{3}");
        encoder.start();

        // 파일 Appender 설정
        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("FILE-" + job.getId());
        fileAppender.setFile(logFileName);
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        // 콘솔 Appender 설정
        ConsoleAppender consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setName("CONSOLE-" + job.getId());
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();


        // 로거 설정
        Logger logger = (Logger) LoggerFactory.getLogger("JobLogger-" + job.getName());
        logger.setAdditive(false); // 부모 로거에 추가되지 않도록 설정
        logger.addAppender(fileAppender);
        logger.addAppender(consoleAppender);
        logger.setLevel(ch.qos.logback.classic.Level.DEBUG);

        return logger;
    }

    /**
     * 로그파일 경로 반환
     * @param runHistoryId
     * @return
     */
    public String getLogFilePath(Long runHistoryId) {

        // 엔티티조회
        RunHistory findRunHistory = runHistoryRepository.findById(runHistoryId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORY));

        return findRunHistory.getLogFilePath();
    }

}
