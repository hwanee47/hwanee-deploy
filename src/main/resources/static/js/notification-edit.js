const btnCancelNotification = document.getElementById('btnCancelNotification');
const btnUpdateNotification = document.getElementById('btnUpdateNotification');
const btnDeleteNotification = document.getElementById('btnDeleteNotification');

btnCancelNotification.addEventListener('click', function() {
    window.location.href = "/app/view/settings/notifications";
})

btnUpdateNotification.addEventListener('click', function() {
    fnUpdateNotification($('#notification-id').val());
})

btnDeleteNotification.addEventListener('click', function() {
    gfnOpenModal("정말로 삭제하시겠습니까?", function() {
        fnDeleteNotification($('#notification-id').val());
    })
})


// Notification 조회
const fnSearchNotification = (id) => {
    _axios
        .get(`/api/notification/${id}`, {
        })
        .then(function (response) {
            fnSetNotificationInfo(response.data.result);
        })
        .catch(function (error) {

        });
}

// Notification 정보 셋팅
const fnSetNotificationInfo = (data) => {
    $('#notification-type').val(data.type);
    $('#notification-url').val(data.url);
    $('#notification-apiKey').val(data.apiKey);
}

// Notification 수정
const fnUpdateNotification = (id) => {
    _axios
        .put(`/api/notification/${id}`, {
            'type': $('#notification-type').val(),
            'url': $('#notification-url').val(),
            'apiKey': $('#notification-apiKey').val()
        })
        .then(function (response) {
            window.location.href = "/app/view/settings/notifications";

            gfnSetToastInLocalStorage('success', '수정이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}


// Notification 삭제
const fnDeleteNotification = (id) => {
    _axios
        .delete(`/api/notification/${id}`)
        .then(function (response) {
            window.location.href = "/app/view/settings/notifications";

            gfnSetToastInLocalStorage('success', '삭제가 완료되었습니다.');
        })
        .catch(function (error) {

        });
}