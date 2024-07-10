package com.deploy.service;

import com.deploy.dto.request.BuildFileUpdateReq;
import com.deploy.dto.request.JobUpdateReq;
import com.deploy.dto.response.BuildFileRes;
import com.deploy.entity.BuildFile;
import com.deploy.entity.Job;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.BuildFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuildFileService {

    private final BuildFileRepository buildFileRepository;


    /**
     * 빌드파일 목록 조회
     * @param jobId
     * @param pageable
     * @return
     */
    public Page<BuildFileRes> searchByJobId(Long jobId, Pageable pageable) {
        Page<BuildFile> list = buildFileRepository.searchByJobId(jobId, pageable);

        List<BuildFileRes> results = list.stream()
                .map(BuildFile -> BuildFileRes.builder()
                        .id(BuildFile.getId())
                        .buildFilePath(BuildFile.getBuildFilePath())
                        .description(BuildFile.getDescription())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, list.getTotalElements());
    }

    /**
     * 빌드파일 조회
     * @param buildFileId
     * @return
     */
    public BuildFileRes findBuildFile(Long buildFileId) {
        BuildFile findBuildFile = buildFileRepository.findById(buildFileId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_BUILDFILE));

        return BuildFileRes.builder()
                .id(findBuildFile.getId())
                .buildFilePath(findBuildFile.getBuildFilePath())
                .description(findBuildFile.getDescription())
                .build();
    }


    /**
     * 빌드파일 생성
     * @param job
     * @param buildFilePath
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createBuildFile(Job job, String buildFilePath) {
        BuildFile buildFile = BuildFile.createBuildFile(job, buildFilePath);
        buildFileRepository.save(buildFile);
    }


    /**
     * 빌드파일 수정
     * @param buildFileId
     * @param request
     * @return
     */
    @Transactional
    public Long updateBuildFile(Long buildFileId, BuildFileUpdateReq request) {
        BuildFile findBuildFile = buildFileRepository.findById(buildFileId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_BUILDFILE));

        // update
        findBuildFile.changeInfo(request.getDescription());

        return buildFileId;
    }


}
