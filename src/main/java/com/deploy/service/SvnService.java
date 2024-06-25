package com.deploy.service;

import com.deploy.entity.ScmType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SvnService implements ScmService{


    @Override
    public String getType() {
        return ScmType.SVN.toString();
    }

    /**
     * 접속 가능 여부 확인
     * @param url
     * @param branch
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean isConnected(String url, String branch, String username, String password) throws GitAPIException {


        return true;

    }
}
