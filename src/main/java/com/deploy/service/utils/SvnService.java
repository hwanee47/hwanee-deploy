package com.deploy.service.utils;

import com.deploy.entity.enums.ScmType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SvnService implements ScmService {


    @Override
    public String getType() {
        return ScmType.SVN.toString();
    }

    /**
     * 접속 가능 여부 확인
     * @param url
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean isConnected(String url, String username, String password) throws GitAPIException {


        return true;

    }

    @Override
    public void cloneProject(String url, String branch, String username, String password, String clonePath) throws GitAPIException, IOException {

    }
}
