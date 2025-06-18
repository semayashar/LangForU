$(document).ready(function () {
    (function ($) {
        "use strict";


        $.validator.addMethod("cyrillicOnly", function (value, element) {
            return this.optional(element) || /^[\u0400-\u04FF\s]+$/.test(value);
        }, "Само на кирилица.");


        $('#blogSearchForm').validate({
            rules: {
                query: {
                    required: true,
                    cyrillicOnly: true
                }
            },
            messages: {
                query: {
                    required: "Моля, напишете нещо.",
                    cyrillicOnly: "Само на кирилица."
                }
            },
           errorPlacement: function (error, element) {
               if (element.attr("name") === "query") {
                   error.addClass("validation-message text-danger");
                   $("#blogSearchErrorContainer").html(error); // поставя точно в контейнера
               } else {
                   error.addClass("validation-message text-danger");
                   error.insertAfter(element);
               }
           },
            submitHandler: function (form) {
                form.submit();
            }
        });


        $('#newsletterForm').validate({
            rules: {
                email: {
                    required: true,
                    email: true
                }
            },
            messages: {
                email: {
                    required: "Моля, напишете имейл.",
                    email: "Моля, напишете валиден имейл."
                }
            },
            errorPlacement: function (error, element) {
                error.addClass("validation-message text-danger");
                error.insertAfter(element);
            },
            submitHandler: function (form) {
                var formData = $(form).serialize();
                $.ajax({
                    type: "POST",
                    url: $(form).attr('action'),
                    data: formData,
                    success: function (response) {
                        $('#successMessage').text(response).css("color", "green").show();
                        $('#newsletterForm')[0].reset();
                        $(".validation-message").hide();
                    },
                    error: function (xhr) {
                        let message = "Възникна грешка. Моля, опитайте отново.";
                        if (xhr.status === 409) {
                            message = xhr.responseText; // "Вече сте абонирани!"
                        }
                        $('#successMessage').text(message).css("color", "red").show();
                    }
                });
                return false;
            }
        });

    })(jQuery);
});
