const projectConfigureScm = document.getElementById('projectConfigure-scm');
const projectConfigureCredential = document.getElementById('projectConfigure-credential');
const projectConfigureBuildType = document.getElementById('projectConfigure-buildType');

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


// Step 저장
const fnSaveProjectStep = (stepIndex) => {

    let jobId = $('#project-id').val();
    let type = $(`#projectConfigure-stepType-${stepIndex}`).val();
    let buildType, credentialId, scmConfigId, stepId;

    if (stepIndex === 1) {
        scmConfigId = $('#projectConfigure-scm').val();
        stepId = $('#projectConfigure-scm').data('stepId');
        if (!scmConfigId) return;
    }

    if (stepIndex === 2) {
        buildType = $('#projectConfigure-buildType').val();
        stepId = $('#projectConfigure-buildType').data('stepId');
        if (!buildType) return;
    }

    if (stepIndex === 3) {
        credentialId = $('#projectConfigure-credential').val();
        stepId = $('#projectConfigure-credential').data('stepId');
        if (!credentialId) return;
    }


    if (stepId) {
        _axios
            .put(`/api/step/${stepId}`, {
                'type': type,
                'buildType': buildType,
                'credentialId': credentialId,
                'scmConfigId' : scmConfigId
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
                'scmConfigId' : scmConfigId
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

    list.forEach(step => {
        if (step.stepType === 'SCM') {
            projectConfigureScm.dataset.stepId = step.id;
            projectConfigureScm.value = step.stepValue;
            fnSelectChangeTrigger(projectConfigureScm);
        } else if (step.stepType === 'BUILD') {
            projectConfigureBuildType.dataset.stepId = step.id;
            projectConfigureBuildType.value = step.stepValue;
            fnSelectChangeTrigger(projectConfigureBuildType);
        } else if (step.stepType === 'DEPLOY') {
            projectConfigureCredential.dataset.stepId = step.id;
            projectConfigureCredential.value = step.stepValue;
            fnSelectChangeTrigger(projectConfigureCredential);
        }
    });
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