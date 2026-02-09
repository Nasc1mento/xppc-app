package br.edu.ifpe.services.replacers;

import br.edu.ifpe.services.PlaceholderManager;
import br.edu.ifpe.readers.CampusReader;
import br.edu.ifpe.services.DocumentManager;
import br.edu.ifpe.services.DocumentCursor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlCursor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class HistoryReplacer implements IReplacer {

    private final DocumentCursor documentCursor = DocumentCursor.INSTANCE;
    private final PlaceholderManager placeholderManager = PlaceholderManager.INSTANCE;
    private final CampusReader campusReader = CampusReader.INSTANCE;

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public void replace() throws IOException {
        try (var doc = new XWPFDocument(new FileInputStream(DocumentManager.INSTANCE.getTempPath().toFile()))) {

            XWPFParagraph placeholderParagraph = documentCursor.find(doc, "@@historico_do_campus@@");

            if (placeholderParagraph != null) {
                String historyFileName = campusReader.getByName(placeholderManager.getValue("campus")).history();

                URL historyPath = Thread.currentThread().getContextClassLoader().getResource(historyFileName);

                if (historyPath != null) {
                    try (var historyDoc = new XWPFDocument(DocumentManager.loadResourceStream(historyFileName))) {

                        try (XmlCursor cursor = placeholderParagraph.getCTP().newCursor()) {
                            for (XWPFParagraph pHistory : historyDoc.getParagraphs()) {
                                XWPFParagraph newP = doc.insertNewParagraph(cursor);
                                newP.getCTP().set(pHistory.getCTP());
                                cursor.toCursor(newP.getCTP().newCursor());
                            }
                        }
                    }
                }

                int pos = doc.getPosOfParagraph(placeholderParagraph);
                doc.removeBodyElement(pos);
            }

            commit(doc);
        }
    }
}