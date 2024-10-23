// Đảm bảo rằng JavaScript chỉ chạy sau khi toàn bộ nội dung trang được tải xong
document.addEventListener("DOMContentLoaded", function() {
    // Lấy các phần tử bằng ID
    var helpLink = document.getElementById("helpLink");
    var loginLink = document.getElementById("loginLink");
    var registerLink = document.getElementById("registerLink");

    // Gán sự kiện onclick cho mỗi liên kết nếu chúng tồn tại
    if (helpLink) {
        helpLink.onclick = function() {
            // Hành động khi người dùng nhấp vào link trợ giúp
            alert('Bạn đã nhấp vào liên kết TRỢ GIÚP');
        };
    }

    if (loginLink) {
        loginLink.onclick = function() {
            // Hành động khi người dùng nhấp vào link đăng nhập
            alert('Bạn đã nhấp vào liên kết ĐĂNG NHẬP');
        };
    }

    if (registerLink) {
        registerLink.onclick = function() {
            // Hành động khi người dùng nhấp vào link đăng ký
            alert('Bạn đã nhấp vào liên kết ĐĂNG KÝ');
        };
    }
});
