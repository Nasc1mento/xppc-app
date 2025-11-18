package br.ifpe.edu.replacers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public record MatrixReplacer(Path docPath) implements IReplacer {

    @Override
    public void replace() {
        Path temp = Path.of("ppc_temp.docx");

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {

            XWPFParagraph paragraph = Utils.getParagraph(doc, "@@matriz_curricular@@");

            if (paragraph != null) {
                URL matrixPath = Thread.currentThread().getContextClassLoader().getResource("tabela_matriz_curricular.docx");
                if (matrixPath != null) {
                    try (var matrixDoc = new XWPFDocument(new FileInputStream(Paths.get(matrixPath.getPath()).toFile()))) {

                        List<XWPFTable> tables = matrixDoc.getTables();

                        CTTbl xmlTblToCopy = tables.getFirst().getCTTbl();
                        int N = 3;

                        try (XmlCursor insertCursor = paragraph.getCTP().newCursor()) {
                            for (int i = 0; i < N; i++) {
                                XWPFTable newTable = doc.insertNewTbl(insertCursor);
                                newTable.getCTTbl().set(xmlTblToCopy.copy());
                                XWPFParagraph tempP = doc.insertNewParagraph(newTable.getCTTbl().newCursor());
                                insertCursor.toCursor(tempP.getCTP().newCursor());
                            }
                        }

                        int pos = doc.getPosOfParagraph(paragraph);
                        doc.removeBodyElement(pos);
                    }
                }
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