package com.emersondev.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtil {

  @Autowired
  private JavaMailSender emailSender;

  public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("emersontec147@gmail.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    if (list != null && list.isEmpty()) {
        message.setCc(getCcArray(list));
        emailSender.send(message);
      }
    }

  public String[] getCcArray(List<String> cclist) {
    String[] cc = new String[cclist.size()];
    for (int i = 0; i < cclist.size(); i++) {
      cc[i] = cclist.get(i);
    }
    return cc;
  }

  public void forgetMail(String to, String subject, String password) throws MessagingException {
    MimeMessage message = emailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom("emersontec147@gmail.com");
    helper.setTo(to);
    helper.setSubject(subject);
    String htmlMSG = "<p><b> Login details for Cafe Management System</b></p><b>Email:</b> " + to + "<br><b>Password:</b> " + password + "<br><a href=\"http://localhost:4200/\">Click hete to login</a></p>";
    message.setContent(htmlMSG, "text/html");
    emailSender.send(message);
  }

}

