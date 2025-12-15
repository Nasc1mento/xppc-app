package br.ifpe.edu.services.replacers;

import br.ifpe.edu.services.CCList;
import br.ifpe.edu.helpers.TableLocationHelper;
import br.ifpe.edu.helpers.DocumentHelper;
import br.ifpe.edu.helpers.ParagraphHelper;
import br.ifpe.edu.models.CC;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CurricularDrawReplacer implements IReplacer {

    private final TableLocationHelper tableLocationHelper = TableLocationHelper.INSTANCE;

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void replace() throws IOException {

        Map<String, List<CC>> ccPerPeriod = CCList.INSTANCE.getList()
                .stream()
                .collect(Collectors.groupingBy(
                        CC::period,
                        TreeMap::new,
                        Collectors.toList()
                ));
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentHelper.INSTANCE.getTempPath().toFile()))) {

            XWPFParagraph paragraph = ParagraphHelper.find(doc, "@@desenho_curricular@@");

            if (paragraph != null) {
                try (var dcDoc = new XWPFDocument(DocumentHelper.loadResourceStream("tabela_desenho_curricular.docx"))) {

                    CTTbl xmlTblToCopy = dcDoc.getTables().getFirst().getCTTbl();
                    try (XmlCursor insertCursor = paragraph.getCTP().newCursor()) {
                        XWPFTable newTable = doc.insertNewTbl(insertCursor);
                        newTable.getCTTbl().set(xmlTblToCopy.copy());
                        XWPFParagraph tempP = doc.insertNewParagraph(newTable.getCTTbl().newCursor());
                        insertCursor.toCursor(tempP.getCTP().newCursor());
                    }

                    int pos = doc.getPosOfParagraph(paragraph);
                    doc.removeBodyElement(pos);
                }
            }

            commit(doc);
        }

        try (var doc = new XWPFDocument(new FileInputStream(DocumentHelper.INSTANCE.getTempPath().toFile()))) {
            XWPFTable table = doc.getTableArray(tableLocationHelper.getValue());

            for (var entry : ccPerPeriod.entrySet()) {
                List<CC> ccs = entry.getValue();
                for (CC cc : ccs) {
                    XWPFTableRow currentRow = table.getRows().getLast();
                    currentRow.getCell(0).setText(cc.name());
                    currentRow.getCell(1).setText(cc.ha());
                    currentRow.getCell(2).setText(cc.period());
                    currentRow.getCell(3).setText(cc.prereq());
                    currentRow.getCell(4).setText(cc.coreq());
                    table.addRow(currentRow);
                    for (XWPFTableCell cell : currentRow.getTableCells()) {
                        cell.setText("");
                    }
                }
            }

            table.removeRow(1);
            commit(doc);
        }
    }
}