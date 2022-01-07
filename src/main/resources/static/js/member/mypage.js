(()=>{
    Kakao.init("301666567c882d68ede3425ac4a18848");
})();

let cnt = 1;
let div = document.createElement('div');
let addHist = () => {


    let input = document.createElement('input');


    div.className = "hsDiv";
    input.className = "form-control mt-2";
    input.id = "history" + cnt;
    input.name = "history";
    input.placeholder = "이력사항을 적어주세요";
    div.append(input);

    if (cnt == 1) {
        document.querySelector('#histories').append(div)
    }
    cnt++;
}

document.querySelector('#resetBtn').addEventListener('click', (e) => {
    div.innerHTML = "";
})



function loginFormWithKakao() {
    if(!Kakao.isInitialized()){
        alert("카카오 인스턴스가 존재하지 않습니다.");
        return;
    }

    var type = document.getElementById('memberRole').value;

    Kakao.Auth.loginForm({
        success: function (authObj) {
            Kakao.Auth.login({
                scope: 'profile_nickname,account_email,gender',
                success: function (e) {
                    console.dir("aaaaa" + e);
                    Kakao.API.request({
                        url: '/v2/user/me',
                        success: function (res) {
                            console.dir(res);
                            fetch("/member/kakao-auth?kakao=" + res.id + "&type=" + type)
                                .then((response) => {
                                    if (response.ok) {
                                        return response.text()
                                    } else {
                                        throw new Error(response.status);
                                    }
                                })
                                .then(text => {
                                    if (text == "available") {
                                        alert("연동이 완료되었습니다.")
                                    } else {
                                        alert("이미 등록된 계정입니다.")
                                    }
                                    location.href = "/member/mypage"
                                })
                        },
                        fail: function (error) {
                            alert('login success, but failed to request user information: ' + JSON.stringify(error))
                        }
                    })
                },
                fail: function (error) {
                    console.dir(error);
                },
                })
        },
        fail: function (err) {
            console.dir(JSON.stringify(err));
        },
    })
}
		
		
		
