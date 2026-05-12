package br.edu.ifpe.core.replacers;

import br.edu.ifpe.core.PlaceholderManager;
import br.edu.ifpe.infra.storage.csv.CampusReader;
import br.edu.ifpe.infra.doc.DocumentManager;
import br.edu.ifpe.infra.doc.DocumentCursor;
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

                var campus = campusReader.getByName(placeholderManager.getValue("campus"));

                if (campus != null) {
                    String historyFileName = "docx/histories/" + campus.history() + ".docx";

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
                }

                int pos = doc.getPosOfParagraph(placeholderParagraph);
                doc.removeBodyElement(pos);
            }

            commit(doc);
        }
    }
}