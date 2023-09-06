

function doPut(url, jsonData, onComplete) {
    sendRequestWithData(url, jsonData, "put", onComplete);
}
function sendRequestWithData(url, jsonData, method, onComplete) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        beforeSend: function (jqXHR, settings) { jqXHR.setRequestHeader(header, token);},
        url: url,
        dataType: 'json',
        type: method,
        contentType: 'application/json',
        data: jsonData,
        processData: false,
        success: function( data, textStatus, jQxhr ){
            if (data.redirect) {
                // data.redirect contains the string URL to redirect to
                window.location.href = data.redirect;
            }
            if (onComplete === undefined) {
                location.reload();
            } else {
                onComplete(jQxhr.responseText)
            }
        },
        error: function( jqXhr, textStatus, errorThrown ) {
            console.log(jqXhr.responseText);
        }
    });
}