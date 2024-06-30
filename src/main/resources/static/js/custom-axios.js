
(function(global) {
    // Create axios object
    const createAxios = () => {
        let axiosInstance = axios.create({
            baseURL: "",
            headers: {
                'Content-Type': 'application/json',
            }
        })

        // 요청 인터셉터 추가
        axiosInstance.interceptors.request.use(function (config) {
            // 요청을 보내기 전에 수행할 일
            // 예: 토큰 설정
            const token = sessionStorage.getItem('authToken');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        }, function (error) {
            // 요청 에러 처리를 여기서
            return Promise.reject(error);
        });

        // 응답 인터셉터 추가
        axiosInstance.interceptors.response.use(function (response) {
            // 응답 데이터를 가공
            return response;
        }, function (error) {
            // 응답 에러 처리를 여기서
            console.error('에러 발생:', error);

            let message = "";
            if (error.response.status == 401) {
                message = "권한이 없습니다."
            } else {
                message = error.response.data.message;
            }

            gfnShowToast('error', message);

            return Promise.reject(error);
        });

        return axiosInstance;

    }

    // 전역 변수로 할당
    global._axios = createAxios();
})(window);
