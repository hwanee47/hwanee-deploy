package com.deploy.dto.response;

import com.deploy.entity.Job;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRes {

    private String name;
    private String description;

    public JobRes(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public JobRes(Job job) {
        this.name = job.getName();
        this.description = job.getDescription();
    }
}
