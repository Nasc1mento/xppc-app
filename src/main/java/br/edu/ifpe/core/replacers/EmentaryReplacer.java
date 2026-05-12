package br.edu.ifpe.core.replacers;

import br.edu.ifpe.infra.doc.helpers.TableHelper;
import br.edu.ifpe.infra.doc.helpers.TextHelper;
import br.edu.ifpe.core.CCManager;
import br.edu.ifpe.infra.doc.helpers.TableTracker;
import br.edu.ifpe.infra.doc.DocumentManager;
import br.edu.ifpe.infra.doc.DocumentCursor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class EmentaryReplacer implements IReplacer {

    private final DocumentCursor documentCursor = DocumentCursor.INSTANCE;
    private final CCManager list = CCManager.INSTANCE;
    private final TableTracker tableTracker = TableTracker.INSTANCE;


    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public void replace() throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {

            XWPFParagraph paragraph = documentCursor.find(doc, "@@ementário@@");

            if (paragraph != null) {
                TableHelper.copySimpleTbl(doc, paragraph, "docx/templates/tabela_ementario.docx", list.getList().size());
            }

            commit(doc);
        }

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {
            var table = doc.getTableArray(tableTracker.getValue());

            for (var cc : list.getList()) {
                List<XWPFTableRow> rows = table.getRows();
                var currentRow = rows.getFirst();
                currentRow.getCell(0).getParagraphArray(0).createRun().setText("Componente Curricular: " + cc.name());
                currentRow.getCell(1).getParagraphArray(0).createRun().setText("Créditos: " + cc.credits());

                currentRow = rows.get(1);
                currentRow.getCell(0).getParagraphArray(0).createRun().setText("Carga horária: " + cc.ha());
                TextHelper.setTextNBreak(
                        currentRow.getCell(1).getParagraphArray(0).createRun(),
                        String.format("AT(%s)\nAP(%s)\nEXT(%s)", cc.at(), cc.ap(), cc.ae())
                );

                table = doc.getTableArray(tableTracker.nextTable());
            }

            commit(doc);
        }
    }
}
