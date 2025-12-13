package br.ifpe.edu.replacers.helpers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class ParagraphHelper {

    private ParagraphHelper() {}

    public static XWPFParagraph find(XWPFDocument doc, String placeholder) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            String fullText = p.getText();
            if (fullText != null && fullText.contains(placeholder)) {
                return p;
            }
        }
        return null;
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
