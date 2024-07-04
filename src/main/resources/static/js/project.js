

// Project 조회
const fnSearchProject = (id) => {
    return _axios
        .get(`/api/job/${id}`)
        .then(function (response) {
            return response.data.result;
        })
        .catch(function (error) {

        });

}

const fnSetProjectInfo = (data) => {
    $('#project-name').text(`[ ${data.name} ]`);
}
