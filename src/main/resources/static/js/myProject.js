// project card container click event
document.querySelectorAll('.project-container').forEach(function (project) {
    project.addEventListener('click', function () {
        let projectId = this.getAttribute('data-id');
        window.location.href = "/app/view/project/project?id="+projectId;
    })
})
