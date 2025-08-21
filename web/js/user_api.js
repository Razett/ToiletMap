var UserApiService = (function (){

    const _api_url = "http://localhost:8080/api";

    function validateEmail(email) {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        return emailRegex.test(email);
    }

    function availableEmailAjax(email, callback, err) {
        $.ajax({
            method: 'POST',
            url: _api_url + '/user/check/email',
            contentType: 'application/json',
            data: JSON.stringify(email),
            success: function (data) {
                if (callback) {
                    callback(data);
                }
            },
            error: function (xhr, status, error) {
                if (err) {
                    err(status, error);
                }
            }
        });
    }

    function sendVerificationCodeAjax(email, callback, err) {
        $.ajax({
            method: 'POST',
            url: _api_url + '/user/sendcode',
            contentType: 'application/json',
            data: JSON.stringify(email),
            success: function (data) {
                if (callback) {
                    callback(data);
                }
            },
            error: function (xhr, status, error) {
                if (err) {
                    err(status, error);
                }
            }
        });
    }

    function verifyCodeAjax(email, code, callback, err) {
        $.ajax({
            method: 'POST',
            url: _api_url + '/user/verifycode',
            contentType: 'application/json',
            data: JSON.stringify({'code': code, 'email': email}),
            success: function (data) {
                if (callback) {
                    callback(data);
                }
            },
            error: function (xhr, status, error) {
                if (err) {
                    err(status, error);
                }
            }
        });
    }

    let countdown; // 변수로 타이머를 설정해 놓아야 중지나 재설정이 가능함

    function startTimer(display, callback) {
        let timeRemaining = 5 * 60; // 5분을 초로 변환 (5분 * 60초 = 300초)
        clearInterval(countdown); // 타이머를 시작할 때 기존 타이머 중지

        countdown = setInterval(function () {
            const minutes = Math.floor(timeRemaining / 60);
            const seconds = timeRemaining % 60;

            // 두 자리 형식으로 만들기
            const formattedMinutes = minutes.toString().padStart(2, '0');
            const formattedSeconds = seconds.toString().padStart(2, '0');

            // 화면에 표시
            display.innerText = `${formattedMinutes}:${formattedSeconds}`;

            timeRemaining--;

            // 시간이 다 되면 타이머 중지
            if (timeRemaining < 0) {
                if (callback) {
                    clearInterval(countdown);
                    callback();
                }
            }
        }, 1000); // 1초마다 업데이트
    }

    function stopTimer(display, callback) {
        clearInterval(countdown);
    }

    return {
        validateEmail:validateEmail,
        availableEmailAjax:availableEmailAjax,
        sendVerificationCodeAjax:sendVerificationCodeAjax,
        verifyCodeAjax:verifyCodeAjax,
        startTimer:startTimer,
        stopTimer:stopTimer
    };
})();