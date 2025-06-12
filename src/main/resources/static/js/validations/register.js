$(document).ready(function () {
    (function ($) {
        "use strict";


        $('.main-header').css({
            "background-color": "rgba(255, 255, 255, 0%)",
            "position": "relative",
            "z-index": "3"
        });


        $('#registerForm').validate({
            rules: {
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true,
                    minlength: 8,
                    passwordStrength: true
                },
                name: {
                    required: true,
                    minlength: 2,
                    lettersCyrillic: true
                },
                dateOfBirth: {
                    required: true,
                    validateDate: true,
                    validateAge: true
                },
                address: {
                    required: true,
                    lettersCyrillicWithSpecialChars: true
                }
            },
            messages: {
                email: {
                    required: "Моля, въведете вашия имейл адрес.",
                    email: "Моля, въведете валиден имейл адрес (напр. example@mail.com)."
                },
                password: {
                    required: "Моля, въведете парола, за да продължите.",
                    minlength: "Паролата трябва да съдържа поне 8 символа.",
                    passwordStrength: "Паролата трябва да съдържа поне една малка и една главна буква."
                },
                name: {
                    required: "Моля, въведете вашето име.",
                    minlength: "Името трябва да съдържа поне 2 символа.",
                    lettersCyrillic: "Името трябва да бъде изписано с кирилски букви."
                },
                dateOfBirth: {
                    required: "Моля, въведете вашата дата на раждане.",
                    validateDate: "Моля, използвайте формат dd/mm/yyyy.",
                    validateAge: "Необходимо е да имате навършени поне 14 години."
                },
                address: {
                    required: "Моля, въведете адрес.",
                    lettersCyrillicWithSpecialChars: "Адресът трябва да бъде на кирилица и може да съдържа цифри и специални символи."
                }
            },
            errorPlacement: function (error, element) {
                error.addClass("error-message");
                error.css({
                    "color": "darkorange",
                    "font-size": "9px",
                    "font-weight": "bold",
                    "margin-top": "5px"
                });
                error.insertAfter(element);
            }
        });


        $.validator.addMethod("regex", function (value, element, regexpr) {
            return this.optional(element) || regexpr.test(value);
        }, "Невалидна парола.");


        $.validator.addMethod("passwordStrength", function (value, element) {
            return this.optional(element) || /[a-z]/.test(value) && /[A-Z]/.test(value);
        }, "Паролата трябва да съдържа поне една главна и една малка буква.");

        $.validator.addMethod("validateAge", function (value, element) {
            var dob = parseDate(value);
            if (!dob) return false;
            var today = new Date();
            var age = today.getFullYear() - dob.getFullYear();
            var month = today.getMonth() - dob.getMonth();
            if (month < 0 || (month === 0 && today.getDate() < dob.getDate())) {
                age--;
            }
            return this.optional(element) || (age >= 14);
        }, "Трябва да сте поне 14 години.");


        $.validator.addMethod("lettersCyrillic", function (value, element) {
            return this.optional(element) || /^[А-Яа-яёЁ\s]+$/.test(value);
        }, "Името трябва да съдържа само кирилски букви.");


        $.validator.addMethod("lettersCyrillicWithSpecialChars", function (value, element) {
            return this.optional(element) || /^[А-Яа-яёЁ0-9\s\.,\-\/"']+$/.test(value);
        }, "Адресът трябва да бъде написан на кирилица, но може да съдържа специални символи и цифри.");

        $.validator.addMethod("validateDate", function (value, element) {
            return this.optional(element) || parseDate(value) !== null;
        }, "Невалидна дата.");


        function parseDate(dateString) {
            var parts = dateString.split('/');
            if (parts.length !== 3) return null;
            var day = parseInt(parts[0], 10);
            var month = parseInt(parts[1], 10) - 1;
            var year = parseInt(parts[2], 10);
            var date = new Date(year, month, day);
            return (date.getFullYear() === year && date.getMonth() === month && date.getDate() === day) ? date : null;
        }


        $('#dateOfBirth').on('input', function () {
            let value = $(this).val();

            value = value.replace(/[^\d]/g, '');


            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2);
            }
            if (value.length >= 5) {
                value = value.substring(0, 5) + '/' + value.substring(5);
            }


            $(this).val(value.substring(0, 10));
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
