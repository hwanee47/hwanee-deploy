const navbarToggle = document.getElementById('navbarToggle'),
    navbar       = document.getElementById('navbar');

navbarToggle.addEventListener('click' , function(){
    if (navbar.classList.contains('md:hidden')) {
        navbar.classList.remove('md:hidden');
        navbar.classList.add('fadeIn');
    }else{
        var _classRemover =  function () {
            navbar.classList.remove('fadeIn');
            navbar.classList.add('fadeOut');
            console.log('removed');

        };

        var animate = async function(){
            await _classRemover();
            console.log('animated');

            setTimeout(function(){
                navbar.classList.add('md:hidden');
                navbar.classList.remove('fadeOut');
            }, 450);
        };

        animate();
    };

});


// check if the page have dropdwon menu
var dropdown = document.getElementsByClassName('dropdown');

if (dropdown.length >= 1) {

    for (let i = 0; i < dropdown.length; i++) {
        const item = dropdown[i];

        let menu,btn,overflow;

        item.addEventListener('click' , function(){

            for (let i = 0; i < this.children.length; i++) {
                const e = this.children[i];

                if (e.classList.contains('menu')) {
                    menu = e;
                }else if (e.classList.contains('menu-btn')) {
                    btn = e;
                }else if (e.classList.contains('menu-overflow')) {
                    overflow = e;
                }

            }

            if (menu.classList.contains('hidden')) {
                // show the menu
                showMenu();
            }else{
                // hide the menu
                hideMenu()
            }


        });


        let showMenu = function(){
            menu.classList.remove('hidden');
            menu.classList.add('fadeIn');
            overflow.classList.remove('hidden');
        };

        let hideMenu = function(){
            menu.classList.add('hidden');
            overflow.classList.add('hidden');
            menu.classList.remove('fadeIn');
        };


    }

};


// =================== //

const navbarLogo = document.getElementById('navbar-logo');
navbarLogo.addEventListener('click' , function(){
    window.location.href = "/"
});

// =================== //

const gfnGetSession = () => {

    _axios
        .get('/api/get-session-info')
        .then(function (response) {
            let usernameElement = document.getElementById('navbar-username');
            usernameElement.innerText = response.data.result.username;
            sessionStorage.setItem("userId", response.data.result.userId);
        })
        .catch(function (error) {
            console.error('에러 발생:', error);
        });
}





