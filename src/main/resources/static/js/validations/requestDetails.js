$(document).ready(function () {
    $('#sendEmailForm').validate({
        rules: {
            middleContent: {
                required: true,
                minlength: 10
            }
        },
        messages: {
            middleContent: {
                required: "Моля, въведете съобщение.",
                minlength: "Минималната дължина е 10 символа."
            }
        }
    });
});


    var successMessage = $("#successMessage").text();
    var errorMessage = $("#errorMessage").text();


    if (successMessage) {
        $('#modalMessage').text(successMessage);
        $('#successModal').modal('show');
    }

    else if (errorMessage) {
        $('#modalMessage').text(errorMessage);
        $('#successModal').modal('show');
    }
});
