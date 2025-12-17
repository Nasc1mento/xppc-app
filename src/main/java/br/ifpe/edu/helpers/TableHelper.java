package br.ifpe.edu.helpers;

import br.ifpe.edu.services.DocumentManager;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.IOException;

public class TableHelper {

    public static void copySimpleTbl(final XWPFDocument docTarget, final XWPFParagraph paragraphToInsert, final String resourceName, final int qty) throws IOException {
        try (var dcDoc = new XWPFDocument(DocumentManager.loadResourceStream(resourceName))) {
            for (int i = 0; i < qty; i++) {
                CTTbl xmlTbl = dcDoc.getTables().getFirst().getCTTbl();
                try (XmlCursor insertCursor = paragraphToInsert.getCTP().newCursor()) {
                    XWPFTable newTbl = docTarget.insertNewTbl(insertCursor);
                    newTbl.getCTTbl().set(xmlTbl.copy());
                    XWPFParagraph newP = docTarget.insertNewParagraph(newTbl.getCTTbl().newCursor());
                    insertCursor.toCursor(newP.getCTP().newCursor());
                }
            }

            int pos = docTarget.getPosOfParagraph(paragraphToInsert);
            docTarget.removeBodyElement(pos);
        }
    }

    public static void copySimpleTbl(final XWPFDocument docTarget, final XWPFParagraph paragraphToInsert, final String resourceName) throws IOException {
        copySimpleTbl(docTarget, paragraphToInsert, resourceName, 1);
    }
}
