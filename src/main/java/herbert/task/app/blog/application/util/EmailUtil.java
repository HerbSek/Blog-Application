/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.util;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HerbertSekpey
 */

@Singleton
public class EmailUtil {
    
    @Inject
    @ConfigProperty(name = "mail.smtp.host")
    private String smtpHost;

    @Inject
    @ConfigProperty(name = "mail.smtp.port")
    private String smtpPort;

    @Inject
    @ConfigProperty(name = "mail.smtp.user")
    private String smtpUser ;

    @Inject
    @ConfigProperty(name = "mail.smtp.password")
    private String smtpPassword;
  
    
   @Asynchronous
public void sendSmtpEmail(String recipient, String subject, String body) throws MessagingException {
    
    // 1. Guard against Null / Empty values that crash InternetAddress.parse()
    if (recipient == null || recipient.trim().isEmpty()) {
        Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE, "Aborting email dispatch: Recipient address is null or empty.");
        return; 
    }
    
    
    // 2. Setup SMTP Mail configuration Properties
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.port", smtpPort);
    props.put("mail.smtp.ssl.trust", smtpHost);

    // 3. Clean Lambda Expression replacing old Anonymous Authenticator syntax
    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUser, smtpPassword);
        }
    });
    
    // Alternatively, if using modern environments, the lambda looks like:
    // Session session = Session.getInstance(props, () -> new PasswordAuthentication(smtpUser, smtpPassword));

    try {
        // 4. Construct the Mime Message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUser, "Blog-App-Test"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=UTF-8");
        
        // 5. Send message out
        Transport.send(message);
        
    } catch (Exception e) {
        // Keep tracking stack traces using standard Java logging frameworks inside enterprise beans
        Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE, "SMTP Mail failure encountered: ", e);
    }
}
    

    
}
