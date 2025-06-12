$(document).ready(function() {
    (function($) {
        "use strict";

        $('#blogForm').validate({
            rules: {
                name: {
                    required: true,
                    minlength: 2,
                    maxlength: 200,
                    lettersCyrillicSymbols: true
                },
                shortExplanation: {
                    required: true,
                    maxlength: 1000
                },
                blogText: {
                    required: true,
                    minlength: 1000,
                    maxlength: 10000
                },
                image: {
                    url: true
                },
                categories: {
                    required: true,
                    lettersCyrillicCommaSeparatedSymbols: true
                },
                tags: {
                    lettersCyrillicCommaSeparatedSymbols: true
                }
            },
            messages: {
                name: {
                    required: "Моля, въведете име на блога.",
                    minlength: "Името на блога трябва да бъде поне 2 символа.",
                    maxlength: "Името на блога не може да бъде по-дълго от 200 символа.",
                    lettersCyrillicSymbols: "Името трябва да бъде написано на кирилица и може да съдържа символи."
                },
                shortExplanation: {
                    required: "Краткото описание е задължително!",
                    maxlength: "Краткото описание може да съдържа максимум 1000 символа."
                },
                blogText: {
                    required: "Текстът на блога е задължителен.",
                    minlength: "Текстът на блога трябва да бъде поне 1000 символа.",
                    maxlength: "Текстът на блога не може да бъде по-дълъг от 10000 символа."
                },
                image: {
                    url: "Моля, въведете валиден URL адрес за снимката."
                },
                categories: {
                    required: "Моля, въведете поне една категория, разделена със запетая.",
                    lettersCyrillicCommaSeparatedSymbols: "Категориите трябва да бъдат написани на кирилица и могат да съдържат символи."
                },
                tags: {
                    lettersCyrillicCommaSeparatedSymbols: "Таговете трябва да бъдат написани на кирилица и могат да съдържат символи."
                }
            },
            errorPlacement: function(error, element) {
                error.addClass("error-message");
                error.insertAfter(element);
            }
        });


        $.validator.addMethod("lettersCyrillicSymbols", function(value, element) {
            return this.optional(element) || /^[А-Яа-яёЁ\s\-\.,!@#\$%\^&\*\(\)\[\]\{\}]+$/.test(value);
        }, "Моля, използвайте само кирилица и разрешени символи.");

        $.validator.addMethod("lettersCyrillicCommaSeparatedSymbols", function(value, element) {
            return this.optional(element) || /^[А-Яа-яёЁ\s\-,\.\!@#\$%\^&\*\(\)\[\]\{\}]+$/.test(value);
        }, "Моля, използвайте само кирилица, разделяйте със запетая и използвайте разрешени символи.");

    })(jQuery);
});
