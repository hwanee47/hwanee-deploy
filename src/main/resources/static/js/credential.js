const btnCancelCredential = document.getElementById('btnCancelCredential');
const btnSaveCredential = document.getElementById('btnSaveCredential');

btnCancelCredential.addEventListener('click', function() {
    window.location.href = "/app/view/settings/credentials";
})

btnSaveCredential.addEventListener('click', function() {
    fnSaveCredential();
})


// Credential 저장
const fnSaveCredential = () => {
    _axios
        .post('/api/credential', {
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

            gfnSetToastInLocalStorage('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}
