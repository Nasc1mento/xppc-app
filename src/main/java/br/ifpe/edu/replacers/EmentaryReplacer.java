package br.ifpe.edu.replacers;

import br.ifpe.edu.CCList;
import br.ifpe.edu.replacers.helpers.TableLocationHelper;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import br.ifpe.edu.replacers.helpers.ParagraphHelper;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class EmentaryReplacer implements IReplacer {
    private final CCList list = CCList.INSTANCE;
    private final TableLocationHelper tableLocationHelper = TableLocationHelper.INSTANCE;

    @Override
    public void replace() throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentHelper.getTempPath().toFile()))) {

            XWPFParagraph paragraph = ParagraphHelper.find(doc, "@@ementário@@");

            if (paragraph != null) {
                try (var ementaryDoc = new XWPFDocument(DocumentHelper.loadResourceStream("tabela_ementario.docx"))) {

                    CTTbl xmlTblToCopy = ementaryDoc.getTables().getFirst().getCTTbl();

                    try (XmlCursor insertCursor = paragraph.getCTP().newCursor()) {
                        for (var _ : list.getList()) {
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

            commit(doc);
        }

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentHelper.getTempPath().toFile()))) {
            var table = doc.getTableArray(tableLocationHelper.getCounter().addAndGet(3));

            for (var cc : list.getList()) {
                List<XWPFTableRow> rows = table.getRows();
                var currentRow = rows.getFirst();
                currentRow.getCell(0).getParagraphArray(0).createRun().setText("Componente Curricular: " + cc.name());
                currentRow.getCell(1).getParagraphArray(0).createRun().setText("Créditos: " + cc.credits());

                currentRow = rows.get(1);
                currentRow.getCell(0).getParagraphArray(0).createRun().setText("Carga horária: " + cc.ha());
                ParagraphHelper.setTextNBreak(
                        currentRow.getCell(1).getParagraphArray(0).createRun(),
                        String.format("AT(%s)\nAP(%s)\nEXT(%s)", cc.at(), cc.ap(), cc.ae())
                );

                table = doc.getTableArray(tableLocationHelper.nextTable());
            }

            commit(doc);
        }
    }
}
