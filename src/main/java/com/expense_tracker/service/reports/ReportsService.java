package com.expense_tracker.service.reports;

import com.expense_tracker.model.Transaction;
import com.expense_tracker.model.User;
import com.expense_tracker.repository.TransactionRepository;
import com.expense_tracker.service.UserService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    //CSV
    public byte[] exportCSV(int month, int year) throws Exception {
        User user = userService.getCurrentUser();

        List<Transaction> transactions = transactionRepository.findByUserIdAndMonthAndYear(user.getId(), month, year);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT.withHeader("Date",
                "Amount", "Type", "Category", "Notes"));

        for (Transaction t : transactions) {
            csvPrinter.printRecord(
                    t.getDate(),
                    t.getAmount(),
                    t.getType(),
                    t.getCategory() != null ? t.getCategory().getName() : "General",
                    t.getNotes() != null ? t.getNotes() : ""
            );
        }

        csvPrinter.flush();
        return out.toByteArray();
    }

    // Excel export

    public byte[] exportExcel(int month, int year) throws Exception {
        System.out.println("Entered in the exportExcel service");
        User user = userService.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserIdAndMonthAndYear(user.getId(), month, year);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row header = sheet.createRow(0);
        String[] columns = {"Date", "Amount", "Type", "Category", "Notes"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (Transaction t : transactions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(t.getDate().toString());
            row.createCell(1).setCellValue(t.getAmount());
            row.createCell(2).setCellValue(t.getType().name());
            row.createCell(3).setCellValue(t.getCategory() != null ? t.getCategory().getName() : "General");
            row.createCell(4).setCellValue(t.getNotes() != null ? t.getNotes() : "");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    // PDF Export -> iText 7
//    public byte[] exportPDF(int month, int year) throws Exception {
//        System.out.println("Entering the exportPDF service");
//
//        User user = userService.getCurrentUser();
//
//        List<Transaction> transactions = transactionRepository.findByUserIdAndMonthAndYear(user.getId(), month, year);
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(out);
//        PdfDocument pdfDoc = new PdfDocument(writer);
//        Document document = new Document(pdfDoc);
//
//        document.add(new Paragraph("Monthly Transactions Report"));
//        document.add(new Paragraph("Month: " + month + " Year: " + year));
//        document.add(new Paragraph(" "));
//
//        Table table = new Table(
//                new float[]{3, 2, 2, 3, 4}
//        ).useAllAvailableWidth();
//
//        table.addHeaderCell("Date");
//        table.addHeaderCell("Amount");
//        table.addHeaderCell("Type");
//        table.addHeaderCell("Category");
//        table.addHeaderCell("Notes");
//
//        for (Transaction t : transactions) {
//            table.addCell(t.getDate().toString());
//            table.addCell(String.valueOf(t.getAmount()));
//            table.addCell(t.getType().name());
//            table.addCell(t.getCategory() != null ? t.getCategory().getName() : "General");
//            table.addCell(t.getNotes() != null ? t.getNotes() : "");
//        }
//
//        document.add(table);
//        document.close();
//        return out.toByteArray();
//
//    }
//

    // using open pdf
    public byte[] exportPDF(int month, int year) throws Exception {
        System.out.println("Entering the exportPDF service");

        User user = userService.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserIdAndMonthAndYear(
                user.getId(), month, year
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();

        document.add(new Paragraph("Monthly Transactions Report"));
        document.add(new Paragraph("Month: " + month + " Year: " + year));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        table.addCell("Date");
        table.addCell("Amount");
        table.addCell("Type");
        table.addCell("Category");
        table.addCell("Notes");

        for (Transaction t : transactions) {
            table.addCell(t.getDate() != null ? t.getDate().toString() : "");
            table.addCell(String.valueOf(t.getAmount()));
            table.addCell(t.getType() != null ? t.getType().name() : "");
            table.addCell(t.getCategory() != null ? t.getCategory().getName() : "General");
            table.addCell(t.getNotes() != null ? t.getNotes() : "");
        }

        document.add(table);
        document.close();   // closes document
        writer.close();     // VERY IMPORTANT for OpenPDF

        System.out.println("PDF size = " + out.size());

        return out.toByteArray();
    }

}