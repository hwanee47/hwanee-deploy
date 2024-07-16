const btnSaveMyProfile = document.getElementById('btnSaveMyProfile');
const btnChangePassword = document.getElementById('btnChangePassword');

btnSaveMyProfile.addEventListener('click', function() {

    let userId = sessionStorage.getItem('userId');

    // 사용자 정보 변경
    _axios
        .put(`/api/user/${userId}`, {
            'username': $('#profile-username').val(),
        })
        .then(function (response) {
            gfnShowToast('success', '저장이 완료되었습니다.');
            gfnGetSession();
        })
        .catch(function (error) {

        });
})

btnChangePassword.addEventListener('click' , function() {

    // 비밀번호 변경
    _axios
        .put('/api/user/password', {
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

/**
 * 내 정보 조회
 * @param userId
 */
const fnSearchMyInfo = (userId) => {
    _axios
        .get(`/api/user/${userId}`)
        .then(function (response) {
            console.log(response);
            $('#profile-email').val(response.data.result.email);
            $('#profile-username').val(response.data.result.username);
        })
        .catch(function (error) {

        });
}