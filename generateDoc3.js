const fs = require('fs');
const docx = require('docx');
const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell, HeadingLevel, WidthType, BorderStyle } = docx;

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
                Bullet("Khách hàng: MaKH, CCCD, HoTen, SoDienThoai, Email, DiaChi."),
                Bullet("Tài khoản: SoTK, SoDu, TrangThai."),
                Bullet("Giao dịch: MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich."),
                Bullet("Chi nhánh: MaChiNhanh, TenChiNhanh, DiaChiCN."),
                Bullet("Nhân viên: MaNV, HoTen, ChucVu, Luong."),
                P(""),

                P("B. CHUYỂN BIỂU ĐỒ THỰC THỂ LIÊN KẾT THÀNH QUAN HỆ", true, HeadingLevel.HEADING_4),
                P("Dựa vào các thực thể và mối quan hệ 1-N, chuyển đổi sang mô hình quan hệ bằng cách thêm khóa chính của thực thể bên 1 làm khóa ngoại của thực thể bên N:"),
                Bullet("KHACH_HANG (MaKH, CCCD, HoTen, SoDienThoai, Email, DiaChi, MaChiNhanh)"),
                Bullet("TAI_KHOAN (SoTK, SoDu, TrangThai, MaKH)"),
                Bullet("GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGiaoDich, SoTK_Nguon, SoTK_Dich)"),
                Bullet("CHI_NHANH (MaChiNhanh, TenChiNhanh, DiaChiCN)"),
                Bullet("NHAN_VIEN (MaNV, HoTen, ChucVu, Luong, MaChiNhanh)"),
                P(""),

                P("C. CHUẨN HÓA CÁC QUAN HỆ", true, HeadingLevel.HEADING_4),
                P("+ Xác định tập thuộc tính", true),
                Bullet("Thuộc tính: MaKH, CCCD, HoTenKH, SDT_KH, Email, DiaChi, SoTK, SoDu, TrangThaiTK, MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich, MaChiNhanh, TenCN, DiaChiCN, MaNV, HoTenNV, ChucVu, Luong."),
                Bullet("Thuộc tính lặp: Không có (mỗi khách hàng có 1 SĐT, 1 CCCD duy nhất đăng ký)."),
                P(""),
                
                P("+ Gom các thuộc tính thành một quan hệ R", true),
                P("R(MaKH, CCCD, HoTenKH, SDT_KH, Email, DiaChi, SoTK, SoDu, TrangThaiTK, MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich, MaChiNhanh, TenCN, DiaChiCN, MaNV, HoTenNV, ChucVu, Luong)"),
                P(""),

                P("+ Xác định tập phụ thuộc hàm và khóa R", true),
                Bullet("Khóa của R: {MaKH, SoTK, MaGD, MaChiNhanh, MaNV} (Khóa tổ hợp)."),
                P("Tập phụ thuộc hàm (F):"),
                Bullet("F1: MaKH -> CCCD, HoTenKH, SDT_KH, Email, DiaChi, MaChiNhanh"),
                Bullet("F2: SoTK -> SoDu, TrangThaiTK, MaKH"),
                Bullet("F3: MaGD -> SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich"),
                Bullet("F4: MaChiNhanh -> TenCN, DiaChiCN"),
                Bullet("F5: MaNV -> HoTenNV, ChucVu, Luong, MaChiNhanh"),
                P(""),

                P("+ Vẽ đồ thị phụ thuộc hàm", true),
                P("(Ghi chú: Đồ thị thể hiện các mũi tên chỉ chiều phụ thuộc từ các thuộc tính khóa sang các thuộc tính mô tả tương ứng)"),
                P(" MaKH ------------------------> {CCCD, HoTenKH, SDT_KH, DiaChi, MaChiNhanh}"),
                P(" SoTK ------------------------> {SoDu, TrangThaiTK, MaKH}"),
                P(" MaGD ------------------------> {SoTien, ThoiGian, LoaiGD, SoTK_Nguon, SoTK_Dich}"),
                P(" MaChiNhanh ------------------> {TenCN, DiaChiCN}"),
                P(" MaNV ------------------------> {HoTenNV, ChucVu, Luong, MaChiNhanh}"),
                P(""),

                P("+ Kẻ bảng chuẩn hóa", true),
                createTable(["0NF", "1NF", "2NF", "3NF", "Quan hệ"], [
                    ["R(Tất cả thuộc tính)", "R (Không có TT lặp/đa trị)", "KHACH_HANG", "KHACH_HANG", "KHACH_HANG (MaKH, CCCD, HoTenKH, SDT_KH, DiaChi, MaChiNhanh)"],
                    ["", "", "TAI_KHOAN", "TAI_KHOAN", "TAI_KHOAN (SoTK, SoDu, TrangThaiTK, MaKH)"],
                    ["", "", "GIAO_DICH", "GIAO_DICH", "GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich)"],
                    ["", "", "CHI_NHANH", "CHI_NHANH", "CHI_NHANH (MaChiNhanh, TenCN, DiaChiCN)"],
                    ["", "", "NHAN_VIEN", "NHAN_VIEN", "NHAN_VIEN (MaNV, HoTenNV, ChucVu, Luong, MaChiNhanh)"]
                ]),
                P(""),

                P("+ Bản ghi logic", true),
                Bullet("KHACH_HANG (MaKH, CCCD, HoTenKH, SDT_KH, Email, DiaChi, MaChiNhanh)"),
                Bullet("TAI_KHOAN (SoTK, SoDu, TrangThaiTK, MaKH)"),
                Bullet("GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon, SoTK_Dich)"),
                Bullet("CHI_NHANH (MaChiNhanh, TenCN, DiaChiCN)"),
                Bullet("NHAN_VIEN (MaNV, HoTenNV, ChucVu, Luong, MaChiNhanh)"),
                P(""),

                P("D. THỐNG NHẤT BẢN GHI LOGIC", true, HeadingLevel.HEADING_4),
                P("Các bản ghi logic sau chuẩn hóa với Khóa chính và Khóa ngoại:"),
                Bullet("CHI_NHANH (MaChiNhanh, TenCN, DiaChiCN)"),
                Bullet("KHACH_HANG (MaKH, CCCD, HoTenKH, SDT_KH, Email, DiaChi, MaChiNhanh(FK))"),
                Bullet("NHAN_VIEN (MaNV, HoTenNV, ChucVu, Luong, MaChiNhanh(FK))"),
                Bullet("TAI_KHOAN (SoTK, SoDu, TrangThaiTK, MaKH(FK))"),
                Bullet("GIAO_DICH (MaGD, SoTien, ThoiGian, NoiDung, LoaiGD, SoTK_Nguon(FK), SoTK_Dich(FK))"),
                P(""),

                P("E. CHUYỂN MÔ HÌNH CSDL LOGIC SANG CSDL VẬT LÝ", true, HeadingLevel.HEADING_4),
                P("Chuyển đổi thành các bảng vật lý trên MySQL (ORM Hibernate):"),
                P(""),

                P("1. Bảng customers (Lưu thông tin khách hàng)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["cccd", "VARCHAR(20)", "UNIQUE", "Căn cước công dân"],
                    ["full_name", "VARCHAR(100)", "", "Họ tên"],
                    ["phone", "VARCHAR(15)", "UNIQUE", "Số điện thoại"],
                    ["branch_id", "BIGINT", "FK", "Chi nhánh mở (Tham chiếu branches)"]
                ]),
                P(""),

                P("2. Bảng accounts (Tài khoản thanh toán)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["account_number", "VARCHAR(20)", "UNIQUE", "Số tài khoản"],
                    ["balance", "DECIMAL(19,2)", "", "Số dư"],
                    ["customer_id", "BIGINT", "FK", "Chủ tài khoản (Tham chiếu customers)"]
                ]),
                P(""),

                P("3. Bảng transactions (Giao dịch)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["transaction_code", "VARCHAR(50)", "UNIQUE", "Mã giao dịch"],
                    ["amount", "DECIMAL(19,2)", "", "Số tiền"],
                    ["transaction_date", "DATETIME", "", "Thời gian giao dịch"],
                    ["from_account_id", "BIGINT", "FK", "Tài khoản nguồn"],
                    ["to_account_id", "BIGINT", "FK", "Tài khoản đích"]
                ]),
                P(""),

                P("4. Bảng branches (Chi nhánh)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["name", "VARCHAR(100)", "", "Tên chi nhánh"],
                    ["code", "VARCHAR(20)", "UNIQUE", "Mã định danh chi nhánh"],
                    ["address", "VARCHAR(255)", "", "Địa chỉ chi nhánh"]
                ]),
                P(""),

                P("5. Bảng employees (Nhân viên ngân hàng)", true),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["full_name", "VARCHAR(100)", "", "Họ tên"],
                    ["position", "VARCHAR(50)", "", "Chức vụ"],
                    ["salary", "DECIMAL(15,2)", "", "Mức lương"],
                    ["branch_id", "BIGINT", "FK", "Chi nhánh công tác (Tham chiếu branches)"]
                ])
            ],
        },
    ],
});

Packer.toBuffer(doc).then((buffer) => {
    fs.writeFileSync("ThietKeCSDL_SieuChuan.docx", buffer);
    console.log("Document created successfully at ThietKeCSDL_SieuChuan.docx");
});
