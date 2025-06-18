$(document).ready(function () {
    (function ($) {
        "use strict";

        // Кирилица валидация
        $.validator.addMethod("cyrillicOnly", function (value, element) {
            return this.optional(element) || /^[\u0400-\u04FF\s]+$/.test(value);
        }, "Моля, използвайте само кирилица.");

        $('#contactForm').validate({
            rules: {
                name: {
                    required: true,
                    minlength: 2,
                    cyrillicOnly: true
                },
                subject: {
                    required: true,
                    minlength: 4,
                    cyrillicOnly: true
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
                    required: "Моля, въведете име.",
                    minlength: "Името трябва да е поне 2 символа.",
                    cyrillicOnly: "Името трябва да е на кирилица."
                },
                subject: {
                    required: "Моля, въведете тема.",
                    minlength: "Темата трябва да е поне 4 символа.",
                    cyrillicOnly: "Темата трябва да е на кирилица."
                },
                email: {
                    required: "Моля, въведете имейл.",
                    email: "Въведете валиден имейл адрес."
                },
                message: {
                    required: "Моля, въведете съобщение.",
                    minlength: "Съобщението трябва да е поне 20 символа."
                }
            },
            submitHandler: function (form) {
                const formData = $(form).serialize();
                $(".alert").remove(); // премахва стари съобщения

                $.ajax({
                    type: 'POST',
                    url: $(form).attr('action'),
                    data: formData,
                    success: function () {
                        $('#contactForm')[0].reset();
                       $('#formMessages').html(`
                           <div class="alert alert-success" role="alert" style="display:block; font-weight: bold;">
                               Вашето съобщение е изпратено успешно!
                           </div>
                       `);
                    },
                    error: function () {
                       $('#formMessages').html(`
                           <div class="alert alert-danger" role="alert" style="display:block; font-weight: bold;">
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
