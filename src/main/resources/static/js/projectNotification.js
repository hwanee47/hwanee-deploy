const projectNotificationNotification = document.getElementById('projectNotification-notification');
const btnSaveProjectNotification = document.getElementById('btnSaveProjectNotification');

btnSaveProjectNotification.addEventListener('click', function() {
    fnSaveProjectNotification($('#project-id').val());
})

const fnSaveProjectNotification = (projectId) => {
    _axios
        .put(`/api/job/${projectId}/notification`, {
            'notificationId': $('#projectNotification-notification').val(),
        })
        .then(function (response) {
            gfnShowToast('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}

// Notifications 조회
const fnGetNotifications = () => {
    return _axios
        .get(`/api/notification/searchAll`)
        .then(function (response) {
            return response.data.result;
        })
        .catch(function (error) {
        })
}

// 데이터셋팅
const fnSetNotifications = (data) => {

    let selectElement = projectNotificationNotification;

    selectElement.innerHTML = '';

    const emptyOptionElement = document.createElement('option');
    selectElement.appendChild(emptyOptionElement);

    data.forEach(config => {
        const optionElement = document.createElement('option');
        optionElement.value = config.id;
        optionElement.textContent = `(${config.type}) ${config.url}`;
        optionElement.dataset.type = config.type;
        optionElement.dataset.url = config.url;
        selectElement.appendChild(optionElement);
    })

}


const fnSetNotification = (notificationId) => {
    projectNotificationNotification.value = notificationId;
}
