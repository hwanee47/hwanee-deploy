const btnAddScmConfig = document.getElementById('btnAddScmConfig');
const btnSearchScmConfig = document.getElementById('btnSearchScmConfig');


btnAddScmConfig.addEventListener('click', function() {
    window.location.href = "/app/view/settings/scmConfig";
})


class CustomConnectStatusRenderer {
    constructor(props) {
        const el = document.createElement('span');

        if (props.value) {
            el.className = 'bg-green-400 text-xs text-white font-medium me-2 px-5 py-1 rounded-full dark:bg-blue-900 dark:text-blue-300 ml-3';
            el.textContent = "success";
        } else {
            el.className = 'bg-red-400 text-xs text-white font-medium me-2 px-5 py-1 rounded-full dark:bg-red-900 dark:text-red-300 ml-3';
            el.textContent = "fail";
        }

        this.el = el;
    }

    getElement() {
        return this.el;
    }

    render(props) {

    }
}

