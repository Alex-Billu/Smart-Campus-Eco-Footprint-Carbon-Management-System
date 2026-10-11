package com.campus.eco.service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QRServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void generateReturnsScannableQrImage() throws Exception {
        String payload = "campus resource 42";

        BufferedImage image = QRService.generate(payload);

        assertEquals(240, image.getWidth());
        assertEquals(240, image.getHeight());
        assertEquals(payload, decode(image));
    }

    @Test
    void savePngWritesScannableImageToRequestedPath() throws Exception {
        String payload = "energy meter north wing";
        Path output = tempDir.resolve("resource.png");

        QRService.savePng(payload, output.toString());

        assertTrue(Files.isRegularFile(output));
        BufferedImage image = ImageIO.read(output.toFile());
        assertNotNull(image);
        assertEquals(240, image.getWidth());
        assertEquals(payload, decode(image));
    }

    private static String decode(BufferedImage image) throws Exception {
        BinaryBitmap bitmap = new BinaryBitmap(
                new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        return new MultiFormatReader().decode(bitmap).getText();
    }
}
