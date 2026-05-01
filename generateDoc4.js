const fs = require('fs');
const docx = require('docx');
const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell, HeadingLevel, WidthType } = docx;

function createHeaderCell(text) {
    return new TableCell({
        children: [new Paragraph({ children: [new TextRun({ text: text, bold: true, size: 24 })] })],
        shading: { fill: "D9D9D9" },
        width: { size: 100, type: WidthType.AUTO },
    });
}

function createCell(text) {
    return new TableCell({
        children: [new Paragraph({ children: [new TextRun({ text: text, size: 24 })] })],
        width: { size: 100, type: WidthType.AUTO },
    });
}

function createTable(columns, rows) {
    const tableRows = [
        new TableRow({ children: columns.map(col => createHeaderCell(col)) })
    ];
    rows.forEach(row => {
        tableRows.push(new TableRow({ children: row.map(cell => createCell(cell)) }));
    });
    return new Table({ rows: tableRows, width: { size: 100, type: WidthType.PERCENTAGE } });
}

function P(text, bold = false, heading = undefined) {
    if (heading) {
        return new Paragraph({ text: text, heading: heading });
    }
    return new Paragraph({ children: [new TextRun({ text: text, size: 24, bold: bold })] });
}

function Bullet(text) {
    return new Paragraph({ children: [new TextRun({ text: text, size: 24 })], bullet: { level: 0 } });
}

const doc = new Document({
    sections: [
        {
            properties: {},
            children: [
                P("2.4.2 Thiết kế cơ sở dữ liệu", true, HeadingLevel.HEADING_3),
                
                P("A. XÂY DỰNG BIỂU ĐỒ THỰC THỂ LIÊN KẾT", true, HeadingLevel.HEADING_4),
                P("Bước 1. Xác định thực thể chính và định danh thực thể", true),
                Bullet("Thực thể Khách hàng (Customer): Định danh bằng Mã khách hàng (MaKH)."),
                Bullet("Thực thể Tài khoản (Account): Định danh bằng Số tài khoản (SoTK)."),
                Bullet("Thực thể Giao dịch (Transaction): Định danh bằng Mã giao dịch (MaGD)."),
                Bullet("Thực thể Chi nhánh (Branch): Định danh bằng Mã chi nhánh (MaChiNhanh)."),
                Bullet("Thực thể Nhân viên (Employee): Định danh bằng Mã nhân viên (MaNV)."),
                P(""),
                
                P("Bước 2. Xác định quan hệ giữa các thực thể", true),
                Bullet("Quan hệ (Khách hàng - Tài khoản): Một khách hàng có thể mở nhiều tài khoản (1-N)."),
                Bullet("Quan hệ (Tài khoản - Giao dịch): Một tài khoản có thể thực hiện nhiều giao dịch gửi và nhận (1-N)."),
                Bullet("Quan hệ (Chi nhánh - Khách hàng): Một chi nhánh quản lý nhiều khách hàng đăng ký tại chi nhánh đó (1-N)."),
                Bullet("Quan hệ (Chi nhánh - Nhân viên): Một chi nhánh có nhiều nhân viên trực thuộc (1-N)."),
                P(""),

                P("Bước 3. Gắn các thuộc tính mô tả cho các thực thể", true),
                Bullet("Khách hàng: MaKH, CCCD, HoTen, NgaySinh, SoDienThoai, Email, DiaChi."),
                Bullet("Tài khoản: SoTK, SoDu, LoaiTK, NgayMo, TrangThai."),
                Bullet("Giao dịch: MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich."),
                Bullet("Chi nhánh: MaChiNhanh, TenChiNhanh, DiaChiCN."),
                Bullet("Nhân viên: MaNV, HoTen, ChucVu, BoPhan, Luong."),
                P(""),

                P("B. CHUYỂN BIỂU ĐỒ THỰC THỂ LIÊN KẾT THÀNH QUAN HỆ", true, HeadingLevel.HEADING_4),
                P("Dựa vào các thực thể và mối quan hệ 1-N, chuyển đổi sang mô hình quan hệ bằng cách thêm khóa chính của thực thể bên 1 làm khóa ngoại của thực thể bên N:"),
                Bullet("KHACH_HANG (MaKH, CCCD, HoTen, NgaySinh, SoDienThoai, Email, DiaChi, MaChiNhanh)"),
                Bullet("TAI_KHOAN (SoTK, SoDu, LoaiTK, NgayMo, TrangThai, MaKH)"),
                Bullet("GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich, SoTK_Nguon, SoTK_Dich)"),
                Bullet("CHI_NHANH (MaChiNhanh, TenChiNhanh, DiaChiCN)"),
                Bullet("NHAN_VIEN (MaNV, HoTen, ChucVu, BoPhan, Luong, MaChiNhanh)"),
                P(""),

                P("C. CHUẨN HÓA CÁC QUAN HỆ", true, HeadingLevel.HEADING_4),
                P("+ Xác định tập thuộc tính", true),
                Bullet("Thuộc tính: MaKH, CCCD, HoTenKH, NgaySinh, SDT_KH, Email, DiaChi, SoTK, SoDu, LoaiTK, NgayMo, TrangThaiTK, MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich, MaChiNhanh, TenCN, DiaChiCN, MaNV, HoTenNV, ChucVu, BoPhan, Luong."),
                Bullet("Thuộc tính lặp: Không có (mỗi khách hàng có 1 SĐT, 1 CCCD duy nhất)."),
                P(""),
                
                P("+ Gom các thuộc tính thành một quan hệ R", true),
                P("R(MaKH, CCCD, HoTenKH, NgaySinh, SDT_KH, Email, DiaChi, SoTK, SoDu, LoaiTK, NgayMo, TrangThaiTK, MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich, MaChiNhanh, TenCN, DiaChiCN, MaNV, HoTenNV, ChucVu, BoPhan, Luong)"),
                P(""),

                P("+ Xác định tập phụ thuộc hàm và khóa R", true),
                Bullet("Khóa của R: {MaKH, SoTK, MaGD, MaChiNhanh, MaNV} (Khóa tổ hợp)."),
                P("Tập phụ thuộc hàm (F):"),
                Bullet("F1: MaKH -> CCCD, HoTenKH, NgaySinh, SDT_KH, Email, DiaChi, MaChiNhanh"),
                Bullet("F2: SoTK -> SoDu, LoaiTK, NgayMo, TrangThaiTK, MaKH"),
                Bullet("F3: MaGD -> SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich"),
                Bullet("F4: MaChiNhanh -> TenCN, DiaChiCN"),
                Bullet("F5: MaNV -> HoTenNV, ChucVu, BoPhan, Luong, MaChiNhanh"),
                P(""),

                P("+ Vẽ đồ thị phụ thuộc hàm", true),
                P("(Ghi chú: Đồ thị thể hiện các mũi tên chỉ chiều phụ thuộc từ các thuộc tính khóa sang các thuộc tính mô tả tương ứng)"),
                P(" MaKH ------------------------> {CCCD, HoTenKH, NgaySinh, SDT_KH, DiaChi, MaChiNhanh}"),
                P(" SoTK ------------------------> {SoDu, LoaiTK, NgayMo, TrangThaiTK, MaKH}"),
                P(" MaGD ------------------------> {SoTien, ThoiGian, LoaiGD, SoTK_Nguon, SoTK_Dich}"),
                P(" MaChiNhanh ------------------> {TenCN, DiaChiCN}"),
                P(" MaNV ------------------------> {HoTenNV, ChucVu, BoPhan, Luong, MaChiNhanh}"),
                P(""),

                P("+ Kẻ bảng chuẩn hóa", true),
                createTable(["0NF", "1NF", "2NF", "3NF", "Quan hệ"], [
                    ["R(Toàn bộ thuộc tính)", "R (Không có TT lặp/đa trị)", "KHACH_HANG", "KHACH_HANG", "KHACH_HANG (MaKH, CCCD, HoTenKH, SDT_KH, Email, DiaChi, NgaySinh, MaChiNhanh)"],
                    ["", "", "TAI_KHOAN", "TAI_KHOAN", "TAI_KHOAN (SoTK, SoDu, LoaiTK, NgayMo, TrangThaiTK, MaKH)"],
                    ["", "", "GIAO_DICH", "GIAO_DICH", "GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich)"],
                    ["", "", "CHI_NHANH", "CHI_NHANH", "CHI_NHANH (MaChiNhanh, TenCN, DiaChiCN)"],
                    ["", "", "NHAN_VIEN", "NHAN_VIEN", "NHAN_VIEN (MaNV, HoTenNV, ChucVu, BoPhan, Luong, MaChiNhanh)"]
                ]),
                P(""),

                P("+ Bản ghi logic", true),
                Bullet("KHACH_HANG (MaKH, CCCD, HoTenKH, SDT_KH, Email, DiaChi, NgaySinh, MaChiNhanh)"),
                Bullet("TAI_KHOAN (SoTK, SoDu, LoaiTK, NgayMo, TrangThaiTK, MaKH)"),
                Bullet("GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich)"),
                Bullet("CHI_NHANH (MaChiNhanh, TenCN, DiaChiCN)"),
                Bullet("NHAN_VIEN (MaNV, HoTenNV, ChucVu, BoPhan, Luong, MaChiNhanh)"),
                P(""),

                P("D. THỐNG NHẤT BẢN GHI LOGIC", true, HeadingLevel.HEADING_4),
                P("Thống nhất toàn bộ các bản ghi logic. Hệ thống tách thêm bảng USERS để quản lý đăng nhập chung:"),
                Bullet("USERS (id, username, password, full_name, email, role, status)"),
                Bullet("BRANCHES (id, ma_chi_nhanh, branch_name, branch_address)"),
                Bullet("CUSTOMERS (id(FK), makh, cccd, phone, address, ngay_sinh, branch_id(FK))"),
                Bullet("EMPLOYEES (id(FK), manv, position, bo_phan, salary, branch_id(FK))"),
                Bullet("ACCOUNTS (id, account_number, balance, loai_tk, ngay_mo, status, customer_id(FK))"),
                Bullet("TRANSACTIONS (id, magd, amount, timestamp, content, transaction_type, from_account_id(FK), to_account_id(FK), performed_by)"),
                P(""),

                P("E. CHUYỂN MÔ HÌNH CSDL LOGIC SANG CSDL VẬT LÝ", true, HeadingLevel.HEADING_4),
                P("Dựa vào SQL Schema thực tế (db_dump.sql), các bảng vật lý trên MySQL được Hibernate ORM ánh xạ như sau:"),
                P(""),

                P("1. Bảng users (Tài khoản truy cập)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT(20)", "PK", "Khóa chính tự tăng"],
                    ["username", "VARCHAR(255)", "UNIQUE", "Tên đăng nhập"],
                    ["password", "VARCHAR(255)", "", "Mật khẩu BCrypt"],
                    ["full_name", "VARCHAR(255)", "", "Họ tên người dùng"],
                    ["email", "VARCHAR(255)", "", "Email"],
                    ["role", "VARCHAR(255)", "", "Vai trò (ROLE_CUSTOMER...)"],
                    ["status", "VARCHAR(255)", "", "Trạng thái (ACTIVE, LOCKED)"]
                ]),
                P(""),

                P("2. Bảng branches (Chi nhánh)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT(20)", "PK", "Khóa chính"],
                    ["ma_chi_nhanh", "VARCHAR(50)", "UNIQUE", "Mã chi nhánh nghiệp vụ"],
                    ["branch_name", "VARCHAR(255)", "", "Tên chi nhánh"],
                    ["branch_address", "VARCHAR(255)", "", "Địa chỉ chi nhánh"]
                ]),
                P(""),

                P("3. Bảng customers (Hồ sơ khách hàng)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT(20)", "PK, FK", "Tham chiếu users.id"],
                    ["makh", "VARCHAR(255)", "UNIQUE", "Mã khách hàng"],
                    ["cccd", "VARCHAR(255)", "UNIQUE", "Căn cước công dân"],
                    ["phone", "VARCHAR(255)", "", "Số điện thoại"],
                    ["address", "VARCHAR(255)", "", "Địa chỉ"],
                    ["ngay_sinh", "DATE", "", "Ngày sinh"],
                    ["branch_id", "BIGINT(20)", "FK", "Tham chiếu branches.id"]
                ]),
                P(""),

                P("4. Bảng employees (Hồ sơ nhân viên)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT(20)", "PK, FK", "Tham chiếu users.id"],
                    ["manv", "VARCHAR(255)", "UNIQUE", "Mã nhân viên"],
                    ["position", "VARCHAR(255)", "", "Chức vụ"],
                    ["bo_phan", "VARCHAR(255)", "", "Bộ phận công tác"],
                    ["salary", "DECIMAL(38,2)", "", "Mức lương"],
                    ["branch_id", "BIGINT(20)", "FK", "Tham chiếu branches.id"]
                ]),
                P(""),

                P("5. Bảng accounts (Tài khoản ngân hàng)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT(20)", "PK", "Khóa chính"],
                    ["account_number", "VARCHAR(255)", "UNIQUE", "Số tài khoản"],
                    ["balance", "DECIMAL(38,2)", "", "Số dư"],
                    ["loai_tk", "VARCHAR(50)", "", "Loại (THANH_TOAN)"],
                    ["ngay_mo", "DATE", "", "Ngày mở tài khoản"],
                    ["status", "VARCHAR(255)", "", "Trạng thái hoạt động"],
                    ["customer_id", "BIGINT(20)", "FK", "Tham chiếu customers.id"]
                ]),
                P(""),

                P("6. Bảng transactions (Lịch sử giao dịch)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT(20)", "PK", "Khóa chính"],
                    ["magd", "VARCHAR(255)", "UNIQUE", "Mã giao dịch (VD: CK...)"],
                    ["amount", "DECIMAL(38,2)", "", "Số tiền"],
                    ["transaction_type", "VARCHAR(255)", "", "Loại (TRANSFER, DEPOSIT)"],
                    ["timestamp", "DATETIME(6)", "", "Thời gian giao dịch"],
                    ["content", "VARCHAR(255)", "", "Nội dung giao dịch"],
                    ["from_account_id", "BIGINT(20)", "FK", "Tham chiếu accounts.id"],
                    ["to_account_id", "BIGINT(20)", "FK", "Tham chiếu accounts.id"],
                    ["performed_by", "VARCHAR(255)", "", "Tên nhân viên (nếu giao dịch tại quầy)"]
                ])
            ],
        },
    ],
});

Packer.toBuffer(doc).then((buffer) => {
    fs.writeFileSync("ThietKeCSDL_SQL_Real.docx", buffer);
    console.log("Document created successfully at ThietKeCSDL_SQL_Real.docx");
});
