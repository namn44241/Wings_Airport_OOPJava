# Wings_Airport_OOPJava File Structure

Wings_Airport_OOPJava/
├── .idea/
│   ├── profiles_setting.xml/
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
│   │       │           │   ├── AdminController.java
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
│   │       │           │   ├── PlaneTypeController.java
│   │       │           │   └── SanBayController.java
│   │       │           ├── model/
│   │       │           │   ├── EmailRequest.java
│   │       │           │   ├── EmailResponse.java
│   │       │           │   └── PhanCong.java
│   │       │           └── QuanlisanbayApplication.java
│   │       └── resources/
│   │           ├── static/
│   │           │   ├── css/
│   │           │   │   ├── styles_admin.css                
│   │           │   │   └── styles_sanbay.css
│   │           │   ├── images/
│   │           │   ├── js/
│   │           │   │   └── sanbay/
│   │           │   │       ├── alert-noti.js
│   │           │   │       ├── an-khi-tat.js
│   │           │   │       ├── dom-preload.js
│   │           │   │       ├── dom-xoay.js
│   │           │   │       ├── fe-link-be.js
│   │           │   │       ├── language-switcher.js
│   │           │   │       ├── menu-helplink-languages.js
│   │           │   │       ├── send-mail.js
│   │           │   │       ├── slide-khampha.js   
│   │           │   │       ├── slide-anhto.js
│   │           │   │       ├── slide-khampha.js   
│   │           │   │       └── tin-tuc.js 
│   │           │   └── lang/
│   │           │       ├── english.json
│   │           │       └── tieng-viet.json
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
├── mvnw.cmd
├── pom.xml
├── readme-hdsd.md
└── README.md