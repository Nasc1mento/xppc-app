package br.ifpe.edu.replacers;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class PlaceholderReplacer implements IReplacer {
    @Override
    public void replace() throws IOException {
        try (var doc = new XWPFDocument(new FileInputStream(DocumentHelper.getTempPath().toFile()))) {
            replaceInDocument(doc);
            replaceInHeaders(doc);
            replaceInFooters(doc);
            commit(doc);
        }
    }

    private void replaceInDocument(XWPFDocument doc) {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            replaceInParagraph(paragraph);
        }

        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceInParagraph(paragraph);
                    }
                }
            }
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

    private void replaceInParagraph(XWPFParagraph p) {
        for (XWPFRun run : p.getRuns()) {

            if (run.getText(0) == null) continue;

            String text = run.getText(0);

            for (var ph : PlaceholderList.INSTANCE.getList()) {
                text = text.replace(ph.getKey(), ph.getValue());
            }

            run.setText(text, 0);
        }
    }
}
