package dev.pinebale.minecraft.spigotofbauble29;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilZipper {
    /**
     * Zips a source directory into a zip file.
     *
     * @param sourceDir    The folder that contains the files/folders to be zipped
     * @param outputZipFile The output zip file to create
     * @throws IOException If any I/O error occurs during zipping
     */
    public static void zip(File sourceDir, File outputZipFile) throws IOException {
        Path sourcePath = sourceDir.toPath().toAbsolutePath();
        Path outputPath = outputZipFile.toPath().toAbsolutePath();

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(outputPath)); Stream<Path> paths = Files.walk(sourcePath)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    return;
                }

                Path relativePath = sourcePath.relativize(path);
                String entryName = relativePath.toString().replace(File.separator, "/");

                ZipEntry zipEntry = new ZipEntry(entryName);

                try {
                    zos.putNextEntry(zipEntry);
                    Files.copy(path, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    /**
     * Unzips a zip file to the specified output directory.
     *
     * @param zipFile        The input zip file
     * @param outputDirectory The directory where files will be extracted
     * @throws IOException If any I/O error occurs during unzipping
     */
    public static void unzip(File zipFile, File outputDirectory) throws IOException {
        try (InputStream is = Files.newInputStream(zipFile.toPath())) {
            unzip(is, outputDirectory);
        }
    }

    /**
     * Unzips from an InputStream to the specified output directory.
     *
     * @param inputStream     The input stream containing zip data
     * @param outputDirectory The target extraction directory
     * @throws IOException If any I/O error occurs
     */
    public static void unzip(InputStream inputStream, File outputDirectory) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream); ZipInputStream zis = new ZipInputStream(bis)) {
            unzip(zis, outputDirectory);
        }
    }

    /**
     * Low-level unzip method using ZipInputStream.
     *
     * @param zis             The ZipInputStream to read from
     * @param outputDirectory The target extraction directory
     * @throws IOException If any I/O error occurs
     */
    public static void unzip(ZipInputStream zis, File outputDirectory) throws IOException {
        Path targetPath = outputDirectory.toPath().normalize().toAbsolutePath();

        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            Path newPath = targetPath.resolve(entry.getName()).normalize().toAbsolutePath();

            if (!newPath.startsWith(targetPath)) {
                throw new IOException("Bad entry: " + entry.getName() + " → resolved to: " + newPath + " (outside " + targetPath + ")");
            }

            if (entry.isDirectory()) {
                Files.createDirectories(newPath);
            } else {
                Files.createDirectories(newPath.getParent());
                Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
            }

            zis.closeEntry();
        }
    }
}
