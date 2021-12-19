checkByte = (obj) => {
	
	const maxByte = 100; //최대 100바이트
	const textVal = obj.value; //입력한 문자
	const textLen = textVal.length; //입력한 문자수
	
	let cutEndPoint = 0;
	let totalByte = 0;
	
	for(let i = 0; i < textLen; i++){
		const eachChar = textVal.charAt(i);
	    const uniChar = escape(eachChar); //유니코드 형식으로 변환
	    if(uniChar.length>4){
	    	// 한글 : 2Byte
	        totalByte += 2;
	    }else{
	    	// 영문,숫자,특수문자 : 1Byte
	        totalByte += 1;
	    }

		if(totalByte <= 100){
			cutEndPoint++;
		}
	}

	if(totalByte>maxByte){
	alert('최대 100자 까지만 입력가능합니다.');
		obj.value = textVal.slice(0, cutEndPoint);
    	document.getElementById("nowByte").innerText = totalByte;
        document.getElementById("nowByte").style.color = "red";
    }else{
    	document.getElementById("nowByte").innerText = totalByte;
        document.getElementById("nowByte").style.color = "green";
    }
}