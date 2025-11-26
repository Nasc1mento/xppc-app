package br.ifpe.edu.replacers;

import br.ifpe.edu.CurricularComponentList;
import br.ifpe.edu.ui.pages.CurricularComponents;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MatrixReplacer implements IReplacer {

    private final Path docPath;
    private final CurrentTable currentTable = CurrentTable.INSTANCE;

    public MatrixReplacer(final Path docPath) {
        this.docPath = docPath;
    }

    @Override
    public void replace() {
        Path temp = Path.of("ppc_temp.docx");

        Map<String, List<CurricularComponentList.CC>> ccPerPeriod = CurricularComponentList.INSTANCE.getList()
                .stream()
                .collect(Collectors.groupingBy(
                        CurricularComponentList.CC::period,
                        TreeMap::new,
                        Collectors.toList()
                ));

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {

            XWPFParagraph paragraph = ParagraphFinder.get(doc, "@@matriz_curricular@@");

            if (paragraph != null) {
                URL matrixPath = Thread.currentThread().getContextClassLoader().getResource("tabela_matriz_curricular.docx");
                if (matrixPath != null) {
                    try (var matrixDoc = new XWPFDocument(new FileInputStream(Paths.get(matrixPath.getPath()).toFile()))) {

                        List<XWPFTable> tables = matrixDoc.getTables();

                        CTTbl xmlTblToCopy = tables.getFirst().getCTTbl();

                        try (XmlCursor insertCursor = paragraph.getCTP().newCursor()) {
                            for (var _ : ccPerPeriod.entrySet()) {
                                XWPFTable newTable = doc.insertNewTbl(insertCursor);
                                newTable.getCTTbl().set(xmlTblToCopy.copy());
                                XWPFParagraph tempP = doc.insertNewParagraph(newTable.getCTTbl().newCursor());
                                insertCursor.toCursor(tempP.getCTP().newCursor());
                            }
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

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(temp.toFile()))) {
            var table = doc.getTableArray(currentTable.getCounter());

            for (var entry : ccPerPeriod.entrySet()) {
                List<CurricularComponentList.CC> ccs = entry.getValue();
                XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().getFirst();
                p1.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun pRun1 = p1.createRun();
                pRun1.setBold(true);
                pRun1.setItalic(false);
                pRun1.setText(entry.getValue().getFirst().period() + "°Período");
                XWPFTableRow currentRow =  table.getRows().getLast();
                for (CurricularComponentList.CC cc : ccs) {
                    currentRow.getCell(0).setText(cc.code());
                    currentRow.getCell(1).setText(cc.name());
                    currentRow.getCell(2).setText(cc.credits());
                    currentRow.getCell(3).setText(cc.ha());
                    currentRow.getCell(4).setText(cc.hr());
                    currentRow.getCell(5).setText(cc.ext());
                    currentRow.getCell(6).setText(cc.prereq());
                    currentRow.getCell(7).setText(cc.coreq());

                    if (cc != ccs.getLast()) {
                        table.addRow(currentRow);
                        currentRow = table.getRows().getLast();
                        for (var cell : currentRow.getTableCells()) {
                            cell.setText("");
                        }
                    }
                }
                table = doc.getTableArray(currentTable.newTable());
            }

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