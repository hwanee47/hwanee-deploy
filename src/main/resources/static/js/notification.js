const btnCancelNotification = document.getElementById('btnCancelNotification');
const btnSaveNotification = document.getElementById('btnSaveNotification');

btnCancelNotification.addEventListener('click', function() {
    window.location.href = "/app/view/settings/notifications";
})

btnSaveNotification.addEventListener('click', function() {
    fnSaveNotification();
})


// Notification 저장
const fnSaveNotification = () => {
    _axios
        .post('/api/notification', {
            'type': $('#notification-type').val(),
            'url': $('#notification-url').val(),
            'apiKey': $('#notification-apiKey').val()
        })
        .then(function (response) {
            window.location.href = "/app/view/settings/notifications";

            gfnSetToastInLocalStorage('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}
