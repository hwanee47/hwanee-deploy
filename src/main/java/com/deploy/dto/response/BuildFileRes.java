package com.deploy.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Paths;

@Getter
@Setter
public class BuildFileRes {

    private Long id;
    private String buildFileName;
    private String buildFilePath;
    private String description;


    @Builder
    public BuildFileRes(Long id, String buildFilePath, String description) {
        this.id = id;
        this.buildFileName = Paths.get(buildFilePath).getFileName().toString(); // 파일명 추출
        this.buildFilePath = buildFilePath;
        this.description = description;
    }
}
