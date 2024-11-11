
        document.addEventListener('DOMContentLoaded', function() {
            var modal = document.getElementById("languageModal");
            var btn = document.getElementById("languageSelector");
            var span = document.getElementsByClassName("close")[0];
            var applyBtn = document.getElementById("applyChoice");

            btn.onclick = function(e) {
                e.preventDefault();
                modal.style.display = "block";
            }

            span.onclick = function() {
                modal.style.display = "none";
            }

            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }

            applyBtn.onclick = function() {
                var country = document.getElementById("country");
                var language = document.getElementById("language");
                var displayText = country.options[country.selectedIndex].text + " - " + 
                                language.options[language.selectedIndex].text;
                btn.textContent = displayText;
                modal.style.display = "none";
            }

            document.getElementById("helpLink").onclick = function(event) {
                event.preventDefault();
                
                // Kiểm tra URL hiện tại để xác định ngôn ngữ
                const isEnglish = window.location.pathname.includes('_en');
                
                if (isEnglish) {
                    alert("Welcome to Wings Airport Online!\n\n### Introduction\nWings Airport Online is a platform that helps you manage and book airline tickets easily and conveniently. Explore our features and services for an excellent flying experience!\n\n### Main Features\n- Book and manage your flights.\n- View detailed information about flight schedules and airports.\n- Online support to answer all your questions.\n- Detailed information about services and facilities at the airport.\n\n### Frequently Asked Questions (FAQ)\n**How do I book a flight?**\nTo book a flight on Wings Airport Online, you can access the booking section and follow the on-screen instructions.\n\n**How can I change my flight schedule?**\nYou can change your flight schedule by accessing the flight management section and selecting the schedule modification function.\n\n### Contact and Support\nIf you need more information or support, please contact us via email support@wingsairport.com or call our hotline: 0123 456 789");
                } else {
                    alert("Chào mừng bạn đến với Sân bay Trực tuyến!\n\n### Giới thiệu\nSân bay Trực tuyến là nền tảng giúp bạn quản lý và đặt vé máy bay một cách dễ dàng và thuận tiện. Hãy khám phá các tính năng và dịch vụ mà chúng tôi cung cấp để có một trải nghiệm đi bay tuyệt vời!\n\n### Các tính năng chính\n- Đặt chỗ và quản lý chuyến bay của bạn.\n- Xem thông tin chi tiết về lịch trình chuyến bay và sân bay.\n- Hỗ trợ trực tuyến để giải đáp mọi thắc mắc của bạn.\n- Thông tin chi tiết về các dịch vụ và tiện ích tại sân bay.\n\n### Các câu hỏi thường gặp (FAQ)\n**Làm thế nào để đặt vé máy bay?**\nĐể đặt vé máy bay trên Sân bay Trực tuyến, bạn có thể truy cập vào phần đặt chỗ và làm theo các bước hướng dẫn trên màn hình.\n\n**Tôi có thể thay đổi lịch trình chuyến bay như thế nào?**\nBạn có thể thay đổi lịch trình chuyến bay bằng cách truy cập vào phần quản lý chuyến bay và chọn chức năng sửa đổi lịch trình.\n\n### Liên hệ và Hỗ trợ\nNếu bạn cần thêm thông tin hoặc hỗ trợ, vui lòng liên hệ với chúng tôi qua email support@sanbaytructuyen.com hoặc gọi số hotline: 0123 456 789");
                }
            }


            function showContainer(containerId) {
                document.querySelectorAll('.container').forEach(container => {
                    container.style.display = 'none';
                });
                if (containerId === 'booking') {
                    document.getElementById('booking-container').style.display = 'block';
                }
            }
        });