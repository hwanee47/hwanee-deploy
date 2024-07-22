package com.deploy.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GradleService implements BuildService {

    private final String DEFAULT_TARGET_PATH = System.getProperty("user.home") + "/deployApp/build/";
    private Logger logger = LoggerFactory.getLogger(GradleService.class);

    public void setHistoryLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String executeBuild(String projectPath, boolean isTest) throws Exception {

        logger.info("[OK] Start build. projectPath={}", projectPath);

        String builtFile = null;

        try {
            File project = new File(projectPath);

            ProcessBuilder builder = new ProcessBuilder();
            if (isTest) {
                builder.command("./gradlew", "clean", "build");
            } else {
                builder.command("./gradlew", "clean", "build", "-x", "test");
            }

            builder.directory(project);
            logger.info("[OK] Build command={}", builder.command().stream().collect(Collectors.joining(" ")));

            // 빌드 시작
            Process process = builder.start();
            StringBuilder stdError = new StringBuilder();

            // 표준 출력 (stdout) 스트림 읽기
            Thread stdoutThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info(line);
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
                        if (!line.contains("Note")) { // 경고 메시지를 필터링하여 표준 에러에서 제외
                            stdError.append(line).append("\n");
                            logger.error(line);
                        }
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

            if (exitCode != 0 || stdError.length() > 0) {
                throw new Exception("Gradle build failed with exit code=" + exitCode + ". Error: " + stdError.toString());
            }

            // 빌드된 JAR 파일 경로 수집
            builtFile = findAndMoveBuiltJar(projectPath);

            logger.info("[OK] End build. exitCode={}, builtFile={}", exitCode, builtFile);

        } catch (Exception e) {
            logger.error("[ERROR] Failed gradle build. exception={}, message={}", e.getClass().getName(), e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return builtFile;
    }

    private String findAndMoveBuiltJar(String projectPath) throws IOException {

        String projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);

        try (Stream<Path> paths = Files.walk(Paths.get(projectPath, "build", "libs"))) {
            Optional<Path> optionalPath = paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .max(Comparator.comparingLong(path -> path.toFile().lastModified()));

            if (optionalPath.isPresent()) {
                return moveBuiltJarWithTimestamp(optionalPath.get().toString(), projectName);
            }
        }
        return null;
    }

    private void deletePlainJar(String projectPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(projectPath, "build", "libs"))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith("-plain.jar"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                            logger.info("[OK] Deleted plain.jar file={}", path);
                        } catch (IOException e) {
                            logger.warn("[WARN] Failed to delete plain.jar file={}. message={}", path, e.getMessage());
                        }
                    });
        } catch (IOException e) {
            logger.warn("[ERROR] Failed to walk through the directory. exception={}, message={}", e.getClass().getName(), e.getMessage());
        }
    }

    private String moveBuiltJarWithTimestamp(String builtFilePath, String projectName) throws IOException {
        Path sourcePath = Paths.get(builtFilePath);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HHmmss").format(new Date());
        String fileNameWithTimestamp = timestamp + ".jar";

        // 타겟 디렉토리 없는경우 생성
        Path targetDirectory = Paths.get(DEFAULT_TARGET_PATH + projectName + File.separator);
        if (Files.notExists(targetDirectory)) {
            Files.createDirectories(targetDirectory);
        }
        Path targetPath = targetDirectory.resolve(fileNameWithTimestamp);

        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("[OK] Moved built JAR file to {}", targetPath);
        return targetPath.toString();
    }


}
