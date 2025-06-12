$(document).ready(function() {
    (function($) {
        "use strict";


        $(function() {
            var preloader = $('#preloader-active');
            var mainContent = $('#main-content');
            var message = $('#message');
            var error = $('#error');


            preloader.hide();

            mainContent.show();


            if (message.length && $.trim(message.text())) {
                alert(message.text());
            }

            if (error.length && $.trim(error.text())) {
                alert(error.text());
            }
        });


        $('#signupForm').validate({
            rules: {
                pin: {
                    required: true,
                    minlength: 10,
                    maxlength: 10,
                    digits: true
                },
                citizenship: {
                    required: true
                },
                acceptPolicy: {
                    required: true
                }
            },
            messages: {
                pin: {
                    required: "Моля, въведете вашето ЕГН.",
                    minlength: "ЕГН трябва да съдържа точно 10 цифри.",
                    maxlength: "ЕГН трябва да съдържа точно 10 цифри.",
                    digits: "ЕГН може да съдържа само цифри."
                },
                citizenship: {
                    required: "Моля, изберете вашия статус на гражданство."
                },
                acceptPolicy: {
                    required: "Трябва да приемете условията за обучения."
                }
            },
            errorPlacement: function(error, element) {
                error.addClass("error-message");
                if (element.attr("type") === "radio") {
                    error.insertAfter(element.closest('.form-group').children('label').last());
                } else {
                    error.insertAfter(element);
                }
            }
        });

    })(jQuery);
});
