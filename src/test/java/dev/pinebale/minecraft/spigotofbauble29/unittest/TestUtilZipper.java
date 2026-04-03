package dev.pinebale.minecraft.spigotofbauble29.unittest;

import dev.pinebale.minecraft.spigotofbauble29.UtilZipper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class TestUtilZipper {

    @TempDir
    Path tempInputDir;

    @TempDir
    Path tempOutputDir;

    private File mockDirectTxt1;
    private File mockDirectTxt2;

    private File mockDescendantDir1;
    private File mockDescendantDir2;
    private File mockDescendantTxt1;
    private File mockDescendantTxt2;

    private File mockIndirectDir;
    private File mockIndirectTxt;

    @BeforeEach
    void prepare() throws IOException {
        mockDirectTxt1 = tempInputDir.resolve("mockDirectTxt1.txt").toFile();
        Files.write(mockDirectTxt1.toPath(), "mockDirectTxt1.txt".getBytes(StandardCharsets.UTF_8));

        mockDirectTxt2 = tempInputDir.resolve("mockDirectTxt2.txt").toFile();
        Files.write(mockDirectTxt2.toPath(), "mockDirectTxt2.txt".getBytes(StandardCharsets.UTF_8));

        mockDescendantDir1 = tempInputDir.resolve("mockDescendantDir1").toFile();
        Assertions.assertTrue(mockDescendantDir1.mkdirs());

        mockDescendantDir2 = tempInputDir.resolve("mockDescendantDir2").toFile();
        Assertions.assertTrue(mockDescendantDir2.mkdirs());

        mockDescendantTxt1 = new File(mockDescendantDir1, "mockDescendantTxt1.txt");
        Files.write(mockDescendantTxt1.toPath(), "mockDescendantTxt1.txt".getBytes(StandardCharsets.UTF_8));

        mockDescendantTxt2 = new File(mockDescendantDir2, "mockDescendantTxt2.txt");
        Files.write(mockDescendantTxt2.toPath(), "mockDescendantTxt2.txt".getBytes(StandardCharsets.UTF_8));

        mockIndirectDir = new File(mockDescendantDir1, "mockIndirectDir");
        Assertions.assertTrue(mockIndirectDir.mkdirs());

        mockIndirectTxt = new File(mockIndirectDir, "mockIndirectTxt.txt");
        Files.write(mockIndirectTxt.toPath(), "mockIndirectTxt.txt".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testZipper() throws IOException {
        File outputZip = tempOutputDir.resolve("output.zip").toFile();

        Assertions.assertDoesNotThrow(() -> {
            UtilZipper.zip(tempInputDir.toFile(), outputZip);
            UtilZipper.unzip(outputZip, tempOutputDir.toFile());
        });

        Path outDirect1 = tempOutputDir.resolve("mockDirectTxt1.txt");
        Assertions.assertTrue(Files.exists(outDirect1));
        Assertions.assertTrue(Files.isRegularFile(outDirect1));
        Assertions.assertFalse(Files.isDirectory(outDirect1));
        Assertions.assertEquals("mockDirectTxt1.txt", new String(Files.readAllBytes(outDirect1), StandardCharsets.UTF_8));

        Path outDirect2 = tempOutputDir.resolve("mockDirectTxt2.txt");
        Assertions.assertTrue(Files.exists(outDirect2));
        Assertions.assertTrue(Files.isRegularFile(outDirect2));
        Assertions.assertFalse(Files.isDirectory(outDirect2));
        Assertions.assertEquals("mockDirectTxt2.txt", new String(Files.readAllBytes(outDirect2), StandardCharsets.UTF_8));

        Path outDir1 = tempOutputDir.resolve("mockDescendantDir1");
        Assertions.assertTrue(Files.exists(outDir1));
        Assertions.assertTrue(Files.isDirectory(outDir1));

        Path outDescTxt1 = outDir1.resolve("mockDescendantTxt1.txt");
        Assertions.assertTrue(Files.exists(outDescTxt1));
        Assertions.assertTrue(Files.isRegularFile(outDescTxt1));
        Assertions.assertEquals("mockDescendantTxt1.txt", new String(Files.readAllBytes(outDescTxt1), StandardCharsets.UTF_8));

        Path outIndirectDir = outDir1.resolve("mockIndirectDir");
        Assertions.assertTrue(Files.exists(outIndirectDir));
        Assertions.assertTrue(Files.isDirectory(outIndirectDir));

        Path outIndirectTxt = outIndirectDir.resolve("mockIndirectTxt.txt");
        Assertions.assertTrue(Files.exists(outIndirectTxt));
        Assertions.assertTrue(Files.isRegularFile(outIndirectTxt));
        Assertions.assertEquals("mockIndirectTxt.txt", new String(Files.readAllBytes(outIndirectTxt), StandardCharsets.UTF_8));

        Path outDir2 = tempOutputDir.resolve("mockDescendantDir2");
        Assertions.assertTrue(Files.exists(outDir2));
        Assertions.assertTrue(Files.isDirectory(outDir2));

        Path outDescTxt2 = outDir2.resolve("mockDescendantTxt2.txt");
        Assertions.assertTrue(Files.exists(outDescTxt2));
        Assertions.assertTrue(Files.isRegularFile(outDescTxt2));
        Assertions.assertEquals("mockDescendantTxt2.txt", new String(Files.readAllBytes(outDescTxt2), StandardCharsets.UTF_8));
    }
}