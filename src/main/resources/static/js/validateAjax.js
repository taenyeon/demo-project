function validateAjax(url,returnUrl, formData) {
    $('div[name^=error]').remove();
    $.ajax({
        cache: false,
        url: url,
        processData: false,
        contentType: false,
        type: 'POST',
        data: formData,
        success: function (data) {
            if (data.resultCode === 200) {
                location.replace(returnUrl);
            } else {
                $.each(data, function (key, value) {
                    let msg = "<div name='error_" + key + "' style='color: red; font-size: small'>";
                    msg += value;
                    msg += "</div>"
                    let input = $('input[name=' + key + ']');
                    input.val("");
                    input.after(msg);
                });
            }
        },
        error: function () {
        }
    });
}