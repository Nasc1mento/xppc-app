package br.ifpe.edu.replacers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class Utils {

    private Utils() {}

    protected static XWPFParagraph getParagraph(XWPFDocument doc, String placeholder) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            String fullText = p.getText();
            if (fullText != null && fullText.contains(placeholder)) {
                return p;
            }
        }
        return null;
    }
}
