$(document).ready(function () {
    (function ($) {
        "use strict";

        $('#courseForm').validate({
            rules: {
                language: {
                    required: true,
                    minlength: 2,
                    maxlength: 200
                },
                level: {
                    required: true
                },
                price: {
                    required: true,
                    number: true,
                    min: 0
                },
                startDate: {
                    required: true,
                    pattern: /^\d{2}\/\d{2}\/\d{4}$/
                },
                endDate: {
                    required: true,
                    pattern: /^\d{2}\/\d{2}\/\d{4}$/
                },
                mainInstructorName: {
                    required: true,
                    maxlength: 100
                },
                assistantInstructorName: {
                    required: true,
                    maxlength: 100
                },
                technicianName: {
                    required: true,
                    maxlength: 100
                },
                pictureUrl: {
                    required: true,
                    url: true
                },
                description: {
                    required: true,
                    maxlength: 10000
                },
                rating: {
                    required: true,
                    digits: true,
                    range: [0, 5]
                }
            },
            messages: {
                language: {
                    required: "Моля, въведете език.",
                    minlength: "Езикът трябва да съдържа поне 2 символа.",
                    maxlength: "Езикът не може да бъде по-дълъг от 200 символа."
                },
                level: {
                    required: "Моля, изберете ниво."
                },
                price: {
                    required: "Моля, въведете цена.",
                    number: "Цената трябва да бъде валидно число.",
                    min: "Цената не може да бъде отрицателна."
                },
                startDate: {
                    required: "Моля, въведете начална дата.",
                    pattern: "Моля, използвайте формат дд/мм/гггг."
                },
                endDate: {
                    required: "Моля, въведете крайна дата.",
                    pattern: "Моля, използвайте формат дд/мм/гггг."
                },
                mainInstructorName: {
                    required: "Моля, въведете име на главния инструктор.",
                    maxlength: "Името не може да бъде по-дълго от 100 символа."
                },
                assistantInstructorName: {
                    required: "Моля, въведете име на асистента.",
                    maxlength: "Името не може да бъде по-дълго от 100 символа."
                },
                technicianName: {
                    required: "Моля, въведете име на техника.",
                    maxlength: "Името не може да бъде по-дълго от 100 символа."
                },
                pictureUrl: {
                    required: "Моля, въведете URL за снимка.",
                    url: "Моля, въведете валиден URL адрес."
                },
                description: {
                    required: "Моля, въведете описание.",
                    maxlength: "Описанието не може да бъде по-дълго от 10000 символа."
                },
                rating: {
                    required: "Моля, въведете рейтинг.",
                    digits: "Рейтингът трябва да бъде цяло число.",
                    range: "Рейтингът трябва да бъде между 0 и 5."
                }
            },
            errorPlacement: function (error, element) {
                error.addClass("error-message");
                error.insertAfter(element);
            },
            onkeyup: function (element) {
                $(element).valid();
            }
        });


        flatpickr("#startDate", {
            dateFormat: "d/m/Y",
            allowInput: true
        });
        flatpickr("#endDate", {
            dateFormat: "d/m/Y",
            allowInput: true
        });


        function formatDateInput(event) {
            const input = event.target;
            let value = input.value.replace(/\D/g, '');


            if (value.length > 8) {
                value = value.slice(0, 8);
            }


            if (value.length >= 2) {
                value = value.slice(0, 2) + '/' + value.slice(2);
            }
            if (value.length >= 5) {
                value = value.slice(0, 5) + '/' + value.slice(5);
            }

            input.value = value;
        }

        const dateInputs = document.querySelectorAll('#startDate, #endDate');
        dateInputs.forEach(input => {
            input.addEventListener('input', formatDateInput);
        });

    })(jQuery);
});
