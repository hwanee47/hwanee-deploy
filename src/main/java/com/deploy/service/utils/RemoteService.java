package com.deploy.service.utils;

import com.deploy.exception.RemoteException;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Vector;

@Slf4j
@Service
public class RemoteService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(RemoteService.class);

    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;
    private ChannelExec channelExec = null;

    public void setHistoryLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    /**
     * 초기화
     * @param host
     * @param port
     * @param username
     * @param password
     * @param privateKey
     */
    public void init(String host, int port, String username, String password, String privateKey) {

        logger.info("[OK] Start RemoteService init. host={}", host);

        try {
            // 로그
            JSch.setLogger(new SLF4JLoggerWrapper(logger));

            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);

            // 개인키가 존재할때
            if (StringUtils.hasLength(privateKey)) {
                byte[] privateKeyBytes = privateKey.getBytes();
                jsch.addIdentity("key", privateKeyBytes, null, null);
            }
            // 개인키 없는경우 비밀번호로 접속
            else {
                session.setPassword(password);
            }

            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            logger.info("[OK] Succeeded RemoteService init. host={}", host);

        } catch (Exception e) {
            logger.error("[ERROR] Failed RemoteService init. message={}", e.getMessage());
        }
    }


    /**
     * 스크립트 실행
     * @param script
     * @return
     * @throws Exception
     */
    public String executeScript(String script) throws Exception {

        logger.info("[OK] Start RemoteService execute script.");

        if (session == null) {
            throw new IllegalStateException("RemoteService init메서드를 먼저 호출해주세요.");
        }

        try {
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(script);
            channelExec.setErrStream(System.err);
            BufferedReader reader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));


            channelExec.connect();

            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();
            String line;

            // 표준 출력 읽기
            logger.info("====================================================");
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                logger.info("[OK] [target server] -> " + line);
            }

            // 에러 출력 읽기
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
                logger.error("[ERROR] [target server] -> " + line);
            }
            logger.info("====================================================");

            // 에러 메시지가 있는 경우
            if (errorOutput.length() > 0) {
                throw new RemoteException(errorOutput.toString());
            }

            logger.info("[OK] Succeeded RemoteService execute script.");

            return output.toString();
        } catch (Exception e) {
            logger.error("[ERROR] Failed RemoteService executeScript. message={}", e.getMessage());
            throw e;
        } finally {
            this.disconnect();
        }

    }


    /**
     * 업로드
     * @param file
     * @param remotePath
     * @param newFileName
     * @return
     */
    public boolean upload(File file, String remotePath, String newFileName) throws Exception{

        logger.info("[OK] Start RemoteService upload.");

        //sftp로 접속
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;

        boolean isUpload = false;
        SftpATTRS attrs;
        FileInputStream in = null;
        try {

            in = new FileInputStream(file);
            channelSftp.cd(remotePath);
            channelSftp.put(in, newFileName); // 파일명을 변경하여 업로드

            // 업로드했는지 확인
            if (this.exists(remotePath + "/" + newFileName)) {
                isUpload = true;
            }

            logger.info("[OK] End RemoteService upload.");

        } catch (Exception e) {
            logger.error("[ERROR] Failed RemoteService upload. message={}", e.getMessage());
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.disconnect();
        }

        return isUpload;
    }


    /**
     * 디렉토리( or 파일) 존재 여부
     * @param path 디렉토리 (or 파일)
     * @return
     */
    private boolean exists(String path) {

        if (channelSftp == null) {
            throw new IllegalStateException("ChannelSftp필드가 초기화가 되지 않았습니다.");
        }

        Vector res = null;
        try {
            res = channelSftp.ls(path);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
        }
        return res != null && !res.isEmpty();
    }


    /**
     * 연결 종료
     */
    private void disconnect() {

        if (channelExec != null) {
            channelExec.disconnect();
        }

        if (channelSftp != null) {
            channelSftp.disconnect();
        }

        if (channel != null) {
            channel.disconnect();
        }

        if (session != null) {
            session.disconnect();
        }

    }


    // JSch Logger 설정
    public static class SLF4JLoggerWrapper implements Logger {
        private final org.slf4j.Logger logger;

        public SLF4JLoggerWrapper(org.slf4j.Logger logger) {
            this.logger = logger;
        }

        @Override
        public boolean isEnabled(int level) {
            switch (level) {
                case DEBUG:
                    return logger.isDebugEnabled();
                case INFO:
                    return logger.isInfoEnabled();
                case WARN:
                    return logger.isWarnEnabled();
                case ERROR:
                case FATAL:
                    return logger.isErrorEnabled();
                default:
                    return false;
            }
        }

        @Override
        public void log(int level, String message) {
            switch (level) {
                case DEBUG:
                    logger.debug(message);
                    break;
                case INFO:
                    logger.info(message);
                    break;
                case WARN:
                    logger.warn(message);
                    break;
                case ERROR:
                case FATAL:
                    logger.error(message);
                    break;
            }
        }
    }
}
