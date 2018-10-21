$("form.parsley-validation").parsley({
    errorClass: 'is-invalid text-danger',
    successClass: 'is-valid',
    errorsWrapper: '<div class="invalid-feedback"></div>',
    errorTemplate: '<div></div>',
    trigger: 'change'
}).on('field:validated', function(fieldInstance) {
    $('#loader').modal('hide');
    var ok = $('.parsley-error').length === 0;
    if (!ok){
        if (fieldInstance.$element.is(":hidden")) {
            fieldInstance._ui.$errorsWrapper.css('display', 'none');
            fieldInstance.validationResult = true;
            return true;
        }
        return false;
    }
}).on('form:submit', function() {
    return true;
});

$(document).ready(function(){
    $('#loader').modal('hide');
    showHideUserOpt();
    //loadDatePicker();
    $('[data-toggle="tooltip"]').tooltip();
});
function loadDatePicker(){
    $('.datepicker').datepicker().on('onClose', function(e) {
       $(this).parsley().validate();
     });
}
function showLoader(){
$('#loader').modal('show');
}

function showHideUserOpt() {
$('.linkDiv1').hide();
if($('#roleType').val()==2 || $('#roleType').val()==3){
    $('.linkDiv1').show();
}
}

function showTime(){
    var date = new Date();
    var h = date.getHours(); // 0 - 23
    var m = date.getMinutes(); // 0 - 59
    var s = date.getSeconds(); // 0 - 59
    var session = "AM";
    if(h == 0){
        h = 12;
    }
    if(h > 12){
        h = h - 12;
        session = "PM";
    }
    h = (h < 10) ? "0" + h : h;
    m = (m < 10) ? "0" + m : m;
    s = (s < 10) ? "0" + s : s;
    var time = h + ":" + m + ":" + s + " " + session;
    document.getElementById("boardClock").innerText = time;
    document.getElementById("boardClock").textContent = time;
    setTimeout(showTime, 1000);
}


