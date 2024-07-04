package com.deploy.service;

import com.deploy.dto.request.*;
import com.deploy.dto.response.CodeManageSetRes;
import com.deploy.dto.response.CredentialRes;
import com.deploy.dto.response.ScmConfigRes;
import com.deploy.entity.Credential;
import com.deploy.entity.ScmConfig;
import com.deploy.entity.enums.ScmType;
import com.deploy.exception.AppBizException;
import com.deploy.repository.ScmConfigRepository;
import com.deploy.service.utils.GitService;
import com.deploy.service.utils.ScmService;
import com.deploy.service.utils.SvnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScmConfigService {

    private final ScmConfigRepository scmConfigRepository;


    /**
     * SCM config 다건 조회
     * @param condition
     * @param pageable
     * @return
     */
    public Page<ScmConfigRes> search(ScmConfigSearchCond condition, Pageable pageable) {
        Page<ScmConfig> list = scmConfigRepository.search(condition, pageable);

        Page<ScmConfigRes> results = new PageImpl<>(
                list.stream().map(ScmConfig -> {
                    ScmConfigRes scmConfigRes = new ScmConfigRes(ScmConfig);
                    return scmConfigRes;
                }).collect(Collectors.toList())
                , pageable
                , list.getTotalElements()
        );

        return results;
    }

    public List<ScmConfigRes> searchAll() {
        List<ScmConfig> list = scmConfigRepository.findAll();

        List<ScmConfigRes> results = list.stream()
                .map(ScmConfig -> {
                    ScmConfigRes scmConfigRes = new ScmConfigRes(ScmConfig);
                    return scmConfigRes;
                })
                .collect(Collectors.toList());

        return results;
    }


    /**
     * SCM config 단건 조회
     * @param id
     * @return
     */
    public ScmConfigRes findScmConfig(Long id) {
        ScmConfig scmConfig = scmConfigRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in ScmConfig."));
        return new ScmConfigRes(scmConfig);
    }


    /**
     * SCM config 추가
     * @param request
     * @return
     */
    @Transactional
    public Long createScmConfig(ScmConfigCreateReq request) {

        ScmType type = request.getType();
        String description = request.getDescription();
        String url = request.getUrl();
        String username = request.getUsername();
        String password = request.getPassword();
        String branch = request.getBranch();

        ScmConfig scmConfig = ScmConfig.createScmConfig(type, description, url, username, password, branch);

        // health check
        try {
            if (healthCheck(type, url, username, password, branch)) {
                scmConfig.connectSuccess();
            };
        } catch (GitAPIException e) {
            scmConfig.connectFail(e.getMessage());
        }

        // save
        scmConfigRepository.save(scmConfig);

        return scmConfig.getId();
    }


    /**
     * SCM config 수정
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public Long updateScmConfig(Long id, ScmConfigUpdateReq request) {

        ScmType type = request.getType();
        String description = request.getDescription();
        String url = request.getUrl();
        String username = request.getUsername();
        String password = request.getPassword();
        String branch = request.getBranch();

        ScmConfig findScmConfig = scmConfigRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in ScmConfig."));

        // update
        findScmConfig.changeInfo(type, description, url, username, password, branch);


        // health check
        try {
            if (healthCheck(type, url, username, password, branch)) {
                findScmConfig.connectSuccess();
            };
        } catch (GitAPIException e) {
            findScmConfig.connectFail(e.getMessage());
        }

        return id;
    }


    /**
     * ScmConfig 삭제
     * @param id
     */
    @Transactional
    public void deleteScmConfig(Long id) {
        ScmConfig findScmConfig = scmConfigRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in ScmConfig."));

        scmConfigRepository.delete(findScmConfig);
    }

    /**
     * 연결 테스트 실행
     * @param request
     */
    public String healthCheck(ExecuteConntectReq request) {
        String url = request.getUrl();
        String username = request.getUsername();
        String password = request.getPassword();
        String branch = request.getBranch();

        ScmType scmType = request.getType();
        ScmService scmService = getScmService(scmType);

        try {
            boolean connected = scmService.isConnected(url, username, password, branch);
            log.info("isConnected={}", connected);
            return "Succeeded :: Test connection success.";
        } catch (GitAPIException e) {
            log.error("SCM({}) connected error : {}", scmType, e.getMessage());
            return "Failed :: " + e.getMessage();
        }

    }


    /**
     * SCM 자격증명
     * @param scmType
     * @param url
     * @param username
     * @param password
     * @param branch
     * @return
     * @throws GitAPIException
     */
    private boolean healthCheck(ScmType scmType, String url, String username, String password, String branch) throws GitAPIException {
        try {
            ScmService scmService = getScmService(scmType);
            return scmService.isConnected(url, username, password, branch);
        } catch (GitAPIException e) {
            log.error("SCM({}) connected error : {}", scmType, e.getMessage());
            throw e;
        }

    }


    /**
     * ScmType에 맞는 ScmService 구현체 반환
     * @param scmType
     * @return
     */
    private ScmService getScmService(ScmType scmType) {
        if (scmType.equals(ScmType.SVN)) {
            return new SvnService();
        } else if (scmType.equals(ScmType.GIT)) {
            return new GitService();
        }

        return null;
    }

}
