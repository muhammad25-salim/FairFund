package project.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import project.models.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class PdfExporter {

    public static void exportGroupReport(Group group, String filePath) {
        try {
            // Create reports directory if it doesn't exist
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            
            // If filePath doesn't start with "reports/", prepend it
            if (!filePath.startsWith("reports/") && !filePath.startsWith("reports\\")) {
                String fileName = new File(filePath).getName();
                filePath = "reports/" + fileName;
            }
            
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add header with group information
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(41, 128, 185));
            Paragraph title = new Paragraph("Group Report: " + group.getGroupName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Add current date
            Font dateFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            Paragraph date = new Paragraph("on " + dateFormat.format(new java.util.Date()), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);
            
            document.add(new Paragraph("\n"));

            // Member Balances
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(52, 73, 94));
            Paragraph memberSection = new Paragraph("Members and Balances", sectionFont);
            memberSection.setSpacingBefore(10);
            document.add(memberSection);

            PdfPTable balanceTable = new PdfPTable(2);
            balanceTable.setWidthPercentage(90);
            balanceTable.setSpacingBefore(10);
            
            // Table headers with style
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            
            // Create stylish header cells
            PdfPCell nameHeaderCell = new PdfPCell(new Phrase("Member Name", tableHeaderFont));
            nameHeaderCell.setBackgroundColor(new BaseColor(41, 128, 185));
            nameHeaderCell.setPadding(8);
            
            PdfPCell balanceHeaderCell = new PdfPCell(new Phrase("Balance", tableHeaderFont));
            balanceHeaderCell.setBackgroundColor(new BaseColor(41, 128, 185));
            balanceHeaderCell.setPadding(8);
            balanceHeaderCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            
            balanceTable.addCell(nameHeaderCell);
            balanceTable.addCell(balanceHeaderCell);

            // Table content with alternating colors
            Font tableContentFont = new Font(Font.FontFamily.HELVETICA, 12);
            BaseColor lightBlue = new BaseColor(240, 248, 255);
            BaseColor white = new BaseColor(255, 255, 255);
            int rowCount = 0;
            
            for (Member member : group.getMembers()) {
                BaseColor rowColor = (rowCount % 2 == 0) ? lightBlue : white;
                
                PdfPCell nameCell = new PdfPCell(new Phrase(member.getName(), tableContentFont));
                nameCell.setBackgroundColor(rowColor);
                nameCell.setPadding(6);
                
                PdfPCell balanceCell = new PdfPCell();
                Font balanceFont = new Font(Font.FontFamily.HELVETICA, 12);
                
                // Set color based on balance value
                if (member.getBalance() > 0) {
                    balanceFont.setColor(new BaseColor(0, 128, 0)); // Green for positive
                } else if (member.getBalance() < 0) {
                    balanceFont.setColor(new BaseColor(192, 0, 0)); // Red for negative
                }
                
                balanceCell.setPhrase(new Phrase(String.format("%.2f IQD", member.getBalance()), balanceFont));
                balanceCell.setBackgroundColor(rowColor);
                balanceCell.setPadding(6);
                balanceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                balanceTable.addCell(nameCell);
                balanceTable.addCell(balanceCell);
                
                rowCount++;
            }
            
            document.add(balanceTable);
            document.add(new Paragraph("\n"));

        }
    }
}