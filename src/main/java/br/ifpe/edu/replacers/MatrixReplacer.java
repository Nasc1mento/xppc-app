package br.ifpe.edu.replacers;

import br.ifpe.edu.CurricularComponentList;
import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.replacers.helpers.CurrentTable;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import br.ifpe.edu.replacers.helpers.ParagraphFinder;
import br.ifpe.edu.ui.models.CC;
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
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MatrixReplacer implements IReplacer {

    private final CurricularComponentList list = CurricularComponentList.INSTANCE;
    private final Path docPath = DocumentHelper.INSTANCE.getOutputPath();
    private final CurrentTable currentTable = CurrentTable.INSTANCE;
    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;

    @Override
    public void replace() {
        Path temp = Path.of("ppc_temp.docx");

        NavigableMap<String, List<CC>> ccPerPeriod = list.getList()
                .stream()
                .filter(cc -> CCType.MANDATORY.equals(cc.type()))
                .collect(Collectors.groupingBy(
                        CC::period,
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
            var table = doc.getTableArray(currentTable.nextTable());
            for (var entry : ccPerPeriod.entrySet()) {
                List<CC> ccs = entry.getValue();
                var mandatoryCcs = ccs.stream().filter(cc -> CCType.MANDATORY.equals(cc.type())).toList();
                XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().getFirst();
                p1.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun pRun1 = p1.createRun();
                pRun1.setBold(true);
                pRun1.setItalic(false);
                pRun1.setText(entry.getValue().getFirst().period() + "°Período");
                XWPFTableRow currentRow =  table.getRows().get(table.getRows().size() - 2);
                IO.println(currentRow.getTableCells().size());
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
                lastRow.getCell(1).setText(sum.totalHa);
                lastRow.getCell(2).setText(sum.totalHr);
                lastRow.getCell(3).setText(sum.totalExt);
                table = doc.getTableArray(currentTable.nextTable());
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