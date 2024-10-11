DROP SCHEMA `QuanLiSanbay`;
CREATE SCHEMA `QuanLiSanBay`;
USE QuanLiSanBay;

CREATE TABLE KhachHang (
	MaKH CHAR(8) NOT NULL PRIMARY KEY,
	SDT VARCHAR (10) CHECK (SDT REGEXP '^[0-9]{10}$'), 
	HoDem VARCHAR(30), 
	Ten VARCHAR(20),
	DiaChi VARCHAR(80)
);

CREATE TABLE NhanVien (
	MaNV CHAR(8) NOT NULL PRIMARY KEY,
	DiaChi VARCHAR (80) NOT NULL,
	HoDem VARCHAR(30) ,
    SDT VARCHAR (10) CHECK (SDT REGEXP '^[0-9]{10}$'),
	Ten VARCHAR (20),
    Luong DECIMAL(18, 2),
    LoaiNV VARCHAR(40) DEFAULT 'Tiếp viên'
);


CREATE TABLE LoaiMayBay (
	MaLoai INT NOT NULL ,
	HangSanXuat VARCHAR (80),
	PRIMARY KEY (MaLoai)
);

CREATE TABLE MayBay (
    SoHieu CHAR(10) PRIMARY KEY,
    MaLoai INT,
	SoGheNgoi INT ,
    FOREIGN KEY (MaLoai) REFERENCES LoaiMayBay(MaLoai)
);

CREATE TABLE ChuyenBay (
	MaChuyenBay CHAR(8) NOT NULL PRIMARY KEY ,
	TenSanBayDi VARCHAR(50),
	TenSanBayDen VARCHAR(50),
	GioDi DATETIME ,
	GioDen DATETIME
);


CREATE TABLE LichBay (
    NgayDi DATE,
    MaChuyenBay CHAR(8),
    SoHieu CHAR(10),
    MaLoai INT,
    PRIMARY KEY (NgayDi, MaChuyenBay),
    FOREIGN KEY (MaChuyenBay) REFERENCES ChuyenBay(MaChuyenBay),
    FOREIGN KEY (SoHieu) REFERENCES MayBay(SoHieu),
    FOREIGN KEY (MaLoai) REFERENCES LoaiMayBay(MaLoai)
);

CREATE TABLE DatCho (
    MaKH CHAR(8),
    NgayDi DATE,
    MaChuyenBay CHAR(8),
    PRIMARY KEY (MAKH, NgayDi, MaChuyenBay),
    FOREIGN KEY (MAKH) REFERENCES KhachHang(MAKH),
    FOREIGN KEY (NgayDi, MaChuyenBay) REFERENCES LichBay(NgayDi, MaChuyenBay)
);

CREATE TABLE PhanCong (
    MaNV CHAR(8),
    NgayDi DATE,
    MaChuyenBay CHAR(8),
    PRIMARY KEY (MaNV, NgayDi, MaChuyenBay),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (NgayDi, MaChuyenBay) REFERENCES LICHBAY(NgayDi, MaChuyenBay)
);

-- Tạo bảng quản lý admin
CREATE TABLE Admins (
    UserName VARCHAR(50) NOT NULL PRIMARY KEY,
    PasswordHash CHAR(32) NOT NULL
);

-- Hàm để mã hóa mật khẩu bằng MD5
DELIMiTER //
CREATE FUNCTION HashPassword (password VARCHAR(255))
RETURNS CHAR(32)
DETERMINISTIC
BEGIN
    RETURN LOWER(MD5(password));
END; //
DELIMITER ;
-- Ví dụ cách thêm tài khoản admin với mật khẩu được mã hóa bằng MD5
INSERT INTO Admins (UserName, PasswordHash)
VALUES 
('admin', HashPassword('yourpassword'));

