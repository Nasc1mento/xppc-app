package br.ifpe.edu.replacers;

import br.ifpe.edu.CurricularComponentList;
import br.ifpe.edu.ui.pages.CurricularComponents;
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

public class EmentaryReplacer implements IReplacer {



    private final CurricularComponentList list = CurricularComponentList.INSTANCE;
    private final Path docPath;
    private final CurrentTable currentTable = CurrentTable.INSTANCE;

    public EmentaryReplacer(final Path docPath) {
        this.docPath = docPath;
    }

    @Override
    public void replace() {
        Path temp = Path.of("ppc_temp.docx");

        NavigableMap<String, List<CurricularComponentList.CC>> ccPerPeriod = list.getList()
                .stream()
                .collect(Collectors.groupingBy(
                        CurricularComponentList.CC::period,
                        TreeMap::new,
                        Collectors.toList()
                ));
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {

            XWPFParagraph paragraph = ParagraphFinder.get(doc, "@@ementário@@");

            if (paragraph != null) {
                URL ementaryPath = Thread.currentThread().getContextClassLoader().getResource("tabela_ementario.docx");
                if (ementaryPath != null) {
                    try (var ementaryDoc = new XWPFDocument(new FileInputStream(Paths.get(ementaryPath.getPath()).toFile()))) {

                        List<XWPFTable> tables = ementaryDoc.getTables();

                        CTTbl xmlTblToCopy = tables.getFirst().getCTTbl();

                        try (XmlCursor insertCursor = paragraph.getCTP().newCursor()) {
                            for (var _ : list.getList()) {
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
            var table = doc.getTableArray(currentTable.getCounter().addAndGet(3));

            for (var cc : list.getList()) {
                List<XWPFTableRow> rows = table.getRows();
                var currentRow = rows.getFirst();
                currentRow.getCell(0).setText("Componente Curricular: " + cc.name());
                currentRow.getCell(1).setText("Créditos: " + cc.credits());

                currentRow = rows.get(1);
                currentRow.getCell(0).setText("Carga horária: " + cc.ha());
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
