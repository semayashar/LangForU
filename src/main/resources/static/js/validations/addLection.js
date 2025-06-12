$(document).ready(function () {
    "use strict";


    $('#releaseDate').datepicker({
        dateFormat: 'dd/mm/yy',
        beforeShow: function (input, inst) {

            var startDate = $('#courseId option:selected').data('start-date');
            var endDate = $('#courseId option:selected').data('end-date');


            if (startDate && endDate) {
                $(this).datepicker('option', {
                    minDate: new Date(startDate),
                    maxDate: new Date(endDate)
                });
            }
        }
    });


    $.validator.addMethod("dateInRange", function (value, element) {
        var releaseDate = new Date(value.split('/').reverse().join('-'));
        var startDate = new Date($('#courseId option:selected').data('start-date'));
        var endDate = new Date($('#courseId option:selected').data('end-date'));

        if (!startDate || !endDate) {
            return false;
        }


        var isValid = releaseDate >= startDate && releaseDate <= endDate;


        if (!isValid) {
            var formattedStartDate = startDate.toLocaleDateString('bg-BG');
            var formattedEndDate = endDate.toLocaleDateString('bg-BG');
            $.validator.messages.dateInRange =
                `Дата на излъчване трябва да бъде в рамките на периода ${formattedStartDate} - ${formattedEndDate}.`;
        }

        return this.optional(element) || isValid;
    }, "");


    $('#lectionForm').validate({
        rules: {
            name: {
                required: true,
                minlength: 2,
                maxlength: 500,
                lettersCyrillic: true
            },
            theme: {
                required: true,
                minlength: 2,
                maxlength: 5000,
                lettersCyrillic: true
            },
            instructor: {
                required: true,
                lettersCyrillic: true
            },
            additionalResources: {
                minlength: 10,
                maxlength: 5000
            },
            summary: {
                minlength: 1000,
                maxlength: 10000
            },
            examQuestions: {
                required: true,
                minlength: 5
            },
            course: {
                required: true
            },

            releaseDate: {
                required: true,
                dateInRange: true
            }
        },
        messages: {
            name: {
                required: "Моля, въведете име на урока.",
                minlength: "Името трябва да е поне 2 символа.",
                maxlength: "Името може да бъде до 500 символа.",
                lettersCyrillic: "Името трябва да бъде написано на кирилица и може да съдържа пунктуация."
            },
            theme: {
                required: "Моля, въведете тема на урока.",
                minlength: "Темата трябва да е поне 2 символа.",
                maxlength: "Темата може да бъде до 5000 символа.",
                lettersCyrillic: "Темата трябва да бъде написана на кирилица и може да съдържа пунктуация."
            },
            instructor: {
                required: "Моля, въведете име на инструктора.",
                lettersCyrillic: "Името на инструктора трябва да бъде написано на кирилица и може да съдържа пунктуация."
            },
            releaseDate: {
                required: "Моля, въведете дата на излъчване.",
                dateInRange: ""
            }
        },
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        }
    });


    $('#courseId').on('change', function () {
        var selectedOption = $(this).find('option:selected');
        var startDate = selectedOption.data('start-date');
        var endDate = selectedOption.data('end-date');


        $('#releaseDate').val('');


        $('#releaseDate').datepicker('option', {
            minDate: startDate ? new Date(startDate) : null,
            maxDate: endDate ? new Date(endDate) : null
        });


        console.log('Start Date:', startDate);
        console.log('End Date:', endDate);
    });
});
