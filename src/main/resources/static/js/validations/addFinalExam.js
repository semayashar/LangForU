$(document).ready(function() {
    (function($) {
        "use strict";


        $('#finalExamForm').validate({
            rules: {

                courseId: {
                    required: true
                },

                examQuestions: {
                    required: true,
                    minlength: 10
                },

                essayTopic: {
                    required: true,
                    minlength: 10
                }
            },
            messages: {
                courseId: {
                    required: "Моля, изберете курс."
                },
                examQuestions: {
                    required: "Моля, въведете изпитни упражнения.",
                    minlength: "Изпитните упражнения трябва да съдържат поне 10 знака."
                },
                essayTopic: {
                    required: "Моля, въведете тема за есе.",
                    minlength: "Темата за есе трябва да съдържа поне 10 знака."
                }
            },
            errorPlacement: function(error, element) {
                var errorContainer = '';

                if (element.attr('name') === 'courseId') {
                    errorContainer = '#course-error';
                } else if (element.attr('name') === 'examQuestions') {
                    errorContainer = '#examQuestions-error';
                } else if (element.attr('name') === 'essayTopic') {
                    errorContainer = '#essayTopic-error';
                }

                error.addClass("error-message");
                error.css({
                    "color": "darkorange",
                    "font-size": "14px",
                    "font-weight": "bold",
                    "margin-top": "5px"
                });

                $(errorContainer).html(error);
            },
            submitHandler: function(form) {
                form.submit();
            }
        });


        $('input, textarea').on('focus', function () {
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
