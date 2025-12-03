package br.ifpe.edu.replacers;

import br.ifpe.edu.CurricularComponentList;
import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.ui.models.CCType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class OptionalComponentsReplacer implements  IReplacer{

    private final CurricularComponentList list = CurricularComponentList.INSTANCE;
    private final Path docPath;
    private final CurrentTable currentTable = CurrentTable.INSTANCE;
    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    public OptionalComponentsReplacer(final Path docPath) {
        this.docPath = docPath;
    }
    @Override
    public void replace() throws IOException {
        Path temp = Path.of("ppc_temp.docx");

       var optionalComponents = list.getList().stream().filter(c -> CCType.OPTIONAL.equals(c.type())).toList();
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {

            XWPFParagraph paragraph = ParagraphFinder.get(doc, "@@componentes_optativos@@");

            if (paragraph != null) {
                URL dcPath = Thread.currentThread().getContextClassLoader().getResource("tabela_componentes_optativos_e_eletivos.docx");
                if (dcPath != null) {
                    try (var dcDoc = new XWPFDocument(new FileInputStream(Paths.get(dcPath.getPath()).toFile()))) {

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
            }

            try (var out = new FileOutputStream(temp.toFile())) {
                doc.write(out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var doc = new XWPFDocument(new FileInputStream(temp.toFile()))) {
            XWPFTable table = doc.getTableArray(currentTable.getValue());

                for (CurricularComponentList.CC cc : optionalComponents) {
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


            try (var out = new FileOutputStream(temp.toFile())) {
                doc.write(out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        try {
            Files.move(temp, docPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
