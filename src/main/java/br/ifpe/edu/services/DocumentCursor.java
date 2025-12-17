package br.ifpe.edu.services;

import br.ifpe.edu.helpers.TableTracker;
import org.apache.poi.xwpf.usermodel.*;

import java.util.List;


public enum DocumentCursor {

    INSTANCE;

    private int lastBodyElIndex = 0;
    private int lastTableCount = 0;

    private final TableTracker tblTracker = TableTracker.INSTANCE;

    public XWPFParagraph find(final XWPFDocument doc, final String placeholder) {
        final List<IBodyElement> bodyElements = doc.getBodyElements();
        int currentTableCount = lastTableCount;
        int tablesToSkip = tblTracker.getValue();

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
                    tblTracker.getCounter().set(currentTableCount);
                    return p;
                }
            }
        }

        return null;
    }

    public void reset() {
        lastBodyElIndex = 0;
        lastTableCount = 0;
        tblTracker.reset();
    }

}
