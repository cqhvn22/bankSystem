const fs = require('fs');
const docx = require('docx');
const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell, HeadingLevel, WidthType, BorderStyle } = docx;

// Helper func to create a bold header cell
function createHeaderCell(text) {
    return new TableCell({
        children: [new Paragraph({ children: [new TextRun({ text: text, bold: true, size: 24 })] })],
        shading: { fill: "D9D9D9" },
        width: { size: 100, type: WidthType.AUTO },
    });
}

// Helper func to create a regular cell
function createCell(text) {
    return new TableCell({
        children: [new Paragraph({ children: [new TextRun({ text: text, size: 24 })] })],
        width: { size: 100, type: WidthType.AUTO },
    });
}

// Helper to create a table from columns and rows
function createTable(columns, rows) {
    const tableRows = [];
    
    // Header Row
    tableRows.push(new TableRow({
        children: columns.map(col => createHeaderCell(col))
    }));

    // Data Rows
    rows.forEach(row => {
        tableRows.push(new TableRow({
            children: row.map(cell => createCell(cell))
        }));
    });

    return new Table({
        rows: tableRows,
        width: { size: 100, type: WidthType.PERCENTAGE },
    });
}

const doc = new Document({
    sections: [
        {
            properties: {},
            children: [
                new Paragraph({
                    text: "2.4.2 Thiết kế cơ sở dữ liệu",
                    heading: HeadingLevel.HEADING_3,
                }),
                new Paragraph({
                    children: [
                        new TextRun({
                            text: "Thiết kế cơ sở dữ liệu đóng vai trò then chốt trong việc đảm bảo hệ thống vận hành trơn tru, lưu trữ dữ liệu an toàn và đáp ứng tốc độ truy xuất cao. Hệ thống sử dụng hệ quản trị cơ sở dữ liệu quan hệ MySQL, kết hợp với ORM (Hibernate) để tự động sinh các bảng vật lý từ Entity của Spring Boot.",
                            size: 24
                        })
                    ]
                }),
                new Paragraph({ text: "" }), // empty line
                
                new Paragraph({
                    text: "a) Thiết kế bảng Users (Người dùng hệ thống)",
                    heading: HeadingLevel.HEADING_4,
                }),
                new Paragraph({
                    children: [new TextRun({ text: "Bảng này chứa thông tin đăng nhập và phân quyền chung cho mọi đối tượng trong hệ thống (Khách hàng, Nhân viên, Quản trị viên).", size: 24 })]
                }),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính tự tăng"],
                    ["username", "VARCHAR(50)", "UNIQUE", "Tên đăng nhập"],
                    ["password", "VARCHAR(255)", "", "Mật khẩu (đã mã hóa BCrypt)"],
                    ["full_name", "VARCHAR(100)", "", "Họ và tên đầy đủ"],
                    ["email", "VARCHAR(100)", "", "Địa chỉ email liên hệ"],
                    ["role", "VARCHAR(20)", "", "Vai trò (ROLE_CUSTOMER, ROLE_EMPLOYEE...)"],
                    ["status", "VARCHAR(20)", "", "Trạng thái (ACTIVE, LOCKED)"]
                ]),
                new Paragraph({ text: "" }),

                new Paragraph({
                    text: "b) Thiết kế bảng Customers (Khách hàng)",
                    heading: HeadingLevel.HEADING_4,
                }),
                new Paragraph({
                    children: [new TextRun({ text: "Bảng lưu trữ thông tin chi tiết của khách hàng. Bảng này liên kết 1-1 với bảng Users (Khách hàng cũng là một người dùng).", size: 24 })]
                }),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK, FK", "Khóa chính, tham chiếu tới users.id"],
                    ["cccd", "VARCHAR(20)", "UNIQUE", "Số Căn cước công dân"],
                    ["phone", "VARCHAR(15)", "UNIQUE", "Số điện thoại đăng ký"],
                    ["address", "VARCHAR(255)", "", "Địa chỉ cư trú"],
                    ["branch_id", "BIGINT", "FK", "Mã chi nhánh mở tài khoản (Tham chiếu branches)"]
                ]),
                new Paragraph({ text: "" }),

                new Paragraph({
                    text: "c) Thiết kế bảng Employees (Nhân viên ngân hàng)",
                    heading: HeadingLevel.HEADING_4,
                }),
                new Paragraph({
                    children: [new TextRun({ text: "Lưu thông tin chi tiết của nhân viên nội bộ, liên kết 1-1 với bảng Users.", size: 24 })]
                }),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK, FK", "Khóa chính, tham chiếu tới users.id"],
                    ["position", "VARCHAR(50)", "", "Chức vụ (Giao dịch viên, Quản lý...)"],
                    ["salary", "DECIMAL(15,2)", "", "Mức lương nhân viên"],
                    ["branch_id", "BIGINT", "FK", "Chi nhánh công tác (Tham chiếu branches)"]
                ]),
                new Paragraph({ text: "" }),

                new Paragraph({
                    text: "d) Thiết kế bảng Accounts (Tài khoản ngân hàng)",
                    heading: HeadingLevel.HEADING_4,
                }),
                new Paragraph({
                    children: [new TextRun({ text: "Bảng lõi lưu trữ số dư và thông tin tài khoản ngân hàng của khách hàng.", size: 24 })]
                }),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["account_number", "VARCHAR(20)", "UNIQUE", "Số tài khoản (Lấy từ số điện thoại)"],
                    ["balance", "DECIMAL(19,2)", "", "Số dư hiện tại (Mặc định 0.00)"],
                    ["status", "VARCHAR(20)", "", "Trạng thái (ACTIVE, BLOCKED, CLOSED)"],
                    ["customer_id", "BIGINT", "FK", "Chủ tài khoản (Tham chiếu customers.id)"]
                ]),
                new Paragraph({ text: "" }),

                new Paragraph({
                    text: "e) Thiết kế bảng Transactions (Giao dịch)",
                    heading: HeadingLevel.HEADING_4,
                }),
                new Paragraph({
                    children: [new TextRun({ text: "Lưu lại toàn bộ biến động số dư. Bảng này cực kỳ quan trọng, đảm bảo đối soát dữ liệu (Audit Trail).", size: 24 })]
                }),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["transaction_code", "VARCHAR(50)", "UNIQUE", "Mã giao dịch (VD: CK20240428...)"],
                    ["amount", "DECIMAL(19,2)", "", "Số tiền giao dịch"],
                    ["transaction_date", "DATETIME", "", "Thời gian giao dịch"],
                    ["type", "VARCHAR(20)", "", "Loại (DEPOSIT, WITHDRAW, TRANSFER)"],
                    ["from_account_id", "BIGINT", "FK", "Tài khoản nguồn (Tham chiếu accounts)"],
                    ["to_account_id", "BIGINT", "FK", "Tài khoản đích (Tham chiếu accounts)"],
                    ["performed_by", "VARCHAR(50)", "", "Tên nhân viên thực hiện (Nếu GD tại quầy)"],
                    ["description", "VARCHAR(255)", "", "Nội dung giao dịch"]
                ]),
                new Paragraph({ text: "" }),

                new Paragraph({
                    text: "f) Thiết kế bảng Branches (Chi nhánh)",
                    heading: HeadingLevel.HEADING_4,
                }),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["name", "VARCHAR(100)", "", "Tên chi nhánh"],
                    ["code", "VARCHAR(20)", "UNIQUE", "Mã định danh chi nhánh"],
                    ["address", "VARCHAR(255)", "", "Địa chỉ chi nhánh"]
                ]),
                new Paragraph({ text: "" }),

                new Paragraph({
                    text: "g) Thiết kế bảng Registration_Requests (Yêu cầu mở tài khoản)",
                    heading: HeadingLevel.HEADING_4,
                }),
                new Paragraph({
                    children: [new TextRun({ text: "Lưu trữ các đơn đăng ký mở tài khoản trực tuyến chờ duyệt.", size: 24 })]
                }),
                createTable(["Tên trường", "Kiểu dữ liệu", "Khóa", "Mô tả"], [
                    ["id", "BIGINT", "PK", "Khóa chính"],
                    ["full_name", "VARCHAR(100)", "", "Họ tên người đăng ký"],
                    ["phone_number", "VARCHAR(15)", "", "Số điện thoại"],
                    ["cccd", "VARCHAR(20)", "", "Căn cước công dân"],
                    ["status", "VARCHAR(20)", "", "Trạng thái (PENDING, APPROVED, REJECTED)"],
                    ["branch_id", "BIGINT", "FK", "Chi nhánh xử lý hồ sơ"]
                ])
            ],
        },
    ],
});

Packer.toBuffer(doc).then((buffer) => {
    fs.writeFileSync("ThietKeCSDL.docx", buffer);
    console.log("Document created successfully at ThietKeCSDL.docx");
});
