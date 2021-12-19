/**
 * 
 */

//todoInsert();
document.addEventListener('DOMContentLoaded', function() {
	var start = window.opener.document.getElementById("todo_start").value;
	document.getElementById("startInput").value = start;

});
	
function todoInsert(){

//	addListeners();

	let	titleInput = document.getElementById("titleInput");	//일정제목
	let	startInput = document.getElementById("startInput");
	let	endInput = document.getElementById("endInput");
	let	colorInput = document.getElementById("colorInput");		
	let	addButton = document.getElementById("addButton");	//일정추가버튼
	
	
//	function addListeners(){
//		addButton.addEventListener("click", addEntry);	//클릭시 일정등록
//	}
	
			
	//let flag = true;

	let titleValue = titleInput.value;
	if(!titleValue){
		alert('제목을 입력해주세요');
		document.getElementById("titleInput").focus();
		return;
	}
		
	let startValue = startInput.value;
	if(!startValue){
		alert('날짜를 입력해주세요');
		return;
	}			
	let endValue = endInput.value;
	if(!endValue){
		alert('날짜를 입력해주세요');
		return;

	}			
	
	if( Number(startValue.replace(/-/gi,"")) > Number(endValue.replace(/-/gi,"")) ){
		alert("시작일이 종료일보다 클 수 없습니다.");
		return;
	}
	
	let colorValue = colorInput.value;
	
	var event = {
		title	: titleValue,
		start	: startValue,
		end		: endValue,
		color	: colorValue,
		done	: 0
	};
	
	console.log(event);
	
	setDate(event);
	

	// 등록 끝
//	opener.document.location.reload();
//	alert('일정이 등록되었습니다.');
//	close();
		
}




function setDate(event) {
	
	$.ajax({
	    url		:	'/todo/insert', //request 보낼 서버의 경로
	    type	:	'post', // 메소드(get, post, put 등)
	    data:{	
				title		: event.title,
				startDate	: event.start,
				endDate		: event.end,
				color		: event.color,
				done		: 0
		}, //보낼 데이터
	    success: function(data) {
			window.opener.document.location.reload();
			alert("등록 성공!!");
			close();
	    },
	    error: function(err) {
	        //서버로부터 응답이 정상적으로 처리되지 못햇을 때 실행

	    }
	});

}	

