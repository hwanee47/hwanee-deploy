const btnSaveNewProject = document.getElementById('btnSaveNewProject');

btnSaveNewProject.addEventListener('click', function() {
    fnSaveNewProject();
})


// Project 저장
const fnSaveNewProject = () => {
    _axios
        .post('/api/job', {
            'name': $('#newProject-name').val(),
            'description': $('#newProject-description').val()
        })
        .then(function (response) {
            window.location.href = "/app/view/project/myProject";

            gfnSetToastInLocalStorage('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}
