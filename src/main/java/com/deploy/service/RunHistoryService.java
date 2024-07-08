package com.deploy.service;

import com.deploy.repository.RunHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunHistoryService {

    private final RunHistoryRepository runHistoryRepository;

//
//    @Transactional
//    public Long createRunHistory(Job job, Long jobRunSeq, String logFilePath) {
//        Long maxSeq = runHistoryRepository.findMaxSeq(job.getId());
//        RunHistory runHistory = RunHistory.createRunHistory(job, maxSeq, logFilePath);
//        runHistoryRepository.save(runHistory);
//
//        return runHistory.getId();
//    }

}
