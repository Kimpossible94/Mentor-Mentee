/**
 *
 */

document.addEventListener('DOMContentLoaded', function () {


    /*  캘린더   */
    todoList = [];
    plugins: ['interaction', 'dayGrid', 'interactionPlugin'];

    var calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
        },
        initialView: 'dayGridMonth',
        locale: 'ko',
        navLinks: true, // can click day/week names to navigate views
        businessHours: true, // display business hours
        editable: true,
        displayEventTime: false,

        //날짜 클릭하면 일정추가 팝업
        dateClick: function (info) {
            window.name = "parentForm";
            openAdd = window.open("/todo/todo-insert", "childForm", "width=600, height=520, left=100, top=100");
            //console.log(openAdd);
            window.close();
        },

        //일정 클릭하면 일정수정/삭제 팝업
        eventClick: function (info) {
            window.name = "parentForm";
            openModify = window.open("/todo/todo-modify/" + info.event.id, "popup", "width=600, height=520, left=100, top=100");
        }


        /*  alert('Event: ' + info.event.title);
          alert('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
          alert('View: ' + info.view.type);*/

    });

    // 일정 가져오기
    setDate();
    calendar.render();
    /*  캘린더   */

});


/* 일정 가져오기 */
function setDate() {

    $.ajax({
        url: '/todo/todo-list',
        type: 'get',
        success: function (data) {
            var list = data;

            for (var i = 0; i < list.length; i++) {
                var event = {
                    start: list[i].startDate,
                    end: list[i].endDate,
                    title: list[i].title,
                    color: list[i].color,
                    id: list[i].todoIdx,
                    allDay: true
                };

                calendar.addEvent(event);
            }
        },
        error: function (err) {
            //서버로부터 응답이 정상적으로 처리되지 못햇을 때 실행

        }
    });

}	

