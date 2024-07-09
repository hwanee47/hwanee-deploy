let gridHistoryDetail;

const fnSearchProjectRunHistory = (projectId) => {
    return _axios
        .get(`/api/runHistory/${projectId}`)
        .then(function (response) {
            return response.data.result;
        })
        .catch(function (error) {

        });
}

const fnSearchProjectRunHistoryDetail = (projectId, historyId) => {
    let perPage = gridHistoryDetail.getPagination()._options.perPage;
    let page = gridHistoryDetail.getPagination()._currentPage;

    return _axios
        .get(`/api/runHistory/${projectId}/${historyId}?perPage=${perPage}&page=${page}`)
        .then(function (response) {
            return response.data.data.contents;
        })
        .catch(function (error) {

        });
}


const initGridHistory = (projectId) => {
    const grid = new tui.Grid({
        el: document.getElementById('gridHistory'),
        scrollX: false,
        scrollY: false,
        // rowHeaders: ['rowNum'],
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
                name: 'seq', header: '차수', width: 30, align: 'center',
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'createdAt', header: '시작시간', width: 130,
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'totalRunTime', header: '소요시간', width: 70, align: 'center',
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'isSuccess', header: '결과',
                renderer: {
                    type: CustomIsSuccessRenderer,
                }
            },

        ],
        data: {
            contentType: 'application/json',
            api: {
                readData: { url: `/api/runHistory/${projectId}`, method: 'GET' }
            }
        },
    });


    grid.on('focusChange', ({ rowKey }) => {
        grid.setSelectionRange({ start: [rowKey, 0], end: [rowKey, 0]});
        let historyId = grid.getValue(rowKey, 'id');

        fnSearchProjectRunHistoryDetail(projectId, historyId)
            .then(data => {
                console.log(data);
                gridHistoryDetail.resetData(data);
            })



    });
}

const initGridHistoryDetail = (projectId, historyId) => {
    gridHistoryDetail = new tui.Grid({
        el: document.getElementById('gridHistoryDetail'),
        scrollX: false,
        scrollY: false,
        rowHeaders: ['rowNum'],
        selectionUnit: 'row',
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
                name: 'stepType', header: '유형', width: 70,
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'runTime', header: '소요시간', width: 70, align: 'center',
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'isSuccess', header: '결과', width: 100,
                renderer: {
                    type: CustomIsSuccessRenderer,
                }
            },
            {
                name: 'runFailLog', header: '실패로그',
                renderer: {
                    type: CustomColumnRenderer
                }
            },

        ],
        data: [],
    });




    gridHistoryDetail.on('focusChange', ({ rowKey }) => {
        gridHistoryDetail.setSelectionRange({ start: [rowKey, 0], end: [rowKey, 0]});

    });
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

class CustomIsSuccessRenderer {
    constructor(props) {
        const el = document.createElement('span');

        const rowData = props.grid.getRow(props.rowKey);
        const status = rowData.status;

        if (status === "ING") {
            el.className = 'bg-gray-500 text-xs text-white font-medium me-2 px-3 py-1 rounded-full dark:bg-gray-900 dark:text-gray-300 ml-3';
            el.textContent = "ing";
        } else {

            if (props.value === true) {
                el.className = 'bg-blue-400 text-xs text-white font-medium me-2 px-3 py-1 rounded-full dark:bg-blue-900 dark:text-blue-300 ml-3';
                el.textContent = "success";
            } else if (props.value === false) {
                el.className = 'bg-red-400 text-xs text-white font-medium me-2 px-3 py-1 rounded-full dark:bg-red-900 dark:text-red-300 ml-3';
                el.textContent = "fail";
            }

        }

        this.el = el;
    }

    getElement() {
        return this.el;
    }

    render(props) {

    }
}