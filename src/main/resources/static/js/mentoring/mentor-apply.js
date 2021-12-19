let renderDate = new Date();

let params = [];

let renderCalender = () => {
	
	let year = renderDate.getFullYear();
	let month = renderDate.getMonth()+1;
	
	document.querySelector(".year").innerHTML = `${year}년 `;
	document.querySelector(".today_month").innerHTML = `${month}월`;
	
	//지난달 마지막 날짜 기준으로 Date객체 생성
	let prevLast = new Date(year, month - 1, 0);
	//이번달 마지막 날짜 기준으로 Date객체 생성
	let thisLast = new Date(year, month, 0);
	
	//지난달 마지막날
	let plDate = prevLast.getDate();
	//이번달 마지막날
	let tlDate = thisLast.getDate();
	
	//지난달 마지막 요일
	let plDay = prevLast.getDay();
	//이번달 마지막 요일
	let tlDay = thisLast.getDay();
	
	let prevDates = [];
	let thisDates = [];
	let nextDates = [];
	
	if(plDay !== 6){
		for(let i = 0; i < plDay + 1; i++){
			prevDates.unshift(plDate - i);
		}
	}
	
	for(let i = 1; i < tlDate+1; i ++){
		thisDates.push(i);
	}
	
	for (let i = 1; i < 7 - tlDay; i++) {
  		nextDates.push(i);
	}
	
	let dates = prevDates.concat(thisDates, nextDates);
	
	let firstDateIndex = dates.indexOf(1);
	
  	let lastDateIndex = dates.lastIndexOf(tlDate);

	dates.forEach((date, i) => {
		let dateClass = i >= firstDateIndex && i < lastDateIndex + 1 ? 'now_month' : 'other_month';
		dates[i] = `<div class="date"><span class="${dateClass}">${date}</span></div>`;
	})
	
	document.querySelector('.dates').innerHTML = dates.join('');
}
	
renderCalender();

let prevMonth = () => {
	renderDate.setMonth(renderDate.getMonth()-1);
	renderCalender();
}
	
let nextMonth = () => {
	renderDate.setMonth(renderDate.getMonth()+1);
	renderCalender();
}


let deleteParams = () => {
	
}

let prevBtn = () => {
		
	if(document.getElementById("apply-page-2").style.display == 'flex'){
		document.getElementById("apply-page-1").style.display = 'flex';
		document.getElementById("apply-page-2").style.display = 'none';
		let firstParams = deleteParams(1);
		for(i = 0; i < 2; i++){
			params.pop()
		}
	} else {
		document.getElementById("apply-page-2").style.display = 'flex';
		document.getElementById("apply-page-3").style.display = 'none';
		for(i = 0; i < 2; i++){
			params.pop()
		}
	}
}

let registParams = (pageNum) => {
	let selectedSingle = [];
	let selectedMultiple = [];
	let firstChoice = [];
	let multipleChoice  = [];
	
	if(pageNum == 1){
		selectedSingle = document.getElementsByName("school_type");
		selectedMultiple = document.getElementsByName("major_type");
	} else if(pageNum == 2){
		selectedSingle = document.getElementsByName("want_time");
		selectedMultiple = document.getElementsByName("want_date");
	} else if(pageNum == 3){
		selectedSingle = document.getElementsByName("want_place");
	}
	
	//첫번째 선택항목에서 value값이 all인 항목에 체크되어있다면 모든 항목의 값을 넣어줌
	if(selectedSingle[0].checked){
		for(i = 0; i < selectedSingle.length; i++){
			firstChoice.push(selectedSingle[i].value);
		}
	} else {
		//아니라면 체크된 항목만 넣어줌
		for(i = 0; i < selectedSingle.length; i++){
			if(selectedSingle[i].checked){
				firstChoice.push(selectedSingle[i].value);
			}
		}
	}
	
	if(pageNum == 3){
		return firstChoice;
	} else {
		//두번째 선택항목에서 value값이 all인 항목에 체크되어있다면 모든 항목의 값을 넣어줌
		if(selectedMultiple[0].checked){
			for(i = 0; i < selectedMultiple.length; i++){
				multipleChoice.push(selectedMultiple[i].value);
			}
		} else {
			//아니라면 체크된 항목만 넣어줌
			for(i = 0; i < selectedMultiple.length; i++){
				if(selectedMultiple[i].checked){
					multipleChoice.push(selectedMultiple[i].value)
				}
			}
		}
		return [firstChoice, multipleChoice];
	}
}

let nextBtn = () => {
	
	if(document.getElementById("apply-page-1").style.display != 'none'){
		document.getElementById("apply-page-1").style.display = 'none';
		document.getElementById("apply-page-2").style.display = 'flex';
		let firstParams = registParams(1);
		for(i = 0; i < firstParams.length; i++){
			params.push(firstParams[i]);
		}
	} else {
		document.getElementById("apply-page-2").style.display = 'none';
		document.getElementById("apply-page-3").style.display = 'flex';
		let secondParams = registParams(2);
		for(i = 0; i < secondParams.length; i++){
			params.push(secondParams[i]);
		}
	}
}

let submitBtn = () => {
	let lastParams = registParams(3);
	params.push(lastParams);
	
	console.dir(params);
	location.href = "/mentoring/mentor-list?school_type="+params[0]+"&major_type="+params[1]+"&want_time="+params[2]+"&want_date="+params[3]+"&want_place="+params[4];
}



for(i = 0; i < 3; i++){
	let checkList = '';
	let checkList2 = '';
	
	if(i == 2){
		checkList = document.querySelectorAll('.check-4');
		
		document.querySelectorAll('.next-btn-'+ i).forEach(e =>{
			e.addEventListener('click', event =>{
				let flg = false;
				checkList.forEach(c =>{
					if(c.checked){
						flg = true;	
					}
				})
				
				if(!flg){
					alert('모든 항목에 최소 1개이상 체크 해야합니다.');
					return;
				} else {
					submitBtn();
				}
			})
		})
	} else {
		let j = i+1;
		checkList = document.querySelectorAll('.check-' + (i+i));
		checkList2 = document.querySelectorAll('.check-' + (i+j));
		
		document.querySelectorAll('.next-btn-'+ i).forEach(e =>{
			e.addEventListener('click', event =>{
				let flg = false;
				let flg2 = false;
				checkList.forEach(c =>{
					if(c.checked){
						flg = true;	
					}
				})
				
				checkList2.forEach(c =>{
					if(c.checked){
						flg2 = true;	
					}
				})
				
				if(!flg || !flg2){
					alert('필수 동의항목에 체크 하셔야합니다.');
					return;
				} else {
					nextBtn();
				}
			})
		})
	}
}












