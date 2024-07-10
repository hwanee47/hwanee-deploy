
const btnSaveProjectBuildFile = document.getElementById('btnSaveProjectBuildFile');

btnSaveProjectBuildFile.addEventListener('click', function() {
    fnSaveProjectBuildFile($('#projectBuildFile-id').val());
})


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
}


class CustomColumnRenderer {
    constructor(props) {
        const el = document.createElement('span');
        el.className = 'text-xs text-blue-700 p-2';
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