const fs = require('fs');
const docx = require('docx');
const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell, HeadingLevel, WidthType } = docx;

// Helper to create bold cells
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
                
                P("Bước 1: Xác định thực thể chính và định danh thực thể", true),
                Bullet("Thực thể Khách hàng (Customer): Định danh bằng Mã khách hàng (MaKH)."),
                Bullet("Thực thể Tài khoản (Account): Định danh bằng Số tài khoản (SoTK)."),
                Bullet("Thực thể Giao dịch (Transaction): Định danh bằng Mã giao dịch (MaGD)."),
                Bullet("Thực thể Chi nhánh (Branch): Định danh bằng Mã chi nhánh (MaChiNhanh)."),
                Bullet("Thực thể Nhân viên (Employee): Định danh bằng Mã nhân viên (MaNV)."),
                P(""),
                
                P("Bước 2: Xác định quan hệ giữa các thực thể", true),
                Bullet("Quan hệ (Khách hàng - Tài khoản): Một khách hàng có thể mở nhiều tài khoản (1-N)."),
                Bullet("Quan hệ (Tài khoản - Giao dịch): Một tài khoản có thể thực hiện nhiều giao dịch gửi và nhận (1-N)."),
                Bullet("Quan hệ (Chi nhánh - Khách hàng): Một chi nhánh quản lý nhiều khách hàng đăng ký tại chi nhánh đó (1-N)."),
                Bullet("Quan hệ (Chi nhánh - Nhân viên): Một chi nhánh có nhiều nhân viên trực thuộc (1-N)."),
                P(""),

                P("Bước 3: Gắn các thuộc tính mô tả cho các thực thể", true),
                Bullet("Khách hàng: MaKH, CCCD, HoTen, SoDienThoai, Email, DiaChi, MatKhau."),
                Bullet("Tài khoản: SoTK, SoDu, TrangThai."),
                Bullet("Giao dịch: MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich."),
                Bullet("Chi nhánh: MaChiNhanh, TenChiNhanh, DiaChiCN."),
                Bullet("Nhân viên: MaNV, HoTen, ChucVu, Luong, TrangThai."),
                P(""),

                P("B. CHUYỂN BIỂU ĐỒ THỰC THỂ LIÊN KẾT THÀNH QUAN HỆ", true, HeadingLevel.HEADING_4),
                P("Dựa vào các thực thể và mối quan hệ 1-N, ta chuyển đổi sang mô hình quan hệ bằng cách thêm khóa chính của thực thể bên 1 làm khóa ngoại của thực thể bên N:"),
                Bullet("KHACH_HANG (MaKH, CCCD, HoTen, SoDienThoai, Email, DiaChi, MaChiNhanh)"),
                Bullet("TAI_KHOAN (SoTK, SoDu, TrangThai, MaKH)"),
                Bullet("GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich, SoTK_Nguon, SoTK_Dich)"),
                Bullet("CHI_NHANH (MaChiNhanh, TenChiNhanh, DiaChiCN)"),
                Bullet("NHAN_VIEN (MaNV, HoTen, ChucVu, Luong, MaChiNhanh)"),
                P(""),

                P("C. CHUẨN HÓA CÁC QUAN HỆ", true, HeadingLevel.HEADING_4),
                P("Bước 1 - Lấy danh sách thuộc tính", true),
                P("Tập hợp tất cả các thuộc tính: MaKH, CCCD, HoTen, SoDienThoai, Email, DiaChi, SoTK, SoDu, TrangThaiTK, MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich, MaChiNhanh, TenChiNhanh, DiaChiCN, MaNV, ChucVu, Luong..."),
                P(""),

                P("Bước 2 - Chuẩn hoá về dạng chuẩn 1 (1NF)", true),
                P("Dạng chuẩn 1 (1NF) yêu cầu không có thuộc tính đa trị hay thuộc tính lặp. Vì thông tin khách hàng chỉ có duy nhất 1 số điện thoại làm số tài khoản và 1 căn cước công dân, các quan hệ đã thiết lập không chứa nhóm dữ liệu lặp. => Tất cả các quan hệ đã đạt 1NF."),
                P(""),

                P("Bước 3 - Chuẩn hoá về dạng chuẩn 2 (2NF)", true),
                P("Dạng chuẩn 2 (2NF) yêu cầu mọi thuộc tính không khóa phải phụ thuộc hoàn toàn vào khóa chính (không có phụ thuộc từng phần). Vì tất cả các quan hệ KHACH_HANG, TAI_KHOAN, GIAO_DICH, CHI_NHANH, NHAN_VIEN đều có khóa chính đơn (1 thuộc tính duy nhất như MaKH, SoTK, MaGD), nên tự động đạt 2NF."),
                P(""),

                P("Bước 4 - Chuẩn hoá về dạng chuẩn 3 (3NF)", true),
                P("Dạng chuẩn 3 (3NF) yêu cầu không có phụ thuộc bắc cầu (các thuộc tính không khóa không được phụ thuộc vào một thuộc tính không khóa khác). Trong KHACH_HANG, không có thuộc tính nào suy ra thuộc tính khác ngoài việc phụ thuộc vào MaKH. Tương tự cho các bảng khác. Việc tách TAI_KHOAN ra khỏi KHACH_HANG đã ngăn chặn phụ thuộc bắc cầu nếu khách hàng đổi trạng thái tài khoản. => Đạt 3NF."),
                P(""),

                P("Bước 5 - Viết thành bản ghi logic", true),
                P("Sau khi chuẩn hóa, các bản ghi logic gồm có:"),
                Bullet("KhachHang_Logic(MaKH, CCCD, HoTen, SoDienThoai, Email, DiaChi, MaChiNhanh)"),
                Bullet("TaiKhoan_Logic(SoTK, SoDu, TrangThai, MaKH)"),
                Bullet("GiaoDich_Logic(MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich, SoTK_Nguon, SoTK_Dich)"),
                P(""),

                P("D. THỐNG NHẤT BẢN GHI LOGIC", true, HeadingLevel.HEADING_4),
                P("Tổng hợp và thống nhất toàn bộ các bản ghi logic (Khóa chính được in đậm, khóa ngoại tham chiếu đến bảng khác):"),
                Bullet("CHI_NHANH (MaChiNhanh, TenChiNhanh, DiaChiCN)"),
                Bullet("NGUOI_DUNG (MaUser, TenDangNhap, MatKhau, HoTen, Email, VaiTro) - Bảng dùng chung quản lý đăng nhập."),
                Bullet("KHACH_HANG (MaKH, CCCD, SoDienThoai, DiaChi, MaChiNhanh, MaUser)"),
                Bullet("NHAN_VIEN (MaNV, ChucVu, Luong, MaChiNhanh, MaUser)"),
                Bullet("TAI_KHOAN (SoTK, SoDu, TrangThai, MaKH)"),
                Bullet("GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich, SoTK_Nguon, SoTK_Dich)"),
                P(""),

                P("E. CHUYỂN MÔ HÌNH CSDL LOGIC SANG CSDL VẬT LÝ", true, HeadingLevel.HEADING_4),
                P("Từ các bản ghi logic, chuyển đổi sang cấu trúc bảng vật lý thực tế trên MySQL, do Hibernate ORM của Spring Boot ánh xạ:"),
                P(""),
                
                P("1. Bảng users (Lưu thông tin đăng nhập)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Mã User (Khóa chính tự tăng)"],
                    ["username", "VARCHAR(50)", "UNIQUE", "Tên đăng nhập"],
                    ["password", "VARCHAR(255)", "", "Mật khẩu (mã hóa BCrypt)"],
                    ["full_name", "VARCHAR(100)", "", "Họ tên người dùng"],
                    ["role", "VARCHAR(20)", "", "Vai trò (ROLE_CUSTOMER, ROLE_EMPLOYEE...)"]
                ]),
                P(""),

                P("2. Bảng customers (Lưu thông tin khách hàng)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK, FK", "Khóa chính, tham chiếu users.id"],
                    ["cccd", "VARCHAR(20)", "UNIQUE", "Căn cước công dân"],
                    ["phone", "VARCHAR(15)", "UNIQUE", "Số điện thoại"],
                    ["branch_id", "BIGINT", "FK", "Chi nhánh mở (Tham chiếu branches)"]
                ]),
                P(""),

                P("3. Bảng employees (Lưu thông tin nhân viên)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK, FK", "Khóa chính, tham chiếu users.id"],
                    ["position", "VARCHAR(50)", "", "Chức vụ"],
                    ["branch_id", "BIGINT", "FK", "Chi nhánh công tác (Tham chiếu branches)"]
                ]),
                P(""),

                P("4. Bảng accounts (Tài khoản thanh toán)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["account_number", "VARCHAR(20)", "UNIQUE", "Số tài khoản"],
                    ["balance", "DECIMAL(19,2)", "", "Số dư"],
                    ["customer_id", "BIGINT", "FK", "Chủ tài khoản (Tham chiếu customers)"]
                ]),
                P(""),

                P("5. Bảng transactions (Giao dịch)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["transaction_code", "VARCHAR(50)", "UNIQUE", "Mã giao dịch (VD: CK2024...)"],
                    ["amount", "DECIMAL(19,2)", "", "Số tiền"],
                    ["transaction_date", "DATETIME", "", "Thời gian giao dịch"],
                    ["from_account_id", "BIGINT", "FK", "Tài khoản nguồn"],
                    ["to_account_id", "BIGINT", "FK", "Tài khoản đích"]
                ])
            ],
        },
    ],
});

Packer.toBuffer(doc).then((buffer) => {
    fs.writeFileSync("ThietKeCSDL_ChuanHoa.docx", buffer);
    console.log("Document created successfully at ThietKeCSDL_ChuanHoa.docx");
});
