const btnCancelScmConfig = document.getElementById('btnCancelScmConfig');
const btnUpdateScmConfig = document.getElementById('btnUpdateScmConfig');
const btnDeleteScmConfig = document.getElementById('btnDeleteScmConfig');
const btnHealthCheck = document.getElementById('btnHealthCheck');

btnHealthCheck.addEventListener('click', function() {
    fnHealthCheck();
})

btnCancelScmConfig.addEventListener('click', function() {
    window.location.href = "/app/view/settings/scmConfigs";
})

btnUpdateScmConfig.addEventListener('click', function() {
    fnUpdateScmConfig($('#scmConfig-id').val());
})

btnDeleteScmConfig.addEventListener('click', function() {
    gfnOpenModal("정말로 삭제하시겠습니까?", function() {
        fnDeleteScmConfig($('#scmConfig-id').val());
    })
})


// ScmConfig 조회
const fnSearchScmConfig = (id) => {
    _axios
        .get(`/api/scmConfig/${id}`, {
        })
        .then(function (response) {
            fnSetScmConfigInfo(response.data.result);
        })
        .catch(function (error) {

        });
}

// ScmConfig 정보 셋팅
const fnSetScmConfigInfo = (data) => {
    $('#scmConfig-type').val(data.type);
    $('#scmConfig-url').val(data.url);
    $('#scmConfig-username').val(data.username);
    $('#scmConfig-password').val(data.password);
    $('#scmConfig-branch').val(data.branch);
    $('#scmConfig-description').val(data.description);
}

// ScmConfig 수정
const fnUpdateScmConfig = (id) => {
    _axios
        .put(`/api/scmConfig/${id}`, {
            'type': $('#scmConfig-type').val(),
            'url': $('#scmConfig-url').val(),
            'username': $('#scmConfig-username').val(),
            'password': $('#scmConfig-password').val(),
            'branch': $('#scmConfig-branch').val(),
            'description': $('#scmConfig-description').val()
        })
        .then(function (response) {
            window.location.href = "/app/view/settings/scmConfigs";

            gfnSetToastInLocalStorage('success', '수정이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}


// ScmConfig 삭제
const fnDeleteScmConfig = (id) => {
    _axios
        .delete(`/api/scmConfig/${id}`)
        .then(function (response) {
            window.location.href = "/app/view/settings/scmConfigs";

            gfnSetToastInLocalStorage('success', '삭제가 완료되었습니다.');
        })
        .catch(function (error) {

        });
}


const fnHealthCheck = () => {
    _axios
        .post(`/api/scmConfig/healthCheck`, {
            'type': $('#scmConfig-type').val(),
            'url': $('#scmConfig-url').val(),
            'username': $('#scmConfig-username').val(),
            'password': $('#scmConfig-password').val(),
            'branch': $('#scmConfig-branch').val()
        })
        .then(function (response) {
            $('#healthCheckMessage').text(`"${response.data.result}"`);
        })
        .catch(function (error) {

        });
}