package com.campus.eco.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRService {

    public static BufferedImage generate(String text) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 240, 240, hints);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public static void savePng(String text, String path) throws WriterException, IOException {
        BufferedImage img = generate(text);
        ImageIO.write(img, "PNG", new File(path));
    }
}
