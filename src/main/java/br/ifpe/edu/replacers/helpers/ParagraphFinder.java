package br.ifpe.edu.replacers.helpers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class ParagraphFinder {

    private ParagraphFinder() {}

    public static XWPFParagraph get(XWPFDocument doc, String placeholder) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            String fullText = p.getText();
            if (fullText != null && fullText.contains(placeholder)) {
                return p;
            }
        }
        return null;
    }
}
