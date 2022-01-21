(() => {
	document.getElementById('submit-btn').addEventListener('click', (e) =>{
		let startValue = document.getElementById('start-date').value;
		let endValue = document.getElementById('end-date').value;
		
		if( Number(startValue.replace(/-/gi,"")) > Number(endValue.replace(/-/gi,"")) ){
	   		alert("시작일이 종료일보다 클 수 없습니다.");
			e.preventDefault();
		} else {
			let form = document.getElementById('regist-form');
			console.dir(form);
		}
		
	})
})();
