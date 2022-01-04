package com.mm.kim.mentormentee.common.mail;

import com.mm.kim.mentormentee.common.code.Config;
import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class EmailSender {

    JavaMailSender mailSender;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String htmlText) {
        MimeMessage msg = mailSender.createMimeMessage();

        try {
            msg.setFrom(Config.COMPANY_EMAIL.DESC);
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(htmlText, "UTF-8", "html");
            mailSender.send(msg);
        } catch (MessagingException e) {
            throw new HandlableException(ErrorCode.MAIL_SEND_FAIL_ERROR);
        }

    }

}
