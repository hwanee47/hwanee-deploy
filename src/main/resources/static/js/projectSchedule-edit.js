let datepicker;
const btnSaveProjectScheduleEdit = document.getElementById('btnSaveProjectScheduleEdit');
const btnCancelProjectScheduleEdit = document.getElementById('btnCancelProjectScheduleEdit');
const btnDeleteProjectScheduleEdit = document.getElementById('btnDeleteProjectScheduleEdit');

btnSaveProjectScheduleEdit.addEventListener('click', function() {
    fnUpdateSchedule();
})

btnCancelProjectScheduleEdit.addEventListener('click', function() {
    window.location.href = `/app/view/project/project-schedule?id=${$('#project-id').val()}`;
})

btnDeleteProjectScheduleEdit.addEventListener('click', function() {
    gfnOpenModal("정말로 삭제하시겠습니까?", function() {
        fnDeleteSchedule($('#schedule-id').val());
    })
})

const fnInitScheduleEdit = () => {
    datepicker = new tui.DatePicker('#wrapper', {
        language: 'ko',
        date: new Date(),
        input: {
            element: '#datepicker-input',
            format: 'yyyy-MM-dd HH:mm A',
        },
        timePicker: true
    });
}

const fnSearchSchedule = (id) => {
    _axios
        .get(`/api/schedule/${id}`, {
        })
        .then(function (response) {
            fnSetScheduleInfo(response.data.result);
        })
        .catch(function (error) {

        });
}

const fnSetScheduleInfo = (data) => {
    datepicker.setDate(parseDate(data.scheduleTime));
    $('#projectScheduleEdit-description').val(data.description);
}


const formatDate = (date) => {
    let year = date.getFullYear();
    let month = ('0' + (date.getMonth() + 1)).slice(-2);
    let day = ('0' + date.getDate()).slice(-2);
    let hours = ('0' + date.getHours()).slice(-2);
    let minutes = ('0' + date.getMinutes()).slice(-2);
    let seconds = ('0' + date.getSeconds()).slice(-2);
    return year + '-' + month + '-' + day + 'T' + hours + ':' + minutes + ':' + '00';
}

function parseDate(dateString) {
    var dateParts = dateString.split('T');
    var date = dateParts[0].split('-');
    var time = dateParts[1].split(':');
    return new Date(date[0], date[1] - 1, date[2], time[0], time[1], time[2]);
}


const fnUpdateSchedule = () => {
    _axios
        .put(`/api/schedule/${$('#schedule-id').val()}`, {
            'time': formatDate(datepicker.getDate()),
            'description': $('#projectScheduleEdit-description').val()
        })
        .then(function (response) {
            window.location.href = `/app/view/project/project-schedule?id=${$('#project-id').val()}`;

            gfnSetToastInLocalStorage('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}


const fnDeleteSchedule = (id) => {
    _axios
        .delete(`/api/schedule/${id}`)
        .then(function (response) {
            window.location.href = `/app/view/project/project-schedule?id=${$('#project-id').val()}`;

            gfnSetToastInLocalStorage('success', '삭제가 완료되었습니다.');
        })
        .catch(function (error) {

        });
}