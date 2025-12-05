package br.ifpe.edu.replacers;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class PlaceholderReplacer implements IReplacer {
    @Override
    public void replace() throws IOException {
        try (var fis =DocumentHelper.loadResourceStream("ppc.docx");
             var document = new XWPFDocument(fis)) {

            replaceInDocument(document);
            replaceInHeaders(document);
            replaceInFooters(document);

            try (var out = new FileOutputStream(DocumentHelper.INSTANCE.getOutputPath().toFile())) {
                document.write(out);
            }
        }
    }

    private void replaceInDocument(XWPFDocument doc) {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            replaceInParagraph(paragraph);
        }

        for (XWPFTable table : doc.getTables()) {
            table.getRows().forEach(row ->
                    row.getTableCells().forEach(cell ->
                            cell.getParagraphs().forEach(this::replaceInParagraph)
                    )
            );
        }
    }

    private void replaceInHeaders(XWPFDocument doc) {
        for (XWPFHeader header : doc.getHeaderList()) {
            for (XWPFParagraph paragraph : header.getParagraphs()) {
                replaceInParagraph(paragraph);
            }
        }
    }

    private void replaceInFooters(XWPFDocument doc) {
        for (XWPFFooter footer : doc.getFooterList()) {
            for (XWPFParagraph paragraph : footer.getParagraphs()) {
                replaceInParagraph(paragraph);
            }
        }
    }

    private void replaceInParagraph(XWPFParagraph paragraph) {
        for (XWPFRun run : paragraph.getRuns()) {

            if (run.getText(0) == null) continue;

            String text = run.getText(0);

            for (var ph : PlaceholderList.INSTANCE.getList()) {
                text = text.replace(ph.getKey(), ph.getValue());
            }

            run.setText(text, 0);
        }
    }
}
