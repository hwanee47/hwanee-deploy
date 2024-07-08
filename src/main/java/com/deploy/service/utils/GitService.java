package com.deploy.service.utils;

import com.deploy.entity.enums.ScmType;
import com.deploy.exception.NotFoundGitBranchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GitService implements ScmService {


    @Override
    public String getType() {
        return ScmType.GIT.toString();
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

        try {
            Git.lsRemoteRepository()
                    .setHeads(true)
                    .setTags(false)
                    .setRemote(url)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .call();

            return true;
        } catch (GitAPIException gitAPIException) {
            log.error("Fail connect project. message={}", gitAPIException.getMessage());
            throw gitAPIException;
        }

    }


    /**
     * 접속 가능 여부 확인 with 브랜치
     * @param url
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean isConnected(String url, String username, String password, String branch) throws GitAPIException {

        try {
            List<String> branches = getBranches(url, username, password);

            // branch check
            if (branches.isEmpty() || (branch != null && !branches.contains(branch))) {
                throw new NotFoundGitBranchException("Not found Git branch = " + branch);
            }

            return true;
        } catch (GitAPIException gitAPIException) {
            log.error("Fail connect project. message={}", gitAPIException.getMessage());
            throw gitAPIException;
        }

    }


    private List<String> getBranches(String url, String username, String password) throws GitAPIException {
        return Git.lsRemoteRepository()
                .setHeads(true)
                .setTags(false)
                .setRemote(url)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call()
                .stream()
                .map(ref -> {
                    return ref.getName().replace("refs/heads/", "");
                })
                .collect(Collectors.toList());
    }



    // 클론 프로젝트
    @Override
    public void cloneProject(String url, String branch, String username, String password, String clonePath) throws GitAPIException, IOException {

        try {
            File directory = new File(clonePath);

            if (directory.exists()) {
                FileUtils.cleanDirectory(directory);
            }


            if (directory.mkdirs()) {
                log.info("Directory create success. clonePath={}", clonePath);
            }

            Git.cloneRepository()
                    .setURI(url)
                    .setBranch(branch)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .setDirectory(directory)
                    .call();
        } catch (GitAPIException gitAPIException) {
            log.error("Fail clone project. message={}", gitAPIException.getMessage());
            throw gitAPIException;
        } catch (IOException ioException) {
            log.error("Fail create directory. message={}", ioException.getMessage());
            throw ioException;
        }
    }


}
