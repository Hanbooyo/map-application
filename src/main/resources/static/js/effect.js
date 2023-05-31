$(window).load(function() {
    // loading
	$("#load").fadeOut(2000);
})

$(document).ready(function() {
    // sidebar open & close function
    $("#btn-open").hide();

    // sidebar close function
    $("#btn-close").click(function() {
        $("#btn-close").hide();
        $("#btn-open").show();
        $("#calendar-container").animate({left: "-440px"}, 400);
    });

    // sidebar open function
    $("#btn-open").click(function() {
        $("#btn-open").hide();
        $("#btn-close").show();
        $("#calendar-container").animate({left: "60px"}, 400);
    });
});
