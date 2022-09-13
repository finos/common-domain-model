package cdm.SchemeImport;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class LatestSchemesImpportsTest {

    private static final String schemaPath = "src/main/resources/coding-schemes/fpml";
    private static final String codelist = "src/main/resources/coding-schemes/fpml/codelist.zip";


    @Test
    public void downloadLatestVersions() throws IOException {

        URL website = new URL("https://www.fpml.org/spec/coding-scheme/codelist.zip");

        File codeListZip = new File(codelist);
        if (codeListZip.exists()){
            codeListZip.delete();
        }

        File codeList = new File("src/main/resources/coding-schemes/fpml/codelist");
        if (codeList.exists()){
            codeList.delete();
        }
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(codelist);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        unzip();
      //  InputStream inputStream = new URL("https://www.fpml.org/coding-scheme/set-of-schemes").openStream();
      //  Files.copy(inputStream, Paths.get("src/main/resources/" + schemaPath), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void unzip(){
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(codelist).toFile().toPath())))

        {

            // list files in zip
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                // Check for zip slip vulnerability attack
                Path newUnzipPath = zipSlipVulnerabilityProtect(zipEntry, Paths.get(schemaPath));

                boolean isDirectory = false;
                //check for files or directory
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                if (isDirectory) {
                    Files.createDirectories(newUnzipPath);
                } else {

                    if (newUnzipPath.getParent() != null) {
                        if (Files.notExists(newUnzipPath.getParent())) {
                            Files.createDirectories(newUnzipPath.getParent());
                        }
                    }

                    // copy files using nio
                    Files.copy(zipInputStream, newUnzipPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Check for zip slip attack
    public static Path zipSlipVulnerabilityProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        /*
          resolve(String other) method of java. nio. file.Path used to converts a given
          path string to a Path and resolves it against this Path in the exact same manner
          as specified by the resolve method
         */
        Path dirResolved = targetDir.resolve(zipEntry.getName());

        /*
          Normalizing a path involves modifying the string that identifies a
          path or file so that it conforms to a valid path on the target operating system.
         */
        //normalize the path on target directory or else throw exception
        Path normalizePath = dirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Invalid zip: " + zipEntry.getName());
        }

        return normalizePath;
    }

}