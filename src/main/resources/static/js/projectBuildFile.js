const btnSaveProjectBuildFile = document.getElementById('btnSaveProjectBuildFile');
const btnDownload = document.getElementById('btnDownload');
const btnDeploy = document.getElementById('btnDeploy');

btnDeploy.addEventListener('click', function() {
    fnDeployProjectBuildFile($('#projectBuildFile-id').val());
})

btnSaveProjectBuildFile.addEventListener('click', function() {
    fnSaveProjectBuildFile($('#projectBuildFile-id').val());
})

btnDownload.addEventListener('click', function() {
    fnDownloadBuildFile($('#projectBuildFile-id').val());
})



const fnDeployProjectBuildFile = (buildFileId) => {
    _axios
        .post(`/api/buildFile/deploy/${buildFileId}`)
        .then(function (response) {

        })
        .catch(function (error) {
        })
}


const fnDownloadBuildFile = (buildFileId) => {
    _axios
        .get(`/api/buildFile/download/${buildFileId}`, {
            responseType: 'blob'
        })
        .then(function (response) {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            const contentDisposition = response.headers['content-disposition'];
            let fileName = 'downloaded_file.jar';
            if (contentDisposition) {
                const fileNameMatch = contentDisposition.match(/filename="(.+)"/);
                if (fileNameMatch.length === 2) {
                    fileName = fileNameMatch[1];
                }
            }
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click();
            link.remove();
        })
        .catch(function (error) {
        })
}

const fnSaveProjectBuildFile = (buildFileId) => {
    _axios
        .put(`/api/buildFile/${buildFileId}`, {
            'description': $('#projectBuildFile-description').val()
        })
        .then(function (response) {
            gfnShowToast('success', '저장이 완료되었습니다.');
        })
        .catch(function (error) {

        });
}

const fnSearchBuildFile = (buildFileId) => {
    return _axios
        .get(`/api/buildFile/${buildFileId}`)
        .then(function (response) {
            return response.data.result;
        })
        .catch(function (error) {
        })
}

const initGridBuildFiles = (projectId) => {
    const grid = new tui.Grid({
        el: document.getElementById('gridVersionList'),
        scrollX: false,
        scrollY: false,
        rowHeaders: ['rowNum'],
        selectionUnit: 'row',
        minBodyHeight: 500,
        header: {
            align: 'left',
            height: 35,
        },
        pageOptions: {
            perPage: 10
        },
        columns: [
            {name: 'id', header: 'id', hidden: 1},
            {
                name: 'buildFileName', header: '빌드파일',
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'description', header: '변경사항', width: 120,
                renderer: {
                    type: CustomDescriptionRenderer
                }
            }
        ],
        data: {
            contentType: 'application/json',
            api: {
                readData: { url: `/api/buildFile/list/${projectId}`, method: 'GET' }
            }
        },
    });


    grid.on('focusChange', ({ rowKey }) => {
        grid.setSelectionRange({ start: [rowKey, 0], end: [rowKey, 0]});
        let buildFileId = grid.getValue(rowKey, 'id');

        fnSearchBuildFile(buildFileId)
            .then(data => {
                fnSetBuildFileInfo(data);
            })

    });
}


const fnSetBuildFileInfo = (data) => {
    $('#projectBuildFile-id').val(data.id);
    $('#projectBuildFile-buildFileName').val(data.buildFileName);
    $('#projectBuildFile-description').val(data.description);
    $('#btnDeploy').removeClass('hidden');
    $('#btnCancelProjectBuildFile').removeClass('hidden');
    $('#btnSaveProjectBuildFile').removeClass('hidden');
    $('#btnDownload').removeClass('hidden');
}


class CustomColumnRenderer {
    constructor(props) {
        const el = document.createElement('span');
        el.className = 'text-xs text-blue-700 pl-3';
        if (props.columnInfo.name === "seq") {
            el.textContent = "# " + props.value;
        } else {
            el.textContent = props.value;
        }


        this.el = el;
    }

    getElement() {
        return this.el;
    }

    render(props) {

    }
}


class CustomDescriptionRenderer {
    constructor(props) {
        const el = document.createElement('span');

        console.log(props.value);

        if (props.value) {
            el.className = 'bg-blue-400 text-xs text-white font-medium me-2 px-3 py-1 rounded-full dark:bg-blue-900 dark:text-blue-300 ml-3';
            el.textContent = "작성완료";
        } else {
            el.className = 'bg-gray-500 text-xs text-white font-medium me-2 px-3 py-1 rounded-full dark:bg-gray-900 dark:text-gray-300 ml-3';
            el.textContent = "미작성";
        }

        this.el = el;
    }

    getElement() {
        return this.el;
    }

    render(props) {

    }
}