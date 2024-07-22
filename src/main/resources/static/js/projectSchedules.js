const btnAddSchedule = document.getElementById('btnAddSchedule');

btnAddSchedule.addEventListener('click', function() {
    window.location.href = `/app/view/project/project-schedule-add?id=${$('#project-id').val()}`;
})

const initGridSchedule = (projectId) => {
    const grid = new tui.Grid({
        el: document.getElementById('gridSchedule'),
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
                name: 'createdAt', header: '등록일시', width: 180,
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'scheduleTime', header: '예정일시', width: 180,
                renderer: {
                    type: CustomColumnRenderer
                }
            },
            {
                name: 'isSuccess', header: '실행여부', width: 100,
                renderer: {
                    type: CustomIsSuccessRenderer,
                }
            },
            {
                name: 'description', header: '비고',
                renderer: {
                    type: CustomColumnRenderer
                }
            },

        ],
        contextMenu: ({ rowKey, columnName }) => [
            [
                {
                    name: 'actions',
                    label: 'Actions',
                    subMenu: [
                        {
                            name: 'edit',
                            label: 'Edit',
                            action: () => {
                                let id = grid.getValue(rowKey, 'id');
                                window.location.href = `/app/view/project/project-schedule-edit?id=${$('#project-id').val()}&scheduleId=${id}`;

                            },
                        },
                        {
                            name: 'delete',
                            label: 'Delete',
                            action: () => {
                                console.log("delete")
                            },
                        },
                    ],
                },
            ],
        ],
        data: {
            contentType: 'application/json',
            api: {
                readData: {url: `/api/schedule/project/${projectId}`, method: 'GET'}
            }
        },
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
        const executedTime = rowData.executedTime;

        if (executedTime) {
            el.className = 'bg-blue-400 text-xs text-white font-medium me-2 px-3 py-1 rounded-full dark:bg-blue-900 dark:text-blue-300 ml-3';
            el.textContent = "실행완료";
        } else {
            el.className = 'bg-gray-500 text-xs text-white font-medium me-2 px-3 py-1 rounded-full dark:bg-gray-900 dark:text-gray-300 ml-3';
            el.textContent = "예정";
        }

        this.el = el;
    }

    getElement() {
        return this.el;
    }

    render(props) {

    }
}
