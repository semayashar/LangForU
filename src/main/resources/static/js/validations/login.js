$(document).ready(function() {
    (function($) {
        "use strict";


        $('.main-header').css({
            "background-color": "rgba(255, 255, 255, 0%)",
            "position": "relative",
            "z-index": "3"
        });

        $('#loginForm').validate({
            rules: {
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true,
                    minlength: 8
                }
            },
            messages: {
                email: {
                    required: "Моля, напишете имейл.",
                    email: "Хайде, не се опитвайте с невалиден имейл!"
                },
                password: {
                    required: "Паролата е задължителна!",
                    minlength: "Паролата трябва да бъде поне 8 символа."
                }
            },
            errorPlacement: function(error, element) {
                var errorContainer = '';
                if (element.attr('name') === 'email') {
                    errorContainer = '#email-error';
                } else if (element.attr('name') === 'password') {
                    errorContainer = '#password-error';
                }

                error.addClass("error-message");
                error.css({
                    "color": "darkorange",
                    "font-size": "14px",
                    "font-weight": "bold",
                    "margin-top": "5px"
                });

                $(errorContainer).html(error);
            }
        });

        $('input').on('focus', function () {
            var placeholderText = $(this).attr('placeholder');
            $(this).data('placeholder', placeholderText).attr('placeholder', '');
        }).on('blur', function () {
            if ($(this).val() === '') {
                var originalPlaceholder = $(this).data('placeholder');
                $(this).attr('placeholder', originalPlaceholder);
            }
        });
    })(jQuery);
});
