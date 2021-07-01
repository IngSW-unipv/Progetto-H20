package carpool.controllers;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import carpool.configs.MyConstants;

@Controller
public class EmailController {
	
	@Autowired
    public JavaMailSender emailSender;
	
	@RequestMapping("/sendSimpleEmail")
    public String sendSimpleEmail() {
		// Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
         
        message.setTo(MyConstants.FRIEND_EMAIL);
        message.setSubject("Test Simple Email");
        message.setText("Hello, Im testing Simple Email");
 
        // Send Message!
        this.emailSender.send(message);
 
        return "Email Sent!";
        }
	
	@ResponseBody
	@RequestMapping("/sendAttachmentEmail")
	public String sendAttachmentEmail() throws MessagingException {
 
        MimeMessage message = emailSender.createMimeMessage();
 
        boolean multipart = true;
 
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);
 
        helper.setTo(MyConstants.FRIEND_EMAIL);
        helper.setSubject("Test email with attachments");
         
        helper.setText("Hello, Im testing email with attachments!");
         
        String path1 = "\\\\Mac\\Home\\Desktop\\SpringCarpool\\im.jpg";
        String path2 = "\\\\Mac\\Home\\Desktop\\SpringCarpool\\prova2.txt";
 
        // Attachment 1
        FileSystemResource file1 = new FileSystemResource(new File(path1));
        helper.addAttachment("Patente ", file1);
 
        // Attachment 2
        FileSystemResource file2 = new FileSystemResource(new File(path2));
        helper.addAttachment("Patente", file2);
 
        emailSender.send(message);
        return "Email Sent!";
    }
	
	@ResponseBody
    @RequestMapping("/sendHtmlEmail")
    public String sendHtmlEmail() throws MessagingException {
 
        MimeMessage message = emailSender.createMimeMessage();
 
        boolean multipart = true;
         
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
         
        String htmlMsg = "<h3>Im testing send a HTML email</h3>"
                +"<img src='https://cdn.babymarkt.com/babymarkt/img/111891/443/big-bobby-car-classic-rosso-a006497.jpg'>";
         
        message.setContent(htmlMsg, "text/html");
        helper.setTo(MyConstants.FRIEND_EMAIL);
        helper.setSubject("Test send HTML email");
     
        this.emailSender.send(message);
 
        return "Email Sent!";
        }
}
