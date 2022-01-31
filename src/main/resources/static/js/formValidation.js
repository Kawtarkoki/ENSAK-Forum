$(document).ready(function () {
//    (function() {
//      'use strict';
//      window.addEventListener('load', function() {
//        // Fetch all the forms we want to apply custom Bootstrap validation styles to
//        var forms = document.getElementsByClassName('needs-validation');
//        // Loop over them and prevent submission
//        var validation = Array.prototype.filter.call(forms, function(form) {
//          form.addEventListener('submit', function(event) {
//            if (form.checkValidity() === false) {
//              event.preventDefault();
//              event.stopPropagation();
//            }
//            form.classList.add('was-validated');
//          }, false);
//        });
//      }, false);
//    })();

    $('.formInput').on("keyup blur",function (Event) {
        var input = Event.currentTarget;
        var inputVal = $(input).val();
        var id = input.id;
        var invalid_feedback= id +" is required!";
        var validation = "is-valid";
        var validationBar ="is-invalid";
        var userFieldJson = {
                    username: "",
                    password: "",
                    role: null,
                    enabled: true,
                    firstName: "",
                    familyName: ""
        };
        userFieldJson[id] = inputVal;
        $.ajax({
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(userFieldJson),
            url: "http://"+location.host+"/validateField",
            success: function (data) {
            },
            error: function (data) {
                var responseError = JSON.parse(JSON.stringify(data));
                    if(responseError.responseJSON.errors.hasOwnProperty(id)) {
                        invalid_feedback = responseError.responseJSON.errors[id];
                        validation = "is-invalid";
                        validationBar = "is-valid";
                    }
                    else if(id === "username"){
                        if(userAlreadyExist(inputVal)){
                            invalid_feedback = "This username is already exist!"
                            validation = "is-invalid";
                            validationBar = "is-valid";
                        }
                    }
                    $(input).parent().find("div.invalid-feedback").text(invalid_feedback);
                    $(input).removeClass(validationBar);
                    $("#myRegistrationForm").removeClass('was-validated');
                    $(input).addClass(validation);

            }
        });
    });

    var userAlreadyExist = function (usernameVal) {
//    function userAlreadyExist(usernameVal){
                    var responseUser = false;
                    var usernameJson = {
                        username: usernameVal,
                    };
                    $.ajax({
                        async: false,
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(usernameJson),
                        url: "http://"+location.host+"/validateUsername",
                        success: function (data) {
                            responseUser = data;
                        },
                        error: function (err) {
                            console.log(err);
                        }
                    });
          return responseUser;
    };

    $("#myRegistrationForm button[type='submit']").click(function(event){
        var valid = true;
        event.preventDefault();
        $(".formInput").each(function(){
            if($(this).hasClass('is-invalid')){
                valid = false;
            }else if(!$(this).hasClass('is-valid')){
                $(this).addClass('is-invalid');
            }
        });
        if(valid){
            registreUser();
        }
    });

    function registreUser(){
        var userJson = {
            username: $("#username").val(),
            password: $("#password").val(),
            enabled: true,
            firstName: $("#firstName").val(),
            familyName: $("#familyName").val(),
            role: $('input[name="role"]:checked').val()
        };
        $.ajax({
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(userJson),
            url: "http://"+location.host+"/new",
            success: function (data) {
                var myAlert = '<div class="alert alert-success" role="alert">Successfully registred!</div>';
                $("#successAlert").find(".modal-dialog").html(myAlert);
                $("#successAlert").modal('show');
                setTimeout(function(){
                    window.location.replace("http://"+location.host+"/login");
                }, 1500);
            },
            error: function (data) {
            var myAlert = '<div class="alert alert-danger" role="alert">Somthing went wrong, fail to registre!</div>';
            $("#successAlert").find(".modal-dialog").html(myAlert);
            $("#successAlert").modal('show');
            }
        });
    }
})
