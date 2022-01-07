(() => {
    Kakao.init("301666567c882d68ede3425ac4a18848");
})();

function loginFormWithKakao() {
    if (!Kakao.isInitialized()) {
        alert("카카오 인스턴스가 존재하지 않습니다.");
        return;
    }

    Kakao.Auth.loginForm({
        success: function (authObj) {
            Kakao.Auth.login({
                scope: 'profile_nickname,account_email,gender',
                success: function (e) {
                    Kakao.API.request({
                        url: '/v2/user/me',
                        success: function (res) {
                            fetch("/member/kakao-login/" + res.id)
                                .then((response) => {
                                    if (response.ok) {
                                        return response.json();
                                    } else {
                                        throw new Error(response.status);
                                    }
                                })
                                .then(res => {
                                    if (res.userId == null) {
                                        alert("회원가입되어 있지 않은 카카오계정입니다.\n회원가입페이지로 이동합니다." +
                                            "\n회원가입 후 자동으로 카카오 연동이 됩니다.");
                                        location.href="/member/join-rule?kakao=" + res.kakaoJoin;
                                    } else {
                                        fetch("/member/kakao-login-impl",
                                            {
                                                method: 'POST',
                                                body: JSON.stringify(res)
                                            })
                                    }
                                })

                        }, fail: function (error) {
                            alert('login success, but failed to request user information: '
                                + JSON.stringify(error))
                        }
                    })
                }, fail: function (error) {
                    console.dir(error)
                },

            })

        }, fail: function (err) {
            console.dir(JSON.stringify(err));
        },
    })
}