package br.ifpe.edu.replacers;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.readers.CampusReader;
import br.ifpe.edu.replacers.helpers.DocumentHelper;
import br.ifpe.edu.replacers.helpers.ParagraphHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlCursor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class HistoryReplacer implements IReplacer {

    @Override
    public int getPriority() {
        return 60;
    }

    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;
    private final CampusReader campusReader = new CampusReader();

    @Override
    public void replace() throws IOException {
        try (var doc = new XWPFDocument(new FileInputStream(DocumentHelper.getTempPath().toFile()))) {
            XWPFParagraph placeholderParagraph = ParagraphHelper.find(doc, "$$historico_do_campus$$");

            if (placeholderParagraph != null) {
                String historyFileName = campusReader.getByNameAndColumn(
                        placeholderList.getValue("campus"), CampusReader.Columns.HISTORY
                ) + ".docx";

                URL historyPath = Thread.currentThread().getContextClassLoader().getResource(historyFileName);

                if (historyPath != null) {
                    try (var historyDoc = new XWPFDocument(DocumentHelper.loadResourceStream(historyFileName))) {

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