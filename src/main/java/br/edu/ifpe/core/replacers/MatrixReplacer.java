package br.edu.ifpe.core.replacers;

import br.edu.ifpe.helpers.TableHelper;
import br.edu.ifpe.core.CCManager;
import br.edu.ifpe.helpers.TableTracker;
import br.edu.ifpe.core.DocumentManager;
import br.edu.ifpe.core.DocumentCursor;
import br.edu.ifpe.models.CC;
import br.edu.ifpe.enums.CCType;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MatrixReplacer implements IReplacer {


    @Override
    public int getPriority() {
        return 20;
    }

    private final DocumentCursor documentCursor = DocumentCursor.INSTANCE;
    private final CCManager list = CCManager.INSTANCE;
    private final TableTracker tableTracker = TableTracker.INSTANCE;

    @Override
    public void replace() throws IOException {

        NavigableMap<String, List<CC>> ccPerPeriod = list.getList()
                .stream()
                .filter(cc -> CCType.MANDATORY.equals(cc.type()))
                .collect(Collectors.groupingBy(
                        CC::period,
                        TreeMap::new,
                        Collectors.toList()
                ));
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {
            XWPFParagraph paragraph = documentCursor.find(doc, "@@matriz_curricular@@");

            if (paragraph != null) {
                TableHelper.copySimpleTbl(doc, paragraph, "docx/templates/tabela_matriz_curricular.docx", ccPerPeriod.size());
            }

            commit(doc);
        }

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {
            var table = doc.getTableArray(tableTracker.getValue());

            for (var entry : ccPerPeriod.entrySet()) {
                List<CC> ccs = entry.getValue();
                var mandatoryCcs = ccs.stream().filter(cc -> CCType.MANDATORY.equals(cc.type())).toList();
                XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().getFirst();
                p1.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun pRun1 = p1.createRun();
                pRun1.setBold(true);
                pRun1.setItalic(false);
                pRun1.setText(entry.getValue().getFirst().period() + "° Período");
                XWPFTableRow currentRow =  table.getRows().get(table.getRows().size() - 2);

                for (CC cc : mandatoryCcs) {
                    currentRow.getCell(0).setText(cc.code());
                    currentRow.getCell(1).setText(cc.name());
                    currentRow.getCell(2).setText(cc.credits());
                    currentRow.getCell(3).setText(cc.ha());
                    currentRow.getCell(4).setText(cc.hr());
                    currentRow.getCell(5).setText(cc.ext());
                    currentRow.getCell(6).setText(cc.prereq());
                    currentRow.getCell(7).setText(cc.coreq());

                    if (cc != mandatoryCcs.getLast()) {
                        table.addRow(currentRow ,table.getRows().size() - 2);
                        currentRow = table.getRows().get(table.getRows().size() - 2);
                        for (var cell : currentRow.getTableCells()) {
                            cell.setText("");
                        }
                    }
                }


                XWPFTableRow lastRow = table.getRows().getLast();
                var sum = list.getSum(entry.getValue(), CCType.MANDATORY);
                lastRow.getCell(1).setText(sum.getTotalHa());
                lastRow.getCell(2).setText(sum.getTotalHr());
                lastRow.getCell(3).setText(sum.getTotalExt());
                table = doc.getTableArray(tableTracker.nextTable());
            }

            commit(doc);

        }
    }
}