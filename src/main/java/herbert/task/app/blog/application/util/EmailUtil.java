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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

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
    private String smtpUser;

    @Inject
    @ConfigProperty(name = "mail.smtp.password")
    private String smtpPassword;

    @Asynchronous
    public void sendVerificationEmail(String recipient, String tokenUrl) throws MessagingException {
        String body = buildBody(tokenUrl, recipient,
                "Verify your email address",
                "Click the button below to confirm your address and activate your account. "
                + "This link is single-use and expires in <strong>5 minutes</strong>.",
                "Verify email address");
        dispatch(recipient, "Verify your BlogApp email", body);
    }

    @Asynchronous
    public void sendLoginEmail(String recipient, String tokenUrl) throws MessagingException {
        String body = buildBody(tokenUrl, recipient,
                "Your sign-in link",
                "Click the button below to sign in to your account. "
                + "This link is single-use and expires in <strong>5 minutes</strong>.",
                "Sign in to BlogApp");
        dispatch(recipient, "Your BlogApp sign-in link", body);
    }

    private String buildBody(String tokenUrl, String recipient,
                             String heading, String subtext, String ctaText) {
        try (InputStream is = getClass().getResourceAsStream("/templates/email-verify.html")) {
            if (is == null) {
                Logger.getLogger(EmailUtil.class.getName())
                        .log(Level.WARNING, "Email template not found, falling back to plain text.");
                return tokenUrl;
            }
            String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return template
                    .replace("{{HEADING}}", heading)
                    .replace("{{SUBTEXT}}", subtext)
                    .replace("{{CTA_TEXT}}", ctaText)
                    .replace("{{TOKEN_URL}}", tokenUrl)
                    .replace("{{RECIPIENT_EMAIL}}", recipient);
        } catch (IOException e) {
            Logger.getLogger(EmailUtil.class.getName())
                    .log(Level.SEVERE, "Failed to load email template: ", e);
            return tokenUrl;
        }
    }

    private void dispatch(String recipient, String subject, String body) {
        if (recipient == null || recipient.trim().isEmpty()) {
            Logger.getLogger(EmailUtil.class.getName())
                    .log(Level.SEVERE, "Aborting email dispatch: recipient is null or empty.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.trust", smtpHost);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser, "BlogApp"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            Logger.getLogger(EmailUtil.class.getName())
                    .log(Level.SEVERE, "SMTP Mail failure: ", e);
        }
    }
}
