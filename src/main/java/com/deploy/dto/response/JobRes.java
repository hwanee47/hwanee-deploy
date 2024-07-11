package com.deploy.dto.response;

import com.deploy.entity.Job;
import com.deploy.entity.Step;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@Slf4j
public class JobRes {

    private Long id;
    private String name;
    private String description;
    private List<StepRes> steps;
    private Long notificationId;

    public JobRes(Job job) {
        this.id = job.getId();
        this.name = job.getName();
        this.description = job.getDescription();
        this.steps = job.getSteps().stream()
                .map(StepRes::new)
                .collect(toList());
        this.notificationId = job.getNotification() == null ? null : job.getNotification().getId();
    }
}
