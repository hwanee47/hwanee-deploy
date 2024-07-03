

// Project 조회
const fnSearchProject = (id) => {
    _axios
        .get(`/api/job/${id}`)
        .then(function (response) {
            fnSetProjectInfo(response.data.result);
        })
        .catch(function (error) {

        });
}

const fnSetProjectInfo = (data) => {
    $('#project-name').text(`[ ${data.name} ]`);
}
