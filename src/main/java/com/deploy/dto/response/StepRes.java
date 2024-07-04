package com.deploy.dto.response;

import com.deploy.entity.Step;
import com.deploy.entity.embed.BuildSet;
import com.deploy.entity.enums.StepType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepRes {

    private Long id;
    private Long stepIndex;
    private StepType stepType;
    private Object stepValue;


    public StepRes(Step step) {
        this.id = step.getId();
        this.stepIndex = step.getStepIndex();
        this.stepType = step.getStepType();

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
