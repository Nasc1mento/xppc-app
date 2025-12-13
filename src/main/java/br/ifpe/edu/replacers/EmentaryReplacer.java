package br.ifpe.edu.replacers;

import br.ifpe.edu.CCList;
import br.ifpe.edu.replacers.helpers.CurrentTable;
import br.ifpe.edu.replacers.helpers.DocumentPath;
import br.ifpe.edu.replacers.helpers.ParagraphFinder;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class EmentaryReplacer implements IReplacer {
    private final CCList list = CCList.INSTANCE;
    private final CurrentTable currentTable = CurrentTable.INSTANCE;


    @Override
    public void replace() throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentPath.getTempPath().toFile()))) {

            XWPFParagraph paragraph = ParagraphFinder.get(doc, "@@ementário@@");

            if (paragraph != null) {
                try (var ementaryDoc = new XWPFDocument(DocumentPath.loadResourceStream("tabela_ementario.docx"))) {

                    List<XWPFTable> tables = ementaryDoc.getTables();

                    CTTbl xmlTblToCopy = tables.getFirst().getCTTbl();

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

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentPath.getTempPath().toFile()))) {
            var table = doc.getTableArray(currentTable.getCounter().addAndGet(3));

            for (var cc : list.getList()) {
                List<XWPFTableRow> rows = table.getRows();
                var currentRow = rows.getFirst();
                currentRow.getCell(0).getParagraphArray(0).createRun().setText(cc.name());
                currentRow.getCell(1).getParagraphArray(0).createRun().setText(cc.credits());

                currentRow = rows.get(1);
                currentRow.getCell(0).getParagraphArray(0).createRun().setText(cc.ha());
                table = doc.getTableArray(currentTable.nextTable());
            }

            commit(doc);
        }
    }
}
