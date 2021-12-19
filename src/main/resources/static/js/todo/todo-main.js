/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
	var todo_length = $(".cb_todo").length;
	var todo_length = $(".cb_todo").length;
	var checked_todo_length = $(".cb_todo:checked").length;
	
	var percent = (checked_todo_length / todo_length * 100).toFixed(0);
	if(isNaN(percent)){	//percent가 NaN이면, 
		percent = 0;
	}
	$("#percent").text(percent);	
	
	$(".Aligner-under-item").width(percent+"%");
});



function cb_click(todoIdx) {
	
	var cb = document.getElementById(todoIdx);
	var check = cb.checked;
	
	$.ajax({
	    url		:	'/todo/todaySave', //request 보낼 서버의 경로
	    type	:	'post', // 메소드(get, post, put 등)
	    data:{	
			done	: check,
			todoIdx	: todoIdx,
		}, //보낼 데이터
	    success: function(data) {
	        //서버로부터 정상적으로 응답이 왔을 때 실행
			location.reload();
			
	    },
	    error: function(err) {
	        //서버로부터 응답이 정상적으로 처리되지 못햇을 때 실행

	    }
	});

}
