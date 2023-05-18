$(document).ready(function() {
    $("#calender_btn_open").click(function() {
        if ($("#calender_container").hasClass("toggle")) {
            $("#calender_btn_open").text("열기");
            $("#calender_container").removeClass("toggle");
        } else {
            $("#calender_btn_open").text("닫기");
            $("#calender_container").addClass("toggle");
        }
    });
});
