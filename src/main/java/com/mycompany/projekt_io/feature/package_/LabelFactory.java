/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;

import com.google.zxing.WriterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import com.mycompany.projekt_io.datamodel.Package;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

/**
 * Klasa w formie modelu fabryki etykiet wysyłkowych generowanych w formacie PDF.
 * <p>
 * Na podstawie danych paczki ({@link Package}) wypełnia szablon HTML danymi
 * nadawcy, odbiorcy, regionów oraz kodu QR, a następnie konwertuje go do pliku
 * PDF zapisywanego na pulpicie użytkownika. Szablon HTML pobierany jest z
 * zasobów projektu ({@code src/main/resources/szablon.html}). Kod QR generowany
 * jest przez {@link QRCodeFactory}.
 * </p>
 * 
 * @author Radosław Kruczek
 */
public class LabelFactory {
    
    Package p;
    QRCodeFactory q;

    /**
     * Tworzy instancję fabryki etykiet dla wskazanej paczki.
     *
     * @param p paczka, dla której zostanie wygenerowana etykieta; nie może być
     * {@code null}
     */
    public LabelFactory(Package p) {
        this.p = p;
    }
    
    
    /**
     * Generuje etykietę wysyłkową w formacie PDF i zapisuje ją na pulpicie
     * użytkownika.
     * <p>
     * Proces generowania przebiega następująco:
     * <ol>
     * <li>Generowany jest kod QR zawierający identyfikator paczki.</li>
     * <li>Wczytywany jest szablon HTML z zasobów projektu.</li>
     * <li>Pola szablonu uzupełniane są danymi paczki: regionem nadania,
     * regionem docelowym, identyfikatorem paczki, danymi nadawcy oraz danymi
     * odbiorcy.</li>
     * <li>Wypełniony dokument HTML konwertowany jest do PDF przy użyciu
     * biblioteki openhtmltopdf.</li>
     * <li>Plik PDF zapisywany jest pod ścieżką
     * {@code ~/Desktop/label.pdf}.</li>
     * </ol>
     * W przypadku błędu wejścia/wyjścia lub błędu generowania kodu QR metoda
     * zwraca {@code false}.
     * </p>
     *
     * @return {@code true} jeśli etykieta została pomyślnie wygenerowana i
     * zapisana, {@code false} jeśli wystąpił błąd {@link IOException} lub
     * {@link WriterException}
     */
    public boolean printLabel() {
        
        try {
            q.generate(String.format("%d", p.getPackage_id()));
            
            File inputHTML = new File("src/main/resources/szablon.html");
            
            Document document = Jsoup.parse(inputHTML, "UTF-8");
            
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        
            document.getElementById("region").html(p.getPackage_region().getRegion_name());
            document.getElementById("regionDest").html("<sub>PL/</sub>"+p.getPackage_dest_region().getRegion_name());
            document.getElementById("id").html("<sup>12345678911111666644</sup>"+String.format("%d", p.getPackage_id()));
            document.getElementById("nadawca").html(p.getPackage_sender().getSender_name()+
                                                    "<br>tel. "+p.getPackage_sender().getSender_phone()+
                                                    "<br>email: "+p.getPackage_sender().getSender_email()+
                                                    "<br>"+p.getPackage_sender().getSender_street()+
                                                    "<br>"+p.getPackage_sender().getSender_postcode()+
                                                    " "+p.getPackage_sender().getSender_city());
            document.getElementById("odbiorca").html(p.getPackage_recipient().getRecipient_name()+
                                                    "<br>tel. "+p.getPackage_recipient().getRecipient_phone()+
                                                    "<br>email: "+p.getPackage_recipient().getRecipient_email()+
                                                    "<br>"+p.getPackage_recipient().getRecipient_street()+
                                                    "<br>"+p.getPackage_recipient().getRecipient_postcode()+
                                                    " "+p.getPackage_recipient().getRecipient_city());
            
            OutputStream os = new FileOutputStream(System.getProperty("user.home") + "/Desktop/label.pdf");
            
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.toStream(os);
            String baseUrl = FileSystems.getDefault().getPath("src/main/resources/").toUri().toURL().toString();
            builder.withW3cDocument(new W3CDom().fromJsoup(document), baseUrl);
            builder.run();
            os.close();
            
        } catch (IOException | WriterException ex) {
             return false;    
        }
        
        return true;
    }
    
}
