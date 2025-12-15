package br.ifpe.edu.replacers;

import br.ifpe.edu.CCList;
import br.ifpe.edu.Eval;
import br.ifpe.edu.replacers.helpers.TableLocationHelper;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import br.ifpe.edu.replacers.helpers.ParagraphHelper;
import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class CurricularFormReplacer implements IReplacer {

    private final TableLocationHelper tableLocationHelper = TableLocationHelper.INSTANCE;
    private final List<CC> ccList = CCList.INSTANCE.getList();

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public void replace() throws IOException {
        try (
                var doc = new XWPFDocument(new FileInputStream(DocumentHelper.INSTANCE.getTempPath().toFile()))
        ) {

            XWPFParagraph paragraph = ParagraphHelper.find(doc, "@@formulario_componentes_curriculares@@");

            if (paragraph != null) {
                try (var ccFormDoc = new XWPFDocument(DocumentHelper.loadResourceStream("formulario_componente_curricular.docx"))) {
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

            commit(doc);
        }

        try (var doc = new XWPFDocument(new FileInputStream(DocumentHelper.INSTANCE.getTempPath().toFile()))) {
            var table = doc.getTableArray(tableLocationHelper.getValue());
            for (var cc : ccList) {
                table.getRows().getFirst().getCell(0).setText("X");
                table = doc.getTableArray(tableLocationHelper.nextTable());

                if (CCType.MANDATORY.equals(cc.type())) {
                    table.getRows().getFirst().getCell(0).setText("X");
                } else if (CCType.ELECTIVE.equals(cc.type())) {
                    table.getRows().getFirst().getCell(2).setText("X");
                } else if (CCType.OPTIONAL.equals(cc.type())) {
                    table.getRows().getFirst().getCell(4).setText("X");
                }

                table = doc.getTableArray(tableLocationHelper.nextTable());

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

                table = doc.getTableArray(tableLocationHelper.nextTable());

                row = table.getRows().getFirst();
                row.getCell(0).getParagraphs().getFirst().createRun().setText(cc.prereq() == null ? "Não há" : cc.prereq());
                row.getCell(1).getParagraphs().getFirst().createRun().setText(cc.coreq() == null ? "Não há" : cc.coreq());

                table = doc.getTableArray(tableLocationHelper.getCounter().addAndGet(6));
            }
            commit(doc);
        }
    }
}
