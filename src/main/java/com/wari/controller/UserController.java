package com.wari.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wari.model.Odeme;
import com.wari.model.User;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

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

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/users")
    public String usersPage(Model model){
        model.addAttribute("users", userService.findAllByRoleEqUser());
        model.addAttribute("user", new User());
        return "users.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/user/{id}")
    public String userPage(@PathVariable("id") int id, Model model){
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("odemeler", odemeService.findAllByOdemeAlanId(id));
        return "user.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/userDisable/{id}")
    public String userDisable(@PathVariable("id") int id){
        userService.setDisableUser(id);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('SUPER')")
    @RequestMapping("/userRole/{id}")
    public String userActive(@PathVariable("id") int id){
        userService.changeRole(id);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/userActive/{id}")
    public String userRole(@PathVariable("id") int id){
        userService.setActiveUser(id);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/userKayit")
    public String userKayit(@ModelAttribute User user){
        if (userService.save(user))
            return "redirect:/user/" + user.getId();
        else
            return "redirect:/users?error";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/user/{id}/pdf", method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> userOdemelerPdf(@PathVariable("id") int id)  throws Exception{
        ByteArrayInputStream byteArrayInputStream = userOdemeler(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Contoent-Disposition","inline=userodemeler.pdf");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4,16,16,32,16);
        PdfWriter.getInstance(document,bos);

        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));

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