package br.edu.ifpe.infra.doc.helpers;

import org.apache.poi.xwpf.usermodel.XWPFRun;

public class TextHelper {
    public static void setTextNBreak(final XWPFRun run, final String text) {
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
