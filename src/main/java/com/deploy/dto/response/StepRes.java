package com.deploy.dto.response;

import com.deploy.entity.Step;
import com.deploy.entity.embed.BuildSet;
import com.deploy.entity.enums.StepType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepRes {

    private Long id;
    private Long stepIndex;
    private StepType stepType;
    private Object stepValue;

    @JsonProperty(value = "isTest")
    private boolean isTest;

    @JsonProperty(value = "isOn")
    private boolean isOn;


    public StepRes(Step step) {
        this.id = step.getId();
        this.stepIndex = step.getStepIndex();
        this.stepType = step.getStepType();
        this.isTest = step.getBuildSet().isBuildTest();
        this.isOn = step.isOn();

        switch (step.getStepType()) {
            case SCM:
                stepValue = step.getScmConfig().getId();
                break;
            case BUILD:
                stepValue = step.getBuildSet().getBuildType();
                break;
            case DEPLOY:
                stepValue = step.getCredential().getId();
                break;
            case COMMAND:
                stepValue = step.getCommand();
                break;
        }

    }

}
