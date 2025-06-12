$(document).ready(function () {
    (function ($) {
        "use strict";

        $('#contactForm').validate({
            rules: {
                name: {
                    required: true,
                    minlength: 2
                },
                subject: {
                    required: true,
                    minlength: 4
                },
                email: {
                    required: true,
                    email: true
                },
                message: {
                    required: true,
                    minlength: 20
                }
            },
            messages: {
                name: {
                    required: "Хайде, имаш име, нали?",
                    minlength: "Името ти трябва да е поне 2 символа"
                },
                subject: {
                    required: "Хайде, имаш тема, нали?",
                    minlength: "Темата трябва да е поне 4 символа"
                },
                email: {
                    required: "Без имейл, без съобщение",
                    email: "Моля, въведете валиден имейл"
                },
                message: {
                    required: "Мм... да, трябва да напишеш нещо, за да изпратиш заявката.",
                    minlength: "Това ли е всичко? Наистина?"
                }
            },
            submitHandler: function (form) {
                const formData = $(form).serialize();

                $.ajax({
                    type: 'POST',
                    url: $(form).attr('action'),
                    data: formData,
                    success: function () {
                        $('#contactForm')[0].reset();
                        $(".alert").remove();
                        $('#contactForm').prepend(`
                            <div class="alert alert-success" role="alert">
                                Вашето съобщение е изпратено успешно!
                            </div>
                        `);
                    },
                    error: function () {
                        $(".alert").remove();
                        $('#contactForm').prepend(`
                            <div class="alert alert-danger" role="alert">
                                Възникна грешка при изпращането. Моля, опитайте отново!
                            </div>
                        `);
                    }
                });

                return false;
            }
        });

    })(jQuery);
});
