const btnCancelCredential = document.getElementById('btnCancelCredential');
const btnUpdateCredential = document.getElementById('btnUpdateCredential');
const btnDeleteCredential = document.getElementById('btnDeleteCredential');

btnCancelCredential.addEventListener('click', function() {
    window.location.href = "/app/view/settings/credentials";
})

btnUpdateCredential.addEventListener('click', function() {
    fnUpdateCredential($('#credential-id').val());
})

btnDeleteCredential.addEventListener('click', function() {
    gfnOpenModal("정말로 삭제하시겠습니까?", function() {
        fnDeleteCredential($('#credential-id').val());
    })
})


// Credential 조회
const fnSearchCredential = (id) => {
    _axios
        .get(`/api/credential/${id}`, {
        })
        .then(function (response) {
            fnSetCredentialInfo(response.data.result);
        })
        .catch(function (error) {

        });
}

// Credential 정보 셋팅
const fnSetCredentialInfo = (data) => {
    $('#credential-identifierName').val(data.name);
    $('#credential-username').val(data.username);
    $('#credential-password').val(data.password);
    $('#credential-host').val(data.host);
    $('#credential-port').val(data.port);
    $('#credential-privateKey').val(data.privateKey);
    $('#credential-passphrase').val(data.passphrase);
    $('#credential-description').val(data.description);
}

// Credential 수정
const fnUpdateCredential = (id) => {
    _axios
        .put(`/api/credential/${id}`, {
            'name': $('#credential-identifierName').val(),
            'username': $('#credential-username').val(),
            'password': $('#credential-password').val(),
            'host': $('#credential-host').val(),
            'port': $('#credential-port').val(),
            'privateKey': $('#credential-privateKey').val(),
            'passphrase': $('#credential-passphrase').val(),
            'description': $('#credential-description').val()
        })
        .then(function (response) {
            window.location.href = "/app/view/settings/credentials";

            gfnSetToastInLocalStorage('success', '수정이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}


// Credential 삭제
const fnDeleteCredential = (id) => {
    _axios
        .delete(`/api/credential/${id}`)
        .then(function (response) {
            window.location.href = "/app/view/settings/credentials";

            gfnSetToastInLocalStorage('success', '삭제가 완료되었습니다.');
        })
        .catch(function (error) {

        });
}