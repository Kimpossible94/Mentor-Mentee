
 (() => {
      let confirmId = '';
		document.querySelector('.checkIdBtn').addEventListener('click',function(){
         
         let userId = document.querySelector('#userId').value;
         
         if(!userId){
            document.querySelector('#idCheck').innerHTML = '아이디를 입력하지 않았습니다.';
            return;
         }
         
         fetch("/member/id-check?userId=" + userId)
         .then(response =>{
			if(response.ok){
				return response.text()
			}else{
				throw new Error(response.status);
			}
		})
         .then(text => {
            if(text == 'available'){
               confirmId = userId;
               document.querySelector('#idCheck').innerHTML = '사용 가능한 아이디 입니다.';
            }else{
               
               document.querySelector('#idCheck').innerHTML = '사용 불가능한 아이디 입니다.';
            }
         })
		.catch(error=>{
			 document.querySelector('#idCheck').innerHTML = '응답에 실패했습니다. 상태코드 ' + error;
		})
      });
	
	


		/*document.querySelector('#frm_join').addEventListener('submit', function(e){
		 let userId = document.querySelector('#userId').value;
         let password = document.querySelector('#password').value;
		 let rePassword = document.querySelector('#passwordConfirmation').value;
         let tell = document.querySelector('#phone').value;
         
         let pwReg = /(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9])(?=.{8,})/;
         let tellReg = /^\d{6,8}$/;
         
         if(confirmId != userId){
            document.querySelector('#idCheck').innerHTML = '아이디 중복 검사를 하지 않았습니다.';
            document.querySelector('#userId').focus();
            e.preventDefault();
         }

		if(rePassword != password){
			document.querySelector('#pwCheck').innerHTML = '비밀번호가 일치하지 않습니다.';
			document.querySelector('#passwordConfirmation').focus();
			e.preventDefault();
			
		}
         
        if(!pwReg.test(password)){
            document.querySelector('#pwCheck').innerHTML = '비밀번호는 숫자, 영문자, 특수문자 조합의 8글자 이상인 문자열 입니다.';
            e.preventDefault();
         }
         
         if(!tellReg.test(tell)){
            document.querySelector('#tellCheck').innerHTML = '전화번호는 9~11자리의 숫자입니다.';
            e.preventDefault();
         }
      })*/
		
 })();

	let cnt = 1;
		document.querySelector('#addHistoryBtn').addEventListener('click', (e) =>{
			let input = document.createElement('input');
		
			
				
				input.className = "form-control bg-white border-md";
				input.id = "history"+cnt;
				input.name = "history";
				input.placeholder="이력사항을 적어주세요";
				document.querySelector('#historyArea').append(document.createElement('br'))
				document.querySelector('#historyArea').append(input)
					
				
				cnt++;
				
		
			
		
			
			
		})