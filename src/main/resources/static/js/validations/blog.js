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
                error.addClass("validation-message text-danger");
                error.insertAfter(element);
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
                        $('#successMessage').show();
                        $('#newsletterForm')[0].reset();
                        $(".validation-message").hide();
                    },
                    error: function () {
                        $('#successMessage').hide();
                        $(".validation-message").text("Произошла грешка. Моля, опитайте отново.").show();
                    }
                });
                return false;
            }
        });

    })(jQuery);
});
