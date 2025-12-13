package br.ifpe.edu.replacers;

import br.ifpe.edu.CCList;
import br.ifpe.edu.replacers.helpers.TableLocationHelper;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import br.ifpe.edu.replacers.helpers.ParagraphHelper;
import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class EletivosReplacer implements  IReplacer{

    private final CCList list = CCList.INSTANCE;
    private final Path docPath = DocumentHelper.INSTANCE.getOutputPath();
    private final TableLocationHelper tableLocationHelper = TableLocationHelper.INSTANCE;

    @Override
    public void replace() throws IOException {

        var electiveComponents = list.getList().stream().filter(c -> CCType.ELECTIVE.equals(c.type())).toList();
        try (var doc = new XWPFDocument(new FileInputStream(DocumentHelper.getTempPath().toFile()))) {

            XWPFParagraph paragraph = ParagraphHelper.find(doc, "@@componentes_optativos@@");

            if (paragraph != null) {
                try (var dcDoc = new XWPFDocument(DocumentHelper.loadResourceStream("tabela_componentes_optativos_e_eletivos.docx"))) {
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

        try (var doc = new XWPFDocument(new FileInputStream(DocumentHelper.getTempPath().toFile()))) {
            XWPFTable table = doc.getTableArray(tableLocationHelper.getValue());

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
            tableLocationHelper.nextTable();

            commit(doc);
        }
    }
}
