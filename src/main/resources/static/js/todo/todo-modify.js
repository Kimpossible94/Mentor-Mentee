function todoModify() {

	let todoIdxValue = document.getElementById("todoIdx").value;
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
		todoIdx: todoIdxValue,
		title: titleValue,
		start: startValue,
		end: endValue,
		color: colorValue
	};

	setDate(event);
}

function todoDelete() {
	if (confirm("정말 삭제하시겠습니까?") == true) {
		let todoIdxValue = document.getElementById("todoIdx").value;
		$.ajax({
			url: '/todo/delete',
			type: 'post',
			data: {
				todoIdx: todoIdxValue
			}, //보낼 데이터
			success: function (data) {
				window.opener.document.location.reload();
				alert("삭제 성공!!");
				close();
			},
			error: function (err) {
				//서버로부터 응답이 정상적으로 처리되지 못햇을 때 실행
			}
		});
		document.submit();
	} else {
		return;
	}
}


function setDate(event) {

	$.ajax({
		url: '/todo/modify',
		type: 'post',
		data: {
			todoIdx: event.todoIdx,
			title: event.title,
			startDate: event.start,
			endDate: event.end,
			color: event.color,
		},
		success: function (data) {
			window.opener.document.location.reload();
			alert("수정 성공!!");
			close();
		},
		error: function (err) {
			//서버로부터 응답이 정상적으로 처리되지 못햇을 때 실행
		}
	});

}

