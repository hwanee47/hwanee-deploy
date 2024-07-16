package com.deploy.service.utils;

import com.deploy.entity.enums.ScmType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScmServiceFactory {

    private final GitService gitService;
    private final SvnService svnService;

    public void setHistoryLogger(Logger logger) {
        gitService.setHistoryLogger(logger);
        svnService.setHistoryLogger(logger);
    }

    public ScmService getScmService(ScmType type) {
        switch (type) {
            case GIT:
                return gitService;
            case SVN:
                return svnService;
            default:
                throw new IllegalArgumentException("알수없는 SCM 유형 입니다. type: " + type);
        }
    }
}
