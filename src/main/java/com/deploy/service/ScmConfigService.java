package com.deploy.service;

import com.deploy.dto.request.CodeManageConfigCreateReq;
import com.deploy.dto.response.CodeManageSetRes;
import com.deploy.entity.ScmConfig;
import com.deploy.repository.ScmConfigRepository;
import com.deploy.service.utils.ScmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScmConfigService {

    private final ScmConfigRepository scmConfigRepository;


    /**
     * 엔티티 저장
     * @param request
     * @return
     */
    @Transactional
    public Long save(CodeManageConfigCreateReq request) {

        ScmConfig entity = request.toEntity();
        scmConfigRepository.save(entity);

        return entity.getId();
    }


    /**
     * 엔티티 단건 조회
     * @param id
     * @return
     */
    public CodeManageSetRes findById(Long id) {
        ScmConfig findEntity = scmConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such data."));

        return new CodeManageSetRes();
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
