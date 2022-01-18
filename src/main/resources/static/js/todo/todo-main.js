/**
 *
 */
document.addEventListener('DOMContentLoaded', function () {
    var todo_length = $(".cb_todo").length;
    var checked_todo_length = $(".cb_todo:checked").length;

    var percent = (checked_todo_length / todo_length * 100).toFixed(0);
    if (isNaN(percent)) {
        percent = 0;
    }
    $("#percent").text(percent);

    $(".Aligner-under-item").width(percent + "%");
});


function cb_click(input) {
    var todoIdx = input.id;
    var check = 1;
    if(input.checked == false){
        check = 0;
    }

    $.ajax({
        url: '/todo/modify',
        type: 'post',
        data: {
            done: check,
            todoIdx: todoIdx,
        },
        success: function (data) {
            location.reload();
        },
        error: function (err) {
            //서버로부터 응답이 정상적으로 처리되지 못햇을 때 실행
        }
    });

}
