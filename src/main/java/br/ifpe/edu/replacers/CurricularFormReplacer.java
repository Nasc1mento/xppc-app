package br.ifpe.edu.replacers;

import br.ifpe.edu.CCList;
import br.ifpe.edu.replacers.helpers.CurrentTable;
import br.ifpe.edu.replacers.helpers.DocumentPath;
import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CurricularFormReplacer implements IReplacer {

    private final CurrentTable currentTable = CurrentTable.INSTANCE;
    private final List<CC> ccList = CCList.INSTANCE.getList();
    private final Path docPath = DocumentPath.INSTANCE.getOutputPath();



    @Override
    public void replace() throws IOException {
        try (
                var doc = new XWPFDocument(new FileInputStream(docPath.toFile()));
                var ccFormDoc = new XWPFDocument(DocumentPath.loadResourceStream("formulario_componente_curricular.docx"))
        ) {

            for (var _ : ccList) {
                for (IBodyElement element : ccFormDoc.getBodyElements()) {
                    if (element instanceof XWPFParagraph sourceP) {
                        XWPFParagraph newP = doc.createParagraph();
                        newP.getCTP().set(sourceP.getCTP());
                    } else if (element instanceof XWPFTable sourceT) {
                        XWPFTable newT = doc.createTable();
                        if (!newT.getRows().isEmpty()) newT.removeRow(0); {
                            newT.getCTTbl().set(sourceT.getCTTbl());
                        }
                    }
                }
            }

            save(doc);
        }

        save();

        try (var doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {
            var table = doc.getTableArray(currentTable.getCounter().addAndGet(24));

            for (var cc : ccList) {

                table.getRows().getFirst().getCell(0).setText("X");


                table = doc.getTableArray(currentTable.nextTable());
                if (CCType.MANDATORY.equals(cc.type())) {
                    table.getRows().getFirst().getCell(0).setText("X");
                } else if (CCType.ELECTIVE.equals(cc.type())) {
                    table.getRows().getFirst().getCell(2).setText("X");
                } else if (CCType.OPTIONAL.equals(cc.type())) {
                    table.getRows().getFirst().getCell(4).setText("X");
                }

                table = doc.getTableArray(currentTable.nextTable());

                table.getRows().get(2).getCell(0).setText(cc.name());


                table.getRows().get(1).getCell(0).setText(cc.name());
                table.getRows().get(1).getCell(3).setText(cc.credits());
                table.getRows().get(1).getCell(4).setText(cc.ha());
                table.getRows().get(1).getCell(5).setText(cc.hr());
                table.getRows().get(1).getCell(6).setText(cc.period());


                table = doc.getTableArray(currentTable.nextTable());

                var row0 = table.getRows().get(0);
                row0.getCell(0).getParagraphs().getFirst().createRun().setText(cc.prereq() == null ? "Não há" : cc.prereq());
                row0.getCell(1).getParagraphs().getFirst().createRun().setText(cc.coreq() == null ? "Não há" : cc.coreq());

                table =  doc.getTableArray(currentTable.getCounter().addAndGet(6));
            }

            save(doc);

        }

        save();
    }
}
