package br.edu.ifpe.services.replacers;

import br.edu.ifpe.services.DocumentManager;
import br.edu.ifpe.services.DocumentCursor;
import br.edu.ifpe.helpers.TableHelper;
import br.edu.ifpe.helpers.TableTracker;
import br.edu.ifpe.models.CC;
import br.edu.ifpe.services.CCManager;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CurricularDrawReplacer implements IReplacer {

    private final DocumentCursor documentCursor = DocumentCursor.INSTANCE;
    private final TableTracker tableTracker = TableTracker.INSTANCE;

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void replace() throws IOException {

        Map<String, List<CC>> ccPerPeriod = CCManager.INSTANCE.getList()
                .stream()
                .collect(Collectors.groupingBy(
                        CC::period,
                        TreeMap::new,
                        Collectors.toList()
                ));
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {

            XWPFParagraph paragraph = documentCursor.find(doc, "@@desenho_curricular@@");

            if (paragraph != null) {
                TableHelper.copySimpleTbl(doc, paragraph, "tabela_desenho_curricular.docx");
            }

            commit(doc);
        }

        try (var doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {
            XWPFTable table = doc.getTableArray(tableTracker.getValue());

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