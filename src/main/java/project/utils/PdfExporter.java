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

            // Expenses Section
            Paragraph expenseSection = new Paragraph("Expenses", sectionFont);
            expenseSection.setSpacingBefore(10);
            document.add(expenseSection);
            
            if (group.getExpenses().isEmpty()) {
                document.add(new Paragraph("No expenses recorded for this group.", 
                    new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)));
            } else {
                PdfPTable expenseTable = new PdfPTable(4);
                expenseTable.setWidthPercentage(100);
                expenseTable.setWidths(new float[]{3, 2, 2, 3});
                expenseTable.setSpacingBefore(10);
                
                // Add table headers
                PdfPCell titleHeader = new PdfPCell(new Phrase("Description", tableHeaderFont));
                titleHeader.setBackgroundColor(new BaseColor(41, 128, 185));
                titleHeader.setPadding(8);
                
                PdfPCell amountHeader = new PdfPCell(new Phrase("Amount", tableHeaderFont));
                amountHeader.setBackgroundColor(new BaseColor(41, 128, 185));
                amountHeader.setPadding(8);
                amountHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                PdfPCell payerHeader = new PdfPCell(new Phrase("Payer", tableHeaderFont));
                payerHeader.setBackgroundColor(new BaseColor(41, 128, 185));
                payerHeader.setPadding(8);
                
                PdfPCell participantsHeader = new PdfPCell(new Phrase("Participants", tableHeaderFont));
                participantsHeader.setBackgroundColor(new BaseColor(41, 128, 185));
                participantsHeader.setPadding(8);
                
                expenseTable.addCell(titleHeader);
                expenseTable.addCell(amountHeader);
                expenseTable.addCell(payerHeader);
                expenseTable.addCell(participantsHeader);

                // Add expense rows
                rowCount = 0;
                for (Expense exp : group.getExpenses()) {
                    BaseColor rowColor = (rowCount % 2 == 0) ? lightBlue : white;
                    
                    PdfPCell titleCell = new PdfPCell(new Phrase(exp.getTitle(), tableContentFont));
                    titleCell.setBackgroundColor(rowColor);
                    titleCell.setPadding(6);
                    
                    PdfPCell amountCell = new PdfPCell(new Phrase(String.format("%.2f IQD", exp.getTotalAmount()), tableContentFont));
                    amountCell.setBackgroundColor(rowColor);
                    amountCell.setPadding(6);
                    amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    
                    PdfPCell payerCell = new PdfPCell(new Phrase(exp.getPayer().getName(), tableContentFont));
                    payerCell.setBackgroundColor(rowColor);
                    payerCell.setPadding(6);
                    
                    StringBuilder participantNames = new StringBuilder();
                    for (Member u : exp.getParticipants()) {
                        participantNames.append(u.getName()).append(", ");
                    }
                    if (participantNames.length() > 0)
                        participantNames.setLength(participantNames.length() - 2); // remove last comma
                    
                    PdfPCell participantsCell = new PdfPCell(new Phrase(participantNames.toString(), tableContentFont));
                    participantsCell.setBackgroundColor(rowColor);
                    participantsCell.setPadding(6);
                    
                    expenseTable.addCell(titleCell);
                    expenseTable.addCell(amountCell);
                    expenseTable.addCell(payerCell);
                    expenseTable.addCell(participantsCell);
                    
                    rowCount++;
                }
                
                document.add(expenseTable);
            }
            
            document.add(new Paragraph("\n"));

            // Add Payments Section
            Paragraph paymentSection = new Paragraph("Payments", sectionFont);
            paymentSection.setSpacingBefore(10);
            document.add(paymentSection);
            
            if (group.getPayments().isEmpty()) {
                document.add(new Paragraph("No payments recorded for this group.", 
                    new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)));
            } else {
                PdfPTable paymentTable = new PdfPTable(3);
                paymentTable.setWidthPercentage(90);
                paymentTable.setSpacingBefore(10);
                
                // Add table headers
                PdfPCell fromHeader = new PdfPCell(new Phrase("From", tableHeaderFont));
                fromHeader.setBackgroundColor(new BaseColor(41, 128, 185));
                fromHeader.setPadding(8);
                
                PdfPCell toHeader = new PdfPCell(new Phrase("To", tableHeaderFont));
                toHeader.setBackgroundColor(new BaseColor(41, 128, 185));
                toHeader.setPadding(8);
                
                PdfPCell amountHeader = new PdfPCell(new Phrase("Amount", tableHeaderFont));
                amountHeader.setBackgroundColor(new BaseColor(41, 128, 185));
                amountHeader.setPadding(8);
                amountHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                paymentTable.addCell(fromHeader);
                paymentTable.addCell(toHeader);
                paymentTable.addCell(amountHeader);

                // Add payment rows
                rowCount = 0;
                for (Payment payment : group.getPayments()) {
                    BaseColor rowColor = (rowCount % 2 == 0) ? lightBlue : white;
                    
                    PdfPCell fromCell = new PdfPCell(new Phrase(payment.getFrom(), tableContentFont));
                    fromCell.setBackgroundColor(rowColor);
                    fromCell.setPadding(6);
                    
                    PdfPCell toCell = new PdfPCell(new Phrase(payment.getTo(), tableContentFont));
                    toCell.setBackgroundColor(rowColor);
                    toCell.setPadding(6);
                    
                    PdfPCell amountCell = new PdfPCell(new Phrase(
                        String.format("%.2f IQD", payment.getAmount()), 
                        new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0, 102, 204))
                    ));
                    amountCell.setBackgroundColor(rowColor);
                    amountCell.setPadding(6);
                    amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    
                    paymentTable.addCell(fromCell);
                    paymentTable.addCell(toCell);
                    paymentTable.addCell(amountCell);
                    
                    rowCount++;
                }
                
                document.add(paymentTable);
            }

            // Add footer
            Paragraph footer = new Paragraph("by FairFund App", 
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();
            System.out.println("PDF exported to: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
