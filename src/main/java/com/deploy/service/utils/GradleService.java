package com.deploy.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class GradleService implements BuildService{

    @Override
    public String executeBuild(String projectPath) throws Exception {

        log.info("Start build. projectPath={}", projectPath);
        String builtFile = null;

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

            // 빌드후 -plain.jar 삭제
            deletePlainJar(projectPath);

            // 빌드된 JAR 파일 경로 수집
            try (Stream<Path> paths = Files.walk(Paths.get(projectPath, "build", "libs"))) {
                Optional<Path> optionalPath = paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".jar"))
                        .max(Comparator.comparingLong(path -> path.toFile().lastModified()));
                if (optionalPath.isPresent()) {
                    builtFile = optionalPath.get().toString();
                }
            }

            log.info("End build. exitCode={}, builtFile={}", exitCode, builtFile);


        } catch (Exception e) {
            log.error("Gradle build failed. message={}", e.getMessage());
            throw e;
        }

        return builtFile;
    }


    private void deletePlainJar(String projectPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(projectPath, "build", "libs"))) {
            paths.filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith("-plain.jar"))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                        log.info("Deleted plain.jar file={}", path);
                    } catch (IOException e) {
                        log.warn("Failed to delete plain.jar file={}. message={}", path, e.getMessage());
                    }
                });
        } catch (IOException e) {
            log.warn("Failed to walk through the directory. message={}", e.getMessage());
        }
    }
}
