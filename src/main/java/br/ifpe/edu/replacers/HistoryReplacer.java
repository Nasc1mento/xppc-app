package br.ifpe.edu.replacers;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.readers.CampusReader;
import br.ifpe.edu.replacers.helpers.DocumentPath;
import br.ifpe.edu.replacers.helpers.ParagraphFinder;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlCursor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class HistoryReplacer implements IReplacer {

    private final Path docPath = DocumentPath.INSTANCE.getOutputPath();
    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;
    private final CampusReader campusReader = new CampusReader();

    @Override
    public void replace() {
        try (var doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {
            XWPFParagraph placeholderParagraph = ParagraphFinder.get(doc, "$$historico_do_campus$$");

            if (placeholderParagraph != null) {
                String historyFileName = campusReader.getByNameAndColumn(
                        placeholderList.getValue("campus"), CampusReader.Columns.HISTORY_COLUMN
                ) + ".docx";

                URL historyPath = Thread.currentThread().getContextClassLoader().getResource(historyFileName);

                if (historyPath != null) {
                    try (var historyDoc = new XWPFDocument(DocumentPath.loadResourceStream(historyFileName))) {

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

            save(doc);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        save();
    }
}