const projectConfigureScm = document.getElementById('projectConfigure-scm');
const projectConfigureCredential = document.getElementById('projectConfigure-credential');
const projectConfigureBuildType = document.getElementById('projectConfigure-buildType');
const projectConfigureIsTest = document.getElementById('projectConfigure-isTest');
const projectConfigureCommand = document.getElementById('projectConfigure-command');
const toggleContainers = document.querySelectorAll('.toggle-container');

projectConfigureScm.addEventListener('change', function() {

    // empty options인 경우 종료
    if (!this.options[this.selectedIndex].value) {
        document.getElementById('healthCheckMessage').style.display = 'none';
        return;
    }

    // Health check
    fnHealthCheck(
        this.options[this.selectedIndex].dataset.type,
        this.options[this.selectedIndex].dataset.url,
        this.options[this.selectedIndex].dataset.username,
        this.options[this.selectedIndex].dataset.password,
        this.options[this.selectedIndex].dataset.branch
    )
})

toggleContainers.forEach(toggleContainer => {

    toggleContainer.addEventListener('click', () => {
        let isOn = toggleContainer.dataset.isOn === 'true';
        isOn = !isOn;
        toggleContainer.dataset.isOn = isOn;
        fnSetToggleStyle(toggleContainer, isOn);

        let firstDivContainer = toggleContainer.closest('div[data-step-index]');
        let stepIndex = firstDivContainer.getAttribute('data-step-index');
        fnUpdateToggleSwitch(fnGetStepId(Number(stepIndex)), isOn);
    });
})

// 토글 스타일 변경
const fnSetToggleStyle = (toggleContainer, isOn) => {
    const toggleCircle = toggleContainer.querySelector('.toggle-circle');

    if (isOn) {
        toggleContainer.classList.remove('bg-gray-300');
        toggleContainer.classList.add('bg-blue-500');
        toggleCircle.classList.add('transform', 'translate-x-8');
    } else {
        toggleContainer.classList.remove('bg-blue-500');
        toggleContainer.classList.add('bg-gray-300');
        toggleCircle.classList.remove('translate-x-8');
    }
}

// Step 토글
const fnUpdateToggleSwitch = (stepId, toggle) => {
    _axios
        .put(`/api/step/${stepId}/toggle`, {
            'isOn': toggle
        })
        .then(function (response) {
            gfnShowToast('success', '수정이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}

const fnGetStepId = (stepIndex) => {
    let stepId;
    if (stepIndex === 1) {
        stepId = $('#projectConfigure-scm').data('stepId');
    }
    else if (stepIndex === 2) {
        stepId = $('#projectConfigure-buildType').data('stepId');
    }
    else if (stepIndex === 3) {
        stepId = $('#projectConfigure-credential').data('stepId');
    }
    else if (stepIndex === 4) {
        stepId = $('#projectConfigure-command').data('stepId');
    }

    return stepId;
}


// Step 저장
const fnSaveProjectStep = (stepIndex) => {

    let jobId = $('#project-id').val();
    let stepId = fnGetStepId(stepIndex);
    let type = $(`#projectConfigure-stepType-${stepIndex}`).val();
    let buildType, credentialId, scmConfigId, isTest, command;

    if (stepIndex === 1) {
        scmConfigId = $('#projectConfigure-scm').val();
        if (!scmConfigId) return;
    }

    if (stepIndex === 2) {
        buildType = $('#projectConfigure-buildType').val();
        isTest = $('#projectConfigure-isTest').val();
        if (!buildType) return;
    }

    if (stepIndex === 3) {
        credentialId = $('#projectConfigure-credential').val();
        if (!credentialId) return;
    }

    if (stepIndex === 4) {
        credentialId = $('#projectConfigure-credential').val();
        command = $('#projectConfigure-command').val();
        if (!command || !credentialId) return;
    }


    if (stepId) {
        _axios
            .put(`/api/step/${stepId}`, {
                'type': type,
                'buildType': buildType,
                'credentialId': credentialId,
                'scmConfigId': scmConfigId,
                'isTest': isTest,
                'command': command,
            })
            .then(function (response) {
                gfnShowToast('success', '수정이 완료되었습니다.');
            })
            .catch(function (error) {

            });

    } else {
        _axios
            .post(`/api/step`, {
                'jobId': jobId,
                'type': type,
                'buildType': buildType,
                'credentialId': credentialId,
                'scmConfigId' : scmConfigId,
                'isTest': isTest,
                'command': command,
            })
            .then(function (response) {
                gfnShowToast('success', '저장이 완료되었습니다.');
            })
            .catch(function (error) {

            });

    }


}


// SCM configs 조회
const fnGetScmConfigs = () => {
    return _axios
        .get(`/api/scmConfig/searchAll`)
        .then(function (response) {
            return response.data.result;
        })
        .catch(function (error) {
        })
}


// Credentials 조회
const fnGetCredentials = () => {
    return _axios
        .get(`/api/credential/searchAll`)
        .then(function (response) {
            return response.data.result;
        })
        .catch(function (error) {
        })
}

// SCM 데이터셋팅
const fnSetScmConfigs = (data) => {

    let selectElement = projectConfigureScm;

    selectElement.innerHTML = '';

    const emptyOptionElement = document.createElement('option');
    selectElement.appendChild(emptyOptionElement);

    data.forEach(config => {
        const optionElement = document.createElement('option');
        optionElement.value = config.id;
        optionElement.textContent = `${config.url} (${config.branch})`;
        optionElement.dataset.type = config.type;
        optionElement.dataset.url = config.url;
        optionElement.dataset.username = config.username;
        optionElement.dataset.password = config.password;
        optionElement.dataset.branch = config.branch;
        selectElement.appendChild(optionElement);
    })

    // fnSelectChangeTrigger(selectElement);
}

const fnSelectChangeTrigger = (element) => {
    const event = new Event('change', { bubbles: true });
    element.dispatchEvent(event);
}

const fnSetCredentials = (data) => {
    let selectElement = projectConfigureCredential;

    selectElement.innerHTML = '';

    data.forEach(config => {
        const optionElement = document.createElement('option');
        optionElement.value = config.id;
        optionElement.textContent = `${config.name}`;
        optionElement.dataset.name = config.type;
        optionElement.dataset.username = config.username;
        optionElement.dataset.host = config.host;
        optionElement.dataset.port = config.port;

        selectElement.appendChild(optionElement);
    })

}

// step 정보 셋팅
const fnSetStepInfo = (list) => {

    // toggleContainer.addEventListener('click', () => {
    //     const toggleCircle = toggleContainer.querySelector('.toggle-circle');
    //     isOn = !isOn;
    //
    //     fnSetToggleStyle(toggleContainer, toggleCircle, isOn);
    //
    //     let parentWithStepIndex = toggleContainer.closest('div[data-step-index]');
    //     let stepIndex = parentWithStepIndex.getAttribute('data-step-index');
    //     fnUpdateToggleSwitch(fnGetStepId(Number(stepIndex)), isOn);
    // });



    list.forEach(step => {
        if (step.stepType === 'SCM') {
            projectConfigureScm.dataset.stepId = step.id;
            projectConfigureScm.value = step.stepValue;
            fnSelectChangeTrigger(projectConfigureScm);
            fnSetStepToggleInfo(projectConfigureScm, step.isOn);
        } else if (step.stepType === 'BUILD') {
            projectConfigureBuildType.dataset.stepId = step.id;
            projectConfigureBuildType.value = step.stepValue;
            projectConfigureIsTest.value = step.isTest;
            fnSelectChangeTrigger(projectConfigureBuildType);
            fnSetStepToggleInfo(projectConfigureBuildType, step.isOn);
        } else if (step.stepType === 'DEPLOY') {
            projectConfigureCredential.dataset.stepId = step.id;
            projectConfigureCredential.value = step.stepValue;
            fnSelectChangeTrigger(projectConfigureCredential);
            fnSetStepToggleInfo(projectConfigureCredential, step.isOn);
        } else if (step.stepType === 'COMMAND') {
            projectConfigureCommand.dataset.stepId = step.id;
            projectConfigureCommand.value = step.stepValue;
            fnSetStepToggleInfo(projectConfigureCommand, step.isOn);
        }
    });
}

const fnSetStepToggleInfo = (stepElement, isOn) => {
    const firstDivContainer = stepElement.closest('div[data-step-index]');
    const toggleContainer = firstDivContainer.querySelector('.toggle-container');
    toggleContainer.dataset.isOn = isOn;
    fnSetToggleStyle(toggleContainer, isOn);
}

// 연결확인
const fnHealthCheck = (type, url, username, password, branch) => {
    document.getElementById('healthCheckMessage').style.display = 'none';
    showLoading();

    _axios
        .post(`/api/scmConfig/healthCheck`, {
            'type': type,
            'url': url,
            'username': username,
            'password': password,
            'branch': branch
        })
        .then(function (response) {
            document.getElementById('healthCheckMessage').style.display = 'inline-block';

            let element = document.getElementById('healthCheckMessage');
            let message = "";

            if (response.data.result.includes('Failed')) {
                element.classList.remove('text-green-500');
                element.classList.add('text-red-500');
                message = "connection failed.";
            } else {
                element.classList.remove('text-red-500');
                element.classList.add('text-green-500');
                message = "connection successful.";
            }

            element.innerText = message;

        })
        .catch(function (error) {

        })
        .finally(() => {
            fnHideLoading();
        });
}

// 로딩 보이기
const showLoading = () => {
    document.getElementById('loadingText').style.display = 'inline-block';
    dotCount = 0;
    loadingInterval = setInterval(() => {
        dotCount = (dotCount + 1) % 4; // 0, 1, 2, 3
        document.getElementById('dots').textContent = '.'.repeat(dotCount);
    }, 300);
};

// 로딩 숨기기
const fnHideLoading = () => {
    document.getElementById('loadingText').style.display = 'none';
}