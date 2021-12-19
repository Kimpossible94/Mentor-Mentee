Kakao.init("0f53e9a65e6dce062add32c866c84d39");
		function loginFormWithKakao() {
			Kakao.Auth.loginForm({
						success : function(authObj) {
							Kakao.Auth.login({
										scope : 'profile_nickname,account_email,gender',
										success : function(e) {
											Kakao.API.request({
														url : '/v2/user/me',
														success : function(res) {
															fetch("/member/test?kakao="+res.id+"&email="+ res.kakao_account.email + "&gender=" + res.kakao_account.gender+ "&name=" + res.kakao_account.profile.nickname)
															.then((response)=>{
																if(response.ok){
																	return response.text()
																}else{
																	throw new Error(response.status);
																}
															}).then(text =>{
																 if(text == 'notMember'){
														              location.href = "/member/join-rule"
														            }else{
														            	 location.href = "/member/kakao-login?userIdx="+ text
														              
														            }
															})
															
														},fail : function(error) {
															alert('login success, but failed to request user information: '
																	+ JSON.stringify(error))
														}
													})
										},fail : function(error) {
											console.dir(error)
										},

									})

						},fail : function(err) {
							showResult(JSON.stringify(err))
						},
					})
		}