let datepicker;
const btnSaveProjectScheduleAdd = document.getElementById('btnSaveProjectScheduleAdd');
const btnCancelProjectScheduleAdd = document.getElementById('btnCancelProjectScheduleAdd');

btnSaveProjectScheduleAdd.addEventListener('click', function() {
    fnSaveSchedule();
})

btnCancelProjectScheduleAdd.addEventListener('click', function() {
    window.location.href = `/app/view/project/project-schedule?id=${$('#project-id').val()}`;
})

const fnInitScheduleAdd = () => {
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


const formatDate = (date) => {
    let year = date.getFullYear();
    let month = ('0' + (date.getMonth() + 1)).slice(-2);
    let day = ('0' + date.getDate()).slice(-2);
    let hours = ('0' + date.getHours()).slice(-2);
    let minutes = ('0' + date.getMinutes()).slice(-2);
    let seconds = ('0' + date.getSeconds()).slice(-2);
    return year + '-' + month + '-' + day + 'T' + hours + ':' + minutes + ':' + '00';
}


const fnSaveSchedule = () => {
    _axios
        .post('/api/schedule', {
            'jobId': $('#project-id').val(),
            'time': formatDate(datepicker.getDate()),
            'description': $('#projectScheduleAdd-description').val()
        })
        .then(function (response) {
            window.location.href = `/app/view/project/project-schedule?id=${$('#project-id').val()}`;

            gfnSetToastInLocalStorage('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}
