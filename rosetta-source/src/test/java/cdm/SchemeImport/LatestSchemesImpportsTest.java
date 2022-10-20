package cdm.SchemeImport;

import com.regnosys.granite.ingestor.ExpectationUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LatestSchemesImpportsTest {

    private static final String schemaPath = "src/main/resources/coding-schemes/fpml";
    private static final String codeListZip = "src/main/resources/coding-schemes/fpml/codelist.zip";

    private static final String codeList = "src/main/resources/coding-schemes/fpml/codelist";

    private static final boolean execute_Download_Latest_Version = ExpectationUtil.WRITE_EXPECTATIONS;

    @Test
    public void downloadLatestVersions() throws IOException {
        if (execute_Download_Latest_Version) {
            URL website = new URL("https://www.fpml.org/spec/coding-scheme/codelist.zip");

            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(LatestSchemesImpportsTest.codeListZip);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            //Unzip from CodeList just being downloaded
            unzip();
            //Move the unzipped files to SchemePath if not already exists
            moveFilesToFpml();
            //  InputStream inputStream = new URL("https://www.fpml.org/coding-scheme/set-of-schemes").openStream();
            //  Files.copy(inputStream, Paths.get("src/main/resources/" + schemaPath), StandardCopyOption.REPLACE_EXISTING);
            deletFileFolder(new File(LatestSchemesImpportsTest.codeListZip));
            deletFileFolder(new File(LatestSchemesImpportsTest.codeList));
        }
    }

    public static void unzip() {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(codeListZip).toFile().toPath()))) {

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


    public void moveFilesToFpml() {
        Path sourceDir = Paths.get(codeList);
        Path destinationDir = Paths.get(schemaPath);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDir)) {
            for (Path path : directoryStream) {
                System.out.println("copying " + path.toString());
                Path d2 = destinationDir.resolve(path.getFileName());
                System.out.println("destination File=" + d2);
                if (Files.notExists(d2))
                    Files.move(path, d2, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deletFileFolder(File fileToBeDeleted) {
        File[] contents = fileToBeDeleted.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deletFileFolder(f);
                }
            }
        }
        fileToBeDeleted.delete();
    }

}