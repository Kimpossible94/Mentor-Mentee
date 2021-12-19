(() =>{
	
	if(document.querySelector('#security').baseURI.endsWith('err=1')){
		document.querySelector('#profile').className = "tab-pane";
		document.querySelector('#security').className += "active";
	}
	
	
	
})();


	let cnt = 1;
	let div = document.createElement('div');
	let addHist =  () =>{
		
		
		
		
		let input = document.createElement('input');

		
			div.className = "hsDiv";
			input.className = "form-control mt-2";
			input.id = "history"+cnt;
			input.name = "history";
			input.placeholder="이력사항을 적어주세요";
			div.append(input);
			
			if(cnt == 1){
				document.querySelector('#histories').append(div)
			}
			
			
				
			
			cnt++;
			
		
	}


	document.querySelector('#resetBtn').addEventListener('click', (e) =>{
		div.innerHTML = "";
	})




		Kakao.init("0f53e9a65e6dce062add32c866c84d39");
		function loginFormWithKakao() {
			Kakao.Auth
					.loginForm({
						success : function(authObj) {
							Kakao.Auth
									.login({
										scope : 'profile_nickname,account_email,gender',
										success : function(e) {
											console.dir(e)
											Kakao.API
													.request({

														url : '/v2/user/me',
														success : function(res) {
															fetch("/member/kakao-auth?kakao="+res.id)
															.then((response)=>{
																if(response.ok){
																	return response.text()
																	
																}else{
																	throw new Error(response.status);
																}
															}).then(text =>{
																if(text == "available"){
																	alert("연동이 완료되었습니다.")
																}else{
																	alert("이미 등록된 계정입니다.")
																}
																
																location.href="/member/mypage"
															})
															
														},
														fail : function(error) {
															alert('login success, but failed to request user information: '
																	+ JSON.stringify(error))
														}
													})
										},
										fail : function(error) {
											console.dir(error)
										},

									})

						},
						fail : function(err) {
							showResult(JSON.stringify(err))
						},
					})
		}
		
		
		
