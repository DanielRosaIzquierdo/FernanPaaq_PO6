package utils;

import com.aspose.cells.Cells;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import config.Config;
import dataClass.InfoShipmentDataClass;
import models.Shipment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GeneraDocumentos {
    public static void generaPDF(InfoShipmentDataClass is, Shipment s) {
        File file = new File(Config.rutaPATH_RECEIPT());
        PdfWriter pdfWriter = null;
        String men = "Hola " + is.getSender() + " gracias por confiar en FERNANPAAQ \n" +
                "\n" +
                "----------------------------------------------------------------------------------------------------------------------------------\n" +
                "Envio con id : " + s.getId() + "\n Esta en camino \n" +
                "\n" +
                "\n" +
                "INFORMACION DEL ENVIO:\n" +
                "Fecha de creacion: " + is.getCreateDate() + "\n" +
                "Destinatario: " + is.getReceiver() + "\n" +
                "Ciudad: " + is.getCity() + "," + is.getPostalCode() + "\n" +
                "Direccion: " + is.getAddress() + "\n" +
                "Pais: ES \n" +
                "----------------------------------------------------------------------------------------------------------------------------------\n" +
                "Dia de entrega esperado: " + Utils.fechaAString(s.getExpectDate()) + "\n" +
                "Precio total : " + s.getCost();
        try {
            pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            Paragraph paragraph = new Paragraph(men);
            document.add(paragraph);
            document.close();
            pdfWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    public static void generaExcel(ArrayList<Shipment> aux) {
        Workbook ExcelWorkbook = new Workbook();
        Worksheet ExcelWorksheet = ExcelWorkbook.getWorksheets().get(0);
        Cells WorksheetCells = ExcelWorksheet.getCells();

        WorksheetCells.get("A1").putValue("Id Paquete");
        WorksheetCells.get("B1").putValue("Fecha Creacion");
        WorksheetCells.get("C1").putValue("Fecha Esperada");
        WorksheetCells.get("D1").putValue("Fecha Entrega");
        WorksheetCells.get("E1").putValue("Direccion");
        WorksheetCells.get("F1").putValue("Codigo Postal");
        WorksheetCells.get("G1").putValue("Estado");
        WorksheetCells.get("H1").putValue("Coste");
        WorksheetCells.get("I1").putValue("Email si el usuario no esta registrado");
        WorksheetCells.get("J1").putValue("Id del remitente");
        WorksheetCells.get("K1").putValue("Id del destinatario");
        WorksheetCells.get("L1").putValue("Nombre del usuario sin registro");
        WorksheetCells.get("M1").putValue("Ciudad");

        int cont = 2;
        for (Shipment s :
                aux) {
            WorksheetCells.get("A" + cont).putValue(s.getId());
            WorksheetCells.get("B" + cont).putValue(s.getCreateDate());
            WorksheetCells.get("C" + cont).putValue(s.getExpectDate());
            WorksheetCells.get("D" + cont).putValue((s.getDeliveryDate() == null) ? "Paquete sin entregar" : s.getDeliveryDate());
            WorksheetCells.get("E" + cont).putValue(s.getAddress());
            WorksheetCells.get("F" + cont).putValue(s.getPostalCode());
            WorksheetCells.get("G" + cont).putValue(s.getStatus());
            WorksheetCells.get("H" + cont).putValue(s.getCost());
            WorksheetCells.get("I" + cont).putValue((s.getEmailUserNoRegister().equals("")) ? "Sin expecificar" : s.getEmailUserNoRegister());
            WorksheetCells.get("J" + cont).putValue(s.getIdSender());
            WorksheetCells.get("K" + cont).putValue(s.getIdReceiver());
            WorksheetCells.get("L" + cont).putValue((s.getNameUserNoRegister().equals("")) ? "Sin expecificar" : s.getNameUserNoRegister());
            WorksheetCells.get("M" + cont).putValue(s.getCity());
            cont++;
        }

        try {
            ExcelWorkbook.save(Config.rutaPATH_EXCEL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
