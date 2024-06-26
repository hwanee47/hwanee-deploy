package com.deploy.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Slf4j
@Service
public class GradleService implements BuildService{

    @Override
    public void executeBuild(String projectPath) throws Exception {

        try {
            File project = new File(projectPath);

            ProcessBuilder builder = new ProcessBuilder();
            builder.command("./gradlew", "clean", "build");
            builder.directory(project);

            // 빌드 시작
            Process process = builder.start();

            // 표준 출력 (stdout) 스트림 읽기
            Thread stdoutThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // 표준 에러 (stderr) 스트림 읽기
            Thread stderrThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            stdoutThread.start();
            stderrThread.start();

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            stdoutThread.join();
            stderrThread.join();

            System.out.println("\nExited with error code : " + exitCode);


        } catch (Exception e) {
            log.error("Gradle build failed. message={}", e.getMessage());
            throw e;
        }

    }
}
