/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Fabryka kodów QR wykorzystywana przy generowaniu etykiet wysyłkowych.
 * <p>
 * Generuje dwuwymiarowy kod QR w formacie PNG przy użyciu biblioteki ZXing i
 * zapisuje go jako plik graficzny w zasobach projektu. Wygenerowany plik jest
 * następnie osadzany w szablonie etykiety HTML przez klasę
 * {@link LabelFactory}.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class QRCodeFactory {
    
    /**
     * Generuje kod QR na podstawie podanego tekstu i zapisuje go jako plik PNG.
     * <p>
     * Kod QR jest generowany w rozdzielczości 300 x 300 pikseli i zapisywany
     * pod stałą ścieżką {@code src/main/resources/qrcode.png}, nadpisując
     * poprzednio wygenerowany plik. Typowym zastosowaniem jest przekazanie
     * identyfikatora paczki jako zakodowanego tekstu.
     * </p>
     *
     * @param URL tekst do zakodowania w kodzie QR, np. identyfikator paczki;
     * nazwa parametru nawiązuje do typowego użycia kodów QR jako odnośników,
     * jednak może to być dowolny ciąg znaków
     * @throws WriterException jeśli wystąpił błąd podczas kodowania danych do
     * macierzy kodu QR
     * @throws IOException jeśli wystąpił błąd podczas zapisu pliku PNG na dysk
     */
    
    static void generate(String URL) throws WriterException, IOException {
        String path = "src/main/resources/qrcode.png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(URL, BarcodeFormat.QR_CODE, 300, 300);

        Path outputPath = FileSystems.getDefault().getPath(path);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", outputPath);
        System.out.println("Kod QR wygenerowany.");
    } 
}
