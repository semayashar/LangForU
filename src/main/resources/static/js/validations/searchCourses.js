$(document).ready(function () {
    (function ($) {
        "use strict";


        $("#coursesSearchForm").on('submit', function (e) {
            var searchField = $("input[name='searchLanguage']");
            var searchValue = searchField.val().trim();
            var errorMessage = searchField.next(".validation-message");

            errorMessage.hide();

            if (!searchValue) {
                e.preventDefault();
                errorMessage.text("Трява да въведете дума за да потърсите").show();
                searchField.focus();
            }
        });
    })(jQuery);
});