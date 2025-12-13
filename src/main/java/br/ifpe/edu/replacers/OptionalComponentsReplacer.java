package br.ifpe.edu.replacers;

import br.ifpe.edu.CCList;
import br.ifpe.edu.replacers.helpers.CurrentTable;
import br.ifpe.edu.replacers.helpers.DocumentPath;
import br.ifpe.edu.replacers.helpers.ParagraphFinder;
import br.ifpe.edu.ui.models.CC;
import br.ifpe.edu.ui.models.CCType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class OptionalComponentsReplacer implements  IReplacer{

    private final List<CC> list = CCList.INSTANCE.getList();
    private final CurrentTable currentTable = CurrentTable.INSTANCE;

    @Override
    public void replace() throws IOException {

       var optionalComponents = list.stream().filter(c -> CCType.OPTIONAL.equals(c.type())).toList();
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(DocumentPath.getTempPath().toFile()))) {

            XWPFParagraph paragraph = ParagraphFinder.get(doc, "@@componentes_optativos@@");

            if (paragraph != null) {
                try (var dcDoc = new XWPFDocument(DocumentPath.loadResourceStream("tabela_componentes_optativos_e_eletivos.docx"))) {
                    List<XWPFTable> tables = dcDoc.getTables();

                    CTTbl xmlTblToCopy = tables.getFirst().getCTTbl();
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

            save(doc);
        }

        try (var doc = new XWPFDocument(new FileInputStream(DocumentPath.getTempPath().toFile()))) {
            XWPFTable table = doc.getTableArray(currentTable.getValue());

                for (CC cc : optionalComponents) {
                    XWPFTableRow currentRow = table.getRows().getLast();
                    currentRow.getCell(0).setText(cc.code());
                    currentRow.getCell(1).setText(cc.name());
                    currentRow.getCell(2).setText(cc.credits());
                    currentRow.getCell(3).setText(cc.ha());
                    currentRow.getCell(4).setText(cc.hr());
                    currentRow.getCell(5).setText(cc.prereq());
                    currentRow.getCell(6).setText(cc.coreq());
                    if (cc != optionalComponents.getLast()) {
                        table.addRow(currentRow);
                        for (XWPFTableCell cell : currentRow.getTableCells()) {
                            cell.setText("");
                        }
                    }
                }

            table.removeRow(1);
            currentTable.nextTable();

            save(doc);
        }
    }
}
