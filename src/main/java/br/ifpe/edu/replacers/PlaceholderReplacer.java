package br.ifpe.edu.replacers;

import br.ifpe.edu.PlaceholderList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public record PlaceholderReplacer(Path source, Path destination) implements IReplacer {
    @Override
    public void replace() throws IOException {
        var tempFilePath = source + ".temp";

        try (var zipFile = new ZipFile(source.toString()); var zos = new ZipOutputStream(new FileOutputStream(tempFilePath))) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream is = zipFile.getInputStream(entry);
                var newEntry = new ZipEntry(entry.getName());
                zos.putNextEntry(newEntry);
                if (entry.getName().matches("word/(document|header\\d+|footer\\d+)\\.xml")) {
                    var xml = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    for (var ph : PlaceholderList.INSTANCE.getList()) {
                        xml = xml.replace(ph.getKey(), ph.getValue());
                    }
                    zos.write(xml.getBytes(StandardCharsets.UTF_8));
                } else {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        zos.write(buffer, 0, len);
                    }
                }
                is.close();
                zos.closeEntry();
            }
        }

        Files.copy(Paths.get(tempFilePath), destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
