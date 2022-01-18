document.addEventListener('DOMContentLoaded', function() {
	var start = window.opener.document.getElementById("todo_start").value;
	document.getElementById("startInput").value = start;

});
	
function todoInsert(){

	let titleValue = document.getElementById("titleInput").value;
	let startValue = document.getElementById("startInput").value;
	let endValue = document.getElementById("endInput").value;
	let colorValue = document.getElementById("colorInput").value;

	if (!titleValue) {
		alert('제목을 입력해주세요');
		return;
	} else if (!startValue) {
		alert('날짜를 입력해주세요');
		return;
	} else if (!endValue) {
		alert('날짜를 입력해주세요');
		return;
	} else if (Number(startValue.replace(/-/gi, "")) > Number(endValue.replace(/-/gi, ""))) {
		alert("시작일이 종료일보다 클 수 없습니다.");
		return;
	}

	var event = {
		title	: titleValue,
		start	: startValue,
		end		: endValue,
		color	: colorValue,
		done	: 0
	};

	setDate(event);
}

function setDate(event) {
	
	$.ajax({
	    url		:	'/todo/insert',
	    type	:	'post',
	    data:{	
				title		: event.title,
				startDate	: event.start,
				endDate		: event.end,
				color		: event.color,
				done		: 0
		},
	    success: function(data) {
			window.opener.document.location.reload();
			alert("등록 성공!!");
			close();
	    },
	    error: function(err) {
			window.opener.document.location.reload();
			alert("등록에 실패하였습니다.");
			close();
	    }
	});

}	

