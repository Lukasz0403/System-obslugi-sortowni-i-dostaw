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
 *
 * @author mateu
 */
public class LabelFactory {
    
    Package p;
    QRCodeFactory q;

    public LabelFactory(Package p) {
        this.p = p;
    }
    
    
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
