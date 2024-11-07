// Lấy các phần tử DOM cần thiết
const languageSelector = document.getElementById('languageSelector');
const languageModal = document.getElementById('languageModal');
const closeBtn = document.querySelector('.close');
const applyBtn = document.getElementById('applyChoice');
const languageSelect = document.getElementById('language');

// Hiển thị modal khi click vào language selector
languageSelector.addEventListener('click', function() {
    languageModal.style.display = 'block';
});

// Đóng modal khi click vào nút close
closeBtn.addEventListener('click', function() {
    languageModal.style.display = 'none';
});

// Đóng modal khi click bên ngoài modal
window.onclick = function(event) {
    if (event.target == languageModal) {
        languageModal.style.display = 'none';
    }
}

// Xử lý khi click nút "LỰA CHỌN"/"APPLY"
applyBtn.addEventListener('click', function() {
    const selectedLanguage = languageSelect.value;
    console.log("Selected language:", selectedLanguage); // Debug log
    
    // Chuyển hướng dựa trên ngôn ngữ được chọn
    if (selectedLanguage === 'tieng-viet') {
        console.log("Switching to Vietnamese"); // Debug log
        window.location.href = '/san_bay.html';
    } else if (selectedLanguage === 'english') {
        console.log("Switching to English"); // Debug log
        window.location.href = '/san_bay_en.html';
    }
    
    // Cập nhật text hiển thị
    const country = document.getElementById('country');
    const displayText = country.options[country.selectedIndex].text + " - " + 
                       languageSelect.options[languageSelect.selectedIndex].text;
    languageSelector.textContent = displayText;
    
    // Đóng modal
    languageModal.style.display = 'none';
});