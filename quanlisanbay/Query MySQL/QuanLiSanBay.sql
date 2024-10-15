-- Chọn CSDL ở thanh bên trái

-- Tạo bảng KhachHang
CREATE TABLE KhachHang (
    MaKH CHAR(8) NOT NULL PRIMARY KEY,
    SDT VARCHAR(10) CHECK (SDT REGEXP '^[0-9]{10}$'), 
    HoDem VARCHAR(30), 
    Ten VARCHAR(20),
    DiaChi VARCHAR(80)
);

-- Tạo bảng NhanVien
CREATE TABLE NhanVien (
    MaNV CHAR(8) NOT NULL PRIMARY KEY,
    DiaChi VARCHAR(80) NOT NULL,
    HoDem VARCHAR(30),
    SDT VARCHAR(10) CHECK (SDT REGEXP '^[0-9]{10}$'),
    Ten VARCHAR(20),
    Luong DECIMAL(18, 2),
    LoaiNV VARCHAR(40) DEFAULT 'Tiếp viên'
);

-- Tạo bảng LoaiMayBay
CREATE TABLE LoaiMayBay (
    MaLoai INT NOT NULL,
    HangSanXuat VARCHAR(80),
    PRIMARY KEY (MaLoai)
);

-- Tạo bảng MayBay
CREATE TABLE MayBay (
    SoHieu CHAR(10) PRIMARY KEY,
    MaLoai INT,
    SoGheNgoi INT,
    FOREIGN KEY (MaLoai) REFERENCES LoaiMayBay(MaLoai)
);

-- Tạo bảng ChuyenBay
CREATE TABLE ChuyenBay (
    MaChuyenBay CHAR(8) NOT NULL PRIMARY KEY,
    TenSanBayDi VARCHAR(50),
    TenSanBayDen VARCHAR(50),
    GioDi DATETIME,
    GioDen DATETIME
);

-- Tạo bảng LichBay
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

-- Tạo bảng DatCho
CREATE TABLE DatCho (
    MaKH CHAR(8),
    NgayDi DATE,
    MaChuyenBay CHAR(8),
    PRIMARY KEY (MaKH, NgayDi, MaChuyenBay),
    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (NgayDi, MaChuyenBay) REFERENCES LichBay(NgayDi, MaChuyenBay)
);

-- Tạo bảng PhanCong
CREATE TABLE PhanCong (
    MaNV CHAR(8),
    NgayDi DATE,
    MaChuyenBay CHAR(8),
    PRIMARY KEY (MaNV, NgayDi, MaChuyenBay),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (NgayDi, MaChuyenBay) REFERENCES LichBay(NgayDi, MaChuyenBay)
);

-- Tạo bảng quản lý tài khoản trang admin
CREATE TABLE Admins (
    UserName VARCHAR(50) NOT NULL PRIMARY KEY,
    PasswordHash CHAR(64) NOT NULL
);

DELIMITER //

CREATE FUNCTION HashPassword(password VARCHAR(255))
RETURNS VARCHAR(255)
DETERMINISTIC
BEGIN
    RETURN SHA2(password, 256);
END //

DELIMITER ;

