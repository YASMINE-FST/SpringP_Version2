package tn.fst.springproject.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; // ✅ Sans static

    public void sendResetCode(String to, String code) { // ✅ Sans static
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Réinitialisation de mot de passe");
        message.setText("Votre code de réinitialisation est : " + code + "\nIl expire dans 10 minutes.");
        mailSender.send(message);
    }
}
