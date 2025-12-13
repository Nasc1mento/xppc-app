package br.ifpe.edu.replacers;

import br.ifpe.edu.CCList;
import br.ifpe.edu.Eval;
import br.ifpe.edu.replacers.helpers.CurrentTable;
import br.ifpe.edu.replacers.helpers.DocumentPath;
import br.ifpe.edu.replacers.helpers.ParagraphFinder;
import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CurricularFormReplacer implements IReplacer {

    private final CurrentTable currentTable = CurrentTable.INSTANCE;
    private final List<CC> ccList = CCList.INSTANCE.getList();

    @Override
    public void replace() throws IOException {
        try (
                var doc = new XWPFDocument(new FileInputStream(DocumentPath.getTempPath().toFile()));
        ) {

            XWPFParagraph paragraph = ParagraphFinder.get(doc, "@@formulario_componentes_curriculares@@");

            if (paragraph != null) {
                try (var ccFormDoc = new XWPFDocument(DocumentPath.loadResourceStream("formulario_componente_curricular.docx"))) {
                    for (var _ : ccList) {
                        try (XmlCursor insertCursor = paragraph.getCTP().newCursor()) {
                            for (IBodyElement element : ccFormDoc.getBodyElements().reversed()) {
                                if (element instanceof XWPFParagraph sourceP) {
                                    XWPFParagraph newP = doc.insertNewParagraph(insertCursor);
                                    newP.getCTP().set(sourceP.getCTP());
                                    insertCursor.toCursor(newP.getCTP().newCursor());
                                } else if (element instanceof XWPFTable sourceT) {
                                    XWPFTable newT = doc.insertNewTbl(insertCursor);
                                    if (!newT.getRows().isEmpty()) newT.removeRow(0); {
                                        newT.getCTTbl().set(sourceT.getCTTbl());
                                    }
                                    insertCursor.toCursor(newT.getCTTbl().newCursor());
                                }
                            }
                        }
                    }

                    int pos = doc.getPosOfParagraph(paragraph);
                    doc.removeBodyElement(pos);
                }
            }

            save(doc);
        }

        try (var doc = new XWPFDocument(new FileInputStream(DocumentPath.getTempPath().toFile()))) {
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

                {
                    XWPFTableRow row = table.getRows().getLast();
                    row.getCell(0).setText(cc.name());
                    row.getCell(1).setText(cc.hrTeo());
                    row.getCell(2).setText(cc.hrPr());
                    row.getCell(3).setText(cc.ext());
                    row = table.getRows().get(1);
                    row.getCell(4).setText(cc.credits());
                    row.getCell(5).setText(cc.ha());
                    row.getCell(6).setText(Eval.evalDecimal("%s+%s", cc.hr(), cc.ext()));
                    row.getCell(7).setText(cc.period() + "°");
                }

                table = doc.getTableArray(currentTable.nextTable());

                {
                    XWPFTableRow row = table.getRows().getFirst();
                    row.getCell(0).getParagraphs().getFirst().createRun().setText(cc.prereq() == null ? "Não há" : cc.prereq());
                    row.getCell(1).getParagraphs().getFirst().createRun().setText(cc.coreq() == null ? "Não há" : cc.coreq());
                }

                table = doc.getTableArray(currentTable.getCounter().addAndGet(6));
            }
            save(doc);
        }
    }
}
