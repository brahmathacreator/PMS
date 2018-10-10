$(document).ready(function(){
    initAJAXcall('@routes.Main.sessionCheck(request.uri)' ,null, $('input:hidden[name=csrfToken]').val());
});

function initAJAXcall(url,data,token){
 $.ajax({
        url: url,
        data: JSON.stringify(data),
        type: 'POST',
        headers: {
           'Content-Type': 'application/json',
           'Csrf-Token': token
        },
        success: function(res) {
          if (res) {
            console.log("Success!" + res);
          } else {
             $('#INVSFrm').submit();
          }
        },
         error: function(xhr, status, error) {
            console.log("Error: "+xhr.responseText);
            $('#CHSFrm').submit();
         }
      });
}
