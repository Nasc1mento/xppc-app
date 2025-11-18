package br.ifpe.edu.replacers;

import br.ifpe.edu.PlaceholderList;
import br.ifpe.edu.readers.CampusReader;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class HistoryReplacer implements IReplacer {

    private final Path docPath;

    public HistoryReplacer(Path docPath) {
        this.docPath = docPath;
    }

    private final PlaceholderList placeholderList = PlaceholderList.INSTANCE;
    private final CampusReader campusReader = new CampusReader();

    @Override
    public void replace() throws IOException {
        Path temp = Path.of("ppc_temp.docx");

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath.toFile()))) {
            XWPFParagraph paragraph = ParagraphFinder.get(doc, "$$historico_do_campus$$");

            if (paragraph != null) {
                String historyFileName = campusReader.getByNameAndColumn(
                        placeholderList.getValue("campus"), CampusReader.Columns.HISTORY_COLUMN
                ) + ".docx";

                URL historyPath = Thread.currentThread().getContextClassLoader().getResource(historyFileName);

                if (historyPath != null) {
                    try (XWPFDocument historyDoc = new XWPFDocument(new FileInputStream(Paths.get(historyPath.getPath()).toFile()))) {
                        StringBuilder sb = new StringBuilder();
                        for (XWPFParagraph p : historyDoc.getParagraphs()) {
                            sb.append(p.getText()).append("\n");
                        }
                        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                            paragraph.removeRun(i);
                        }
                        XWPFRun run = paragraph.createRun();
                        run.setText(sb.toString());
                    }
                }
            }

            try (var out = new FileOutputStream(temp.toFile())) {
                doc.write(out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.move(temp, docPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}