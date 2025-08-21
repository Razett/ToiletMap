var ToastService = (function (){

    const toastContainer = $('#toast-container');

    function printToast(content) {
        var toastHtml = '<div class="toast" role="alert" aria-live="assertive" aria-atomic="true">\n' +
            '                        <div class="toast-header bgc-title text-white">\n' +
            '                          <strong class="me-auto">알림</strong>\n' +
            '                          <small class="text-body-primary">' + formatTime() + '</small>\n' +
            '                          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>\n' +
            '                        </div>\n' +
            '                        <div class="toast-body">\n' +
            '                          ' + content + '\n' +
            '                        </div>\n' +
            '                      </div>'

        if (toastContainer) {
            toastContainer.prepend(toastHtml);

            var toasts = document.getElementsByClassName('toast');

            var toastBootstrap = bootstrap.Toast.getOrCreateInstance(toasts[0]);
            toastBootstrap.show();
        }
    }

    function formatTime() {
        const now = new Date();
        const hours = now.getHours();
        const minutes = now.getMinutes();
        const seconds = now.getSeconds();

        // Determine AM or PM
        const period = hours >= 12 ? '오후' : '오전';

        // Convert hours from 24-hour to 12-hour format
        const hour12 = hours % 12 || 12; // 12 for '12 AM' and '12 PM'

        // Format hours, minutes, and seconds with leading zero if needed
        const formattedHours = String(hour12).padStart(2, '0');
        const formattedMinutes = String(minutes).padStart(2, '0');
        const formattedSeconds = String(seconds).padStart(2, '0');

        // Create formatted time string
        const formattedTime = `${period} ${formattedHours}시 ${formattedMinutes}분 ${formattedSeconds}초`;

        return formattedTime;
    }

    return {
        printToast:printToast
    };
})();