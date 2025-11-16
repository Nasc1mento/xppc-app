package br.ifpe.edu;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) {
        try {
            // Abre o documento
            XWPFDocument doc = new XWPFDocument(new FileInputStream("input.docx"));

            // Seleciona o parágrafo que será substituído
            XWPFParagraph paragraph = doc.getParagraphs().get(0);

            // --- INSERE TABELA 1 ---
            XmlCursor cursor1 = paragraph.getCTP().newCursor();
            XWPFTable tabela1 = doc.insertNewTbl(cursor1);
            tabela1.createRow().getCell(0).setText("Primeira tabela - linha 1");
            tabela1.createRow().getCell(0).setText("Primeira tabela - linha 2");
            cursor1.dispose();

            // --- CRIA PARÁGRAFO TEMPORÁRIO PARA TABELA 2 ---
            XWPFParagraph tempParagraph = doc.insertNewParagraph(tabela1.getCTTbl().newCursor());
            XmlCursor cursor2 = tempParagraph.getCTP().newCursor();

            // --- INSERE TABELA 2 ---
            XWPFTable tabela2 = doc.insertNewTbl(cursor2);
            tabela2.createRow().getCell(0).setText("Segunda tabela - linha 1");
            tabela2.createRow().getCell(0).setText("Segunda tabela - linha 2");
            cursor2.dispose();

            // --- REMOVE O PARÁGRAFO ORIGINAL ---
            int pos = doc.getPosOfParagraph(paragraph);
            doc.removeBodyElement(pos);

            // --- SALVA O DOCUMENTO ---
            FileOutputStream out = new FileOutputStream("output.docx");
            doc.write(out);
            out.close();
            doc.close();

            System.out.println("Documento gerado com sucesso: output.docx");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
