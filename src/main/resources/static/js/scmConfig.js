const btnCancelScmConfig = document.getElementById('btnCancelScmConfig');
const btnSaveScmConfig = document.getElementById('btnSaveScmConfig');

btnCancelScmConfig.addEventListener('click', function() {
    window.location.href = "/app/view/settings/scmConfigs";
})

btnSaveScmConfig.addEventListener('click', function() {
    fnSaveScmConfig();
})


// SCM Config 저장
const fnSaveScmConfig = () => {
    _axios
        .post('/api/scmConfig', {
            'type': $('#scmConfig-type').val(),
            'url': $('#scmConfig-url').val(),
            'username': $('#scmConfig-username').val(),
            'password': $('#scmConfig-password').val(),
            'branch': $('#scmConfig-branch').val(),
            'description': $('#scmConfig-description').val()
        })
        .then(function (response) {
            window.location.href = "/app/view/settings/scmConfigs";

            gfnSetToastInLocalStorage('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}
