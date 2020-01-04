package com.wari.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wari.model.Islem;
import com.wari.model.Musteri;
import com.wari.model.Odeme;
import com.wari.model.User;
import com.wari.repository.IslemRepository;
import com.wari.repository.UserRepository;
import com.wari.security.UserPrincipal;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Controller
public class PageController {


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

    @RequestMapping("/")
    public String anasayfa(Model model) {
        model.addAttribute("ogrenci_sayisi", musteriService.musteriSayisi());
        model.addAttribute("bugunki_kazanc", odemeService.bugunkiKazanc());
        model.addAttribute("bugunki_kayit", musteriService.bugunkiKayitSayisi());
        model.addAttribute("son_islemler", islemService.findLastFive());
        model.addAttribute("sorunlu_islemler", islemService.sorunluIslemler());
        model.addAttribute("son_odemeler", odemeService.bugunkiOdemeler());
        return "anasayfa.html";
    }

    @RequestMapping("/ogrenciler")
    public String ogrenciler(Model model){
        model.addAttribute("musteri", new Musteri());
        model.addAttribute("searchresult", musteriService.findLastRecord());
        return "ogrenciler.html";
    }

    @RequestMapping("/calisanlar")
    public String calisanlar(Model model){
        model.addAttribute("calisanlar", userService.findAllByRoleEqUser());
        model.addAttribute("calisan", new User());
        return "calisanlar.html";
    }

    @RequestMapping("/calisanDisable/{id}")
    public String calisanDisable(@PathVariable("id") int id, Model model){
        userService.setDisableUser(id);
        model.addAttribute("calisanlar", userService.findAllByRoleEqUser());
        model.addAttribute("calisan", new User());
        return "calisanlar.html";
    }

    @RequestMapping("/calisanActive/{id}")
    public String calisanActive(@PathVariable("id") int id, Model model){
        userService.setActiveUser(id);
        model.addAttribute("calisanlar", userService.findAllByRoleEqUser());
        model.addAttribute("calisan", new User());
        return "calisanlar.html";
    }

    @RequestMapping("/calisan/{id}")
    public String calisanlar(@PathVariable("id") int id, Model model){
        model.addAttribute("calisan", userService.findById(id));
        model.addAttribute("odemeler", odemeService.findAllByOdemeAlanId(id));
        return "calisan.html";
    }

    @RequestMapping("/musteri/{id}")
    public String musteri(@PathVariable("id") int id, Model model){
        model.addAttribute("musteri", musteriService.getMusteri(id));
        model.addAttribute("islemler", islemService.finfByMusteriId(id));
        model.addAttribute("odeme", new Odeme());
        model.addAttribute("islem", new Islem());
        return "musteri.html";
    }

    @PostMapping("/musteri/{id}/update")
    public String musteri(@PathVariable("id") int id,@ModelAttribute Musteri musteri){
        musteriService.save(id,musteri);
        return "redirect:/musteri/"+id+"?updated";
    }

    @PostMapping("kayit")
    public String validateKayit(@Valid Musteri musteri, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "redirect:/ogrenciler";
        }
        musteri.setKayitTarihi(LocalDate.now());
        musteriService.save(musteri);
        return "redirect:/musteri/"+musteri.getId()+"?kayit";
    }

    @PostMapping("/calisanKayit")
    public String calisanKayit(@ModelAttribute User user, BindingResult bindingResult){
        userService.save(user);
        return "redirect:/calisan/" + user.getId();
    }

    @PostMapping("islemEkle/{musteri_id}")
    public String islemEkle(@ModelAttribute Islem islem, @PathVariable("musteri_id") int musteri_id){
        islemService.save(islem, musteri_id);
        return "redirect:/musteri/"+ musteri_id+"?islem";
    }

    @PostMapping("odeme/{musteri_id}/{islem_index}")
    public String odemeByMusteriIdAndOdemeIndex(@PathVariable("musteri_id") int musteri_id, @PathVariable("islem_index") int islem_index, @ModelAttribute Odeme odeme, Principal principal){
        odemeService.saveOdemeByMusteriIdAndIslemId(musteri_id, islem_index, odeme, principal);
        return "redirect:/musteri/"+musteri_id+"?odeme";
    }

    @RequestMapping(value = "/bul", produces=MediaType.TEXT_PLAIN_VALUE)
    public String musteribull(@RequestParam(value = "keyword", required = false) String q, Model model){

        if (q.contains(" ")){
            model.addAttribute("searchresult", musteriService.findByNameOrSurname(q.split(" ")[0],q.split(" ")[1]));
        }else {
            model.addAttribute("searchresult", musteriService.findByNameContaining(q));
        }

        //ModelAndView modelAndView = new ModelAndView("ogrenciler");
        model.addAttribute("musteri", new Musteri());

        return "ogrenciler";
    }


    @RequestMapping(value = "/calisan/{id}/pdf", method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> calisanOdemelerPdf(@PathVariable("id") int id)  throws Exception{
        ByteArrayInputStream byteArrayInputStream = userOdemeler(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Contoent-Disposition","inline=userodemeler.pdf");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4,16,16,32,16);
        PdfWriter.getInstance(document,bos);

        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));

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
            com.itextpdf.text.Font headfont = FontFactory.getFont(FontFactory.TIMES_ROMAN,12, BaseColor.WHITE);
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

            for ( Odeme odeme: islem.getOdemeler() ){
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
            Font fontHeader = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
            Font font1 = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);

            para = new Paragraph("X Okulu ", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            para = new Paragraph(" ", fontHeader);
            document.add(para);
            para = new Paragraph(" ", fontHeader);
            document.add(para);

            para = new Paragraph(formattedDate, font);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            para = new Paragraph("             Ürün Açıklamaası : "+ islem.getAciklama(), font1);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);

            para = new Paragraph(" ", font);
            document.add(para);

            document.add(table);

            para = new Paragraph(" ", fontHeader);
            document.add(para);
            para = new Paragraph(" ", fontHeader);
            document.add(para);


            para = new Paragraph("Müsteri Bilgileri : "+ musteri.getAd() + " " +  musteri.getSoyad() + "              ", font1);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private ByteArrayInputStream userOdemeler(int user_id) {
        Document document = new Document();
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        try {
            com.itextpdf.text.Font headfont = FontFactory.getFont(FontFactory.TIMES_ROMAN,12, BaseColor.WHITE);
            List<String> headers = new ArrayList<>();
            headers.add("No");
            headers.add("Musteri");
            headers.add("Islem");
            headers.add("Ödenen Tutar");
            headers.add("Islem Tarihi");

            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(90);
            table.setWidths(new int[]{1,4,3,2,3});

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

            List<Odeme> odemes = odemeService.findAllByOdemeAlanId(user_id);

            int count = 0;
            int toplamIslemTutari = 0;

            for ( Odeme odeme: odemes ){
                count++;
                toplamIslemTutari += odeme.getOdenenTutar();

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(String.valueOf(count), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(odeme.getIslem().getMusteri().getAd() + " " + odeme.getIslem().getMusteri().getSoyad()), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(odeme.getIslem().getAciklama()), font));
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
            Font fontHeader = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
            Font font1 = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);

            para = new Paragraph("X Okulu ", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            para = new Paragraph(" ", fontHeader);
            document.add(para);
            para = new Paragraph(" ", fontHeader);
            document.add(para);

            para = new Paragraph(formattedDate, font);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            User user = userService.findById(user_id);

            para = new Paragraph("              "+  user.getAd() + " " + user.getSoyad() + " Aldigi odemeler", font1);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);

            para = new Paragraph(" ", font);
            document.add(para);

            document.add(table);

            para = new Paragraph(" ", fontHeader);
            document.add(para);
            para = new Paragraph(" ", fontHeader);
            document.add(para);


            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            para = new Paragraph("Toplam : "+ toplamIslemTutari  + " tl             ", font1);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }






}
