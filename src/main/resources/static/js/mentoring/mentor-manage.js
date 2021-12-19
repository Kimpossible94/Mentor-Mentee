let renderApplying = () => {
	document.querySelector('#type_manage').innerHTML = "신청중인 멘토링";
	
	document.querySelector('#expiration-date').innerHTML = "* 신청일 기준 3일 내로 멘토의 수락이 없을 시 삭제됩니다."
	
	document.getElementById("apply-mentoring").style.display = 'flex';
	document.getElementById("process-mentoring").style.display ="none";	
	document.getElementById("finish-mentoring").style.display ="none";
}

let renderProceeding = () => {
	document.querySelector('#type_manage').innerHTML = "진행중인 멘토링";
	
	document.querySelector('#expiration-date').innerHTML = ""
	
	document.getElementById("apply-mentoring").style.display = 'none';
	document.getElementById("process-mentoring").style.display ="flex";	
	document.getElementById("finish-mentoring").style.display ="none";
}

let renderCompleted = () => {
	document.querySelector('#type_manage').innerHTML = "완료한 멘토링";
	document.querySelector('#expiration-date').innerHTML = "* 최대 1년 내의 멘토링 기록만 보여집니다."
	
	document.getElementById("apply-mentoring").style.display = 'none';
	document.getElementById("process-mentoring").style.display ="none";	
	document.getElementById("finish-mentoring").style.display ="flex";
}

let reapply = (aIdx, reapplyCnt) => {
	if(reapplyCnt > 1){
		alert("재신청은 최대 2회까지 가능합니다.");
		return;
	} else {
		location.href=`/mentoring/reapply-complete?a_idx=${aIdx}`;
	}
}

let registApply = (mentorIdx, mentorName) => {
	location.href=`/mentoring/regist-apply?mentor_idx=${mentorIdx}&mentor_name=${mentorName}`;
}

let rating = (mIdx) => {
	fetch(`/mentoring/comment-check?m_idx=${mIdx}`)
	.then(response => {
		if(response.ok){
			return response.text();
		} else {
			throw new Error(response.status);
		}
	})
	.then(text => {
		if(text == 'not-registered'){
			location.href = `/mentoring/rating-page?m_idx=${mIdx}`;
		} else {
			alert("이미 평가를 작성한 멘토입니다.");
			return;
		}
	})
}

let acceptBtn = (userIdx) => {
	location.href = `/mentoring/mentoring-accept?mentee_user_idx=${userIdx}`;
}

let payment = (mentorIdx) => {
	location.href = `/mentoring/payment?mentor_idx=${mentorIdx}`;
}





