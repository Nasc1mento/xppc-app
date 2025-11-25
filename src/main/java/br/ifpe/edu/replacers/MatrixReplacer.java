package br.ifpe.edu.replacers;

import br.ifpe.edu.CurricularComponentList;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

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

    private Path docPath;

    public MatrixReplacer(final Path docPath) {
        this.docPath = docPath;
    }

    @Override
    public void replace() {
        Path temp = Path.of("ppc_temp.docx");

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {

            XWPFParagraph paragraph = ParagraphFinder.get(doc, "@@matriz_curricular@@");

            Map<String, List<CurricularComponentList.CC>> ccPerPeriod = CurricularComponentList.INSTANCE.getList()
                    .stream()
                    .collect(Collectors.groupingBy(
                            CurricularComponentList.CC::period,
                            () -> new TreeMap<>(Comparator.reverseOrder()),
                            Collectors.toList()
                    ));

            if (paragraph != null) {
                URL matrixPath = Thread.currentThread().getContextClassLoader().getResource("tabela_matriz_curricular.docx");
                if (matrixPath != null) {
                    try (var matrixDoc = new XWPFDocument(new FileInputStream(Paths.get(matrixPath.getPath()).toFile()))) {

                        List<XWPFTable> tables = matrixDoc.getTables();

                        CTTbl xmlTblToCopy = tables.getFirst().getCTTbl();
                        int N = 3;

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


        try {
            Files.move(temp, docPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createHeaderCell(XWPFTableRow row) {
        XWPFTableCell cell = row.addNewTableCell();
        XWPFParagraph p = cell.getParagraphArray(0);
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = p.createRun();
        run.setBold(true);
    }

    private void hMerge(XWPFTableCell cell, boolean firstCell) {
        if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
        final CTHMerge hMerge = CTHMerge.Factory.newInstance();
        if  (firstCell) {
            hMerge.setVal(STMerge.RESTART);
        } else {
            hMerge.setVal(STMerge.CONTINUE);
        }
        cell.getCTTc().getTcPr().setHMerge(hMerge);

    }

    private void vMerge(XWPFTableCell cell, boolean firstCell) {
        if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
        final CTVMerge vMerge = CTVMerge.Factory.newInstance();
        if  (firstCell) {
            vMerge.setVal(STMerge.RESTART);
        } else {
            vMerge.setVal(STMerge.CONTINUE);
        }

        cell.getCTTc().getTcPr().setVMerge(vMerge);
    }
}