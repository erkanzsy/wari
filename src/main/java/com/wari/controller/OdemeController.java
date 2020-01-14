package com.wari.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wari.model.Islem;
import com.wari.model.Musteri;
import com.wari.model.Odeme;
import com.wari.repository.IslemRepository;
import com.wari.services.IslemService;
import com.wari.services.MusteriService;
import com.wari.services.OdemeService;
import com.wari.services.UserService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OdemeController {

    @Autowired
    MusteriService musteriService;

    @Autowired
    IslemService islemService;

    @Autowired
    OdemeService odemeService;

    @Autowired
    IslemRepository islemRepository;

    @Autowired
    UserService userService;

    @PostMapping("odeme/{musteri_id}/{islem_index}")
    public String odemeByMusteriIdAndOdemeIndex(@PathVariable("musteri_id") int musteri_id, @PathVariable("islem_index") int islem_index, @ModelAttribute Odeme odeme, Principal principal){
        if (odemeService.saveOdemeByMusteriIdAndIslemId(musteri_id, islem_index, odeme, principal)){
            return "redirect:/musteri/"+musteri_id+"?odeme";
        }else{
            return "redirect:/musteri/"+musteri_id+"?odemeError";
        }
    }

    @RequestMapping(value = "odemepdf/{musteri_id}/{islem_index}",method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> pdf(@PathVariable("musteri_id") int musteri_id, @PathVariable("islem_index") int islem_index) throws Exception{

        ByteArrayInputStream byteArrayInputStream = musteriOdemeler(musteri_id,islem_index);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Contoent-Disposition","inline=users.pdf");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4,16,16,32,16);
        PdfWriter.getInstance(document,bos);

        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
    }

    private ByteArrayInputStream musteriOdemeler(int musteri_id, int islem_index) {
        Document document = new Document();
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        try {
            Font headfont = FontFactory.getFont(FontFactory.TIMES_ROMAN,12, BaseColor.WHITE);
            List<String> headers = new ArrayList<>();
            headers.add("No");
            headers.add("Ödenen Tutar");
            headers.add("Islem Tarihi");

            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(90);
            table.setWidths(new int[]{1,4,4});

            for (String string : headers){
                PdfPCell cell;
                cell = new PdfPCell(new Phrase(string,headfont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                table.addCell(cell);
            }


            Font font = FontFactory.getFont(FontFactory.COURIER, 8);

            BaseColor lgray = BaseColor.LIGHT_GRAY;
            BaseColor gray = BaseColor.WHITE;

            Islem islem = islemRepository.findByMusteriId(musteri_id).get(islem_index);

            Musteri musteri = musteriService.getMusteri(musteri_id);

            int count = 0;
            int toplam = 0;

            for ( Odeme odeme: islem.getOdemeler() ){
                toplam += odeme.getOdenenTutar();
                count++;

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(String.valueOf(count), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(odeme.getOdenenTutar()), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(odeme.getIslemTarihi()), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

            }

            LocalDateTime myDateObj = LocalDateTime.now();

            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String formattedDate = myDateObj.format(myFormatObj);

            PdfWriter.getInstance(document,out);
            document.open();
            Paragraph para = new Paragraph();
            Font fontHeader = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
            Font font1 = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);

            para = new Paragraph("WARI", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            para = new Paragraph(" ", fontHeader);
            document.add(para);
            para = new Paragraph(" ", fontHeader);
            document.add(para);

            para = new Paragraph(formattedDate, font);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            para = new Paragraph("             Ürün Aciklamasi : "+ islem.getAciklama(), font1);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);

            para = new Paragraph(" ", font);
            document.add(para);

            document.add(table);

            para = new Paragraph(" ", fontHeader);
            document.add(para);
            para = new Paragraph(" ", fontHeader);
            document.add(para);

            para = new Paragraph("Anlaşılan Tutar : " + islem.getTutar()+ "              ", font1);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            para = new Paragraph("  Odenen Tutar : " + toplam + "              ", font1);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            para = new Paragraph("Müsteri Bilgileri : " + musteri.getAd() + " " +  musteri.getSoyad() + "              ", font1);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}
