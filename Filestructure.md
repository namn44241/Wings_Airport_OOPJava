# Wings_Airport_OOPJava File Structure

Wings_Airport_OOPJava/
├── .idea/
│   ├── inspectionProfiles/
│   │   └── profiles_setting.xml
│   ├── misc.xml
│   ├── modules.xml  
│   ├── vcs.xml
│   └── Wings_Airport_OOPJava.iml
├── .mvn/  
│   └── wrapper/
│       └── maven-wrapper.properties
├── .vscode/   
│   └── settings.json
├── quanlisanbay/  
│   ├── docker-compose.yml
│   ├── Dockerfile
│   └── init.sql
├── Query MySQL/
│   ├── insertinto.sql
│   ├── QuanliSanBay.sql
│   └── TruyVannn.sql  
├── src/   
│   ├── main/
│   │   └── java/
│   │       ├── com/
│   │       │   └── example/
│   │       │       └── quanlisanbay/
│   │       │           ├── config/
│   │       │           │   ├── EmailService.java
│   │       │           │   ├── LoginRequired.java
│   │       │           │   ├── LoginRequiredInterceptor.java    
│   │       │           │   └── WebMvcConfig.java
│   │       │           ├── controllers/
│   │       │           │   ├── core/
│   │       │           │   │   ├── AdminController.java
│   │       │           │   │   └── SanBayController.java
│   │       │           │   ├── AsignmentController.java
│   │       │           │   ├── AuthController.java
│   │       │           │   ├── BookingController.java
│   │       │           │   ├── CustomerController.java
│   │       │           │   ├── DatabaseConnectionController.java
│   │       │           │   ├── EmailController.java
│   │       │           │   ├── EmployeeController.java
│   │       │           │   ├── FlightController.java
│   │       │           │   ├── FlightScheduleController.java
│   │       │           │   ├── LanguageController.java
│   │       │           │   ├── MainController.java
│   │       │           │   ├── PlaneController.java
│   │       │           │   └── PlaneTypeController.java
│   │       │           ├── model/
│   │       │           │   ├── ChuyenBay.java
│   │       │           │   ├── DatCho.java
│   │       │           │   ├── EmailRequest.java
│   │       │           │   ├── EmailResponse.java
│   │       │           │   ├── KhachHang.java
│   │       │           │   ├── LichBay.Java
│   │       │           │   ├── LoaiMayBay.java
│   │       │           │   ├── MayBay.java
│   │       │           │   ├── NhanVien.java
│   │       │           │   └── PhanCong.java
│   │       │           ├── repository/
│   │       │           │   ├── ChuyenBayRepository.java
│   │       │           │   ├── DatChoRepository.java
│   │       │           │   ├── KhackHangRepository.java
│   │       │           │   ├── LichBayRepository.java
│   │       │           │   ├── LoaiMayBayRepository.java
│   │       │           │   ├── MayBayRepository.java
│   │       │           │   ├── NhanVienRepository.java
│   │       │           │   └── PhanCongRepository.java
│   │       │           ├── repository/
│   │       │           │   ├── ChuyenBayService.java
│   │       │           │   ├── DatChoService.java
│   │       │           │   ├── KhackHangService.java
│   │       │           │   ├── LichBayService.java
│   │       │           │   ├── LoaiMayBayService.java
│   │       │           │   ├── MayBayService.java
│   │       │           │   ├── NhanVienService.java
│   │       │           │   └── PhanCongService.java
│   │       │           └── QuanlisanbayApplication.java
│   │       └── resources/
│   │           ├── static/
│   │           │   ├── css/
│   │           │   │   ├── styles_admin.css                
│   │           │   │   └── styles_sanbay.css
│   │           │   ├── images/
│   │           │   └── js/
│   │           │       └── sanbay/
│   │           │           ├── alert-noti.js
│   │           │           ├── an-khi-tat.js
│   │           │           ├── dom-preload.js
│   │           │           ├── dom-xoay.js
│   │           │           ├── fe-link-be.js
│   │           │           ├── language-switcher.js
│   │           │           ├── menu-helplink-languages.js
│   │           │           ├── send-mail.js
│   │           │           ├── slide-khampha.js   
│   │           │           ├── slide-anhto.js
│   │           │           ├── slide-khampha.js   
│   │           │           └── tin-tuc.js 
│   │           ├── templates/
│   │           │    ├── admin.html
│   │           │    ├── login.html
│   │           │    ├── san_bay_en.html
│   │           │    └── san_bay.html
│   │           └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── quanlisanbay/
│                       └── QuanlisanbayApplicationTests.java
├── api_collection.json
├── Filestructure.md
├── mvnw.cmd
├── pom.xml
├── readme-hdsd.md
└── README.md