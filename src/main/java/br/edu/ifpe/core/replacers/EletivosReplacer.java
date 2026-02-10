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

public class EletivosReplacer implements  IReplacer{

    private final DocumentCursor documentCursor = DocumentCursor.INSTANCE;
    private final CCManager list = CCManager.INSTANCE;
    private final TableTracker tableTracker = TableTracker.INSTANCE;

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public void replace() throws IOException {

        var electiveComponents = list.getList().stream().filter(c -> CCType.ELECTIVE.equals(c.type())).toList();
        try (var doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {

            XWPFParagraph paragraph = documentCursor.find(doc, "@@componentes_eletivos@@");

            if (paragraph != null) {
                TableHelper.copySimpleTbl(doc, paragraph, "docx/templates/tabela_componentes_optativos_e_eletivos.docx");
            }

            commit(doc);
        }

        try (var doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {
            XWPFTable table = doc.getTableArray(tableTracker.getValue());

            for (CC cc : electiveComponents) {
                XWPFTableRow currentRow = table.getRows().getLast();
                currentRow.getCell(0).setText(cc.code());
                currentRow.getCell(1).setText(cc.name());
                currentRow.getCell(2).setText(cc.credits());
                currentRow.getCell(3).setText(cc.ha());
                currentRow.getCell(4).setText(cc.hr());
                currentRow.getCell(5).setText(cc.prereq());
                currentRow.getCell(6).setText(cc.coreq());

                if (cc != electiveComponents.getLast()) {
                    table.addRow(currentRow);
                    for (XWPFTableCell cell : currentRow.getTableCells()) {
                        cell.setText("");
                    }
                }
            }

            table.removeRow(1);
            tableTracker.nextTable();

            commit(doc);
        }
    }
}
