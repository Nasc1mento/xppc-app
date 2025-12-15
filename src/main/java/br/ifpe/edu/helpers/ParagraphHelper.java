package br.ifpe.edu.helpers;

import org.apache.poi.xwpf.usermodel.*;

import java.util.List;
public class ParagraphHelper {

    private static int lastBodyElIndex = 0;
    private static int lastTableCount = 0;

    private static final TableLocationHelper tblHelper = TableLocationHelper.INSTANCE;

    private ParagraphHelper() {}

    public static XWPFParagraph find(XWPFDocument doc, String placeholder) {
        final List<IBodyElement> bodyElements = doc.getBodyElements();
        int currentTableCount = lastTableCount;
        int tablesToSkip = tblHelper.getValue();

        for (int i = lastBodyElIndex; i < bodyElements.size(); i++) {
            IBodyElement el = bodyElements.get(i);

            if (el instanceof XWPFTable) {
                currentTableCount++;
            }

            if (el instanceof XWPFParagraph p) {
                String text = p.getText();
                if (text != null && text.contains(placeholder) && currentTableCount >= tablesToSkip) {
                    lastBodyElIndex = i + 1;
                    lastTableCount = currentTableCount;
                    tblHelper.getCounter().set(currentTableCount);
                    return p;
                }
            }
        }

        return null;
    }

    public static void reset() {
        lastBodyElIndex = 0;
        lastTableCount = 0;
    }

    public static void setTextNBreak(XWPFRun run, String text) {
        if (text != null) {
            String[] lines = text.split("\n");
            for (int i = 0; i < lines.length; i++) {
                run.setText(lines[i]);
                if (i < lines.length - 1) {
                    run.addBreak();
                }
            }
        }
    }
}
