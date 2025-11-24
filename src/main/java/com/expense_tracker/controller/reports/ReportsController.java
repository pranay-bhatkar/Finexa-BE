package com.expense_tracker.controller.reports;

import com.expense_tracker.service.reports.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCSV(
            @RequestParam int month,
            @RequestParam int year
    ) throws Exception {
        byte[] data = reportsService.exportCSV(month, year);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }


    @GetMapping("/monthly/excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam int month,
            @RequestParam int year
    ) throws Exception {
        System.out.println("Entered in the exportExcel controller");
        byte[] data = reportsService.exportExcel(month, year);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/monthly/pdf")
    public ResponseEntity<byte[]> exportPDF(
            @RequestParam int month,
            @RequestParam int year
    ) throws Exception {
        System.out.println("Entering in the exportPDF controller");
        byte[] data = reportsService.exportPDF(month, year);

        System.out.println("Exit from the export pdf service");

        return ResponseEntity.ok()
                .contentLength(data.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

}