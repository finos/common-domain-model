package cdm.migration.fpml.source;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarRosettaSourceLoader {
    public List<RosettaSourceFile> load(Path jarPath, String rootInJar) throws IOException {
        String normalizedRoot = rootInJar.endsWith("/") ? rootInJar : rootInJar + "/";
        List<RosettaSourceFile> files = new ArrayList<RosettaSourceFile>();
        JarFile jarFile = new JarFile(jarPath.toFile());
        try {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                if (name.startsWith(normalizedRoot) && name.endsWith(".rosetta")) {
                    InputStream in = jarFile.getInputStream(entry);
                    try {
                        byte[] data = readAllBytes(in);
                        files.add(new RosettaSourceFile(name, new String(data, StandardCharsets.UTF_8)));
                    } finally {
                        in.close();
                    }
                }
            }
        } finally {
            jarFile.close();
        }
        Collections.sort(files, (a, b) -> a.getLogicalPath().compareTo(b.getLogicalPath()));
        return files;
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int read;
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        while ((read = inputStream.read(buffer)) >= 0) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }
}
