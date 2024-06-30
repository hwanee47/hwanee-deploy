const btnChangePassword = document.getElementById('btnChangePassword');

btnChangePassword.addEventListener('click' , function() {

    // 비밀번호 변경
    _axios
        .put('/api/password', {
            'userId': sessionStorage.getItem('userId'),
            'currentPassword': $('#currentPassword').val(),
            'newPassword': $('#newPassword').val(),
            'newPasswordConfirm': $('#newPasswordConfirm').val()
        })
        .then(function (response) {
            gfnShowToast('success', '비밀번호 변경이 완료되었습니다.');
        })
        .catch(function (error) {

        });
});