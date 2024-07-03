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
import com.deploy.service.utils.ScmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //save
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
     * SCM 자격증명
     * @param service
     * @param url
     * @param username
     * @param password
     * @return
     * @throws GitAPIException
     */
    public Boolean isConnected(ScmService service, String url, String username, String password) throws GitAPIException {

        try {
            return service.isConnected(url, username, password);
        } catch (GitAPIException e) {
            log.error("SCM({}) connected error : {}", service.getType(), e.getMessage());
            throw e;
        }

    }

}
