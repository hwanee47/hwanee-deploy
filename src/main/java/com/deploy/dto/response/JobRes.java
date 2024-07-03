package com.deploy.dto.response;

import com.deploy.entity.Job;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRes {

    private Long id;
    private String name;
    private String description;

    public JobRes(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


    public JobRes(Job job) {
        this.id = job.getId();
        this.name = job.getName();
        this.description = job.getDescription();
    }
}
