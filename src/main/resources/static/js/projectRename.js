const btnSaveProjectRename = document.getElementById('btnSaveProjectRename');

btnSaveProjectRename.addEventListener('click', function() {
    fnSaveProjectRename($('#project-id').val());
})

// ProjectRename 저장
const fnSaveProjectRename = (id) => {
    _axios
        .put(`/api/job/${id}`, {
            'name': $('#projectRename-name').val(),
            'description': $('#projectRename-description').val()
        })
        .then(function (response) {
            window.location.href = "/app/view/project/project-dashboard?id="+id;

            gfnSetToastInLocalStorage('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}

const fnSetProjectRenameInfo = (data) => {
    $('#projectRename-name').val(data.name);
    $('#projectRename-description').val(data.description);
}