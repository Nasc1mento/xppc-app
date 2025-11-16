package br.ifpe.edu.replacers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlCursor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public record MatrixReplacer(Path docPath) implements IReplacer {

    @Override
    public void replace() {
        Path temp = Path.of("ppc_temp.docx");

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {
            XWPFParagraph paragraph = Utils.getParagraph(doc, "@@matriz@@");

            if (paragraph != null) {
                try (XmlCursor cursor = paragraph.getCTP().newCursor()) {
                    XWPFTable table = doc.insertNewTbl(cursor);
                    table.createRow().getCell(0).setText("Primeira tabela - linha 1");
                    table.createRow().getCell(0).setText("Primeira tabela - linha 2");
                }

                int pos = doc.getPosOfParagraph(paragraph);
                doc.removeBodyElement(pos);
            }
            try (var out = new FileOutputStream(temp.toFile())) {
                doc.write(out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            Files.move(temp, docPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
