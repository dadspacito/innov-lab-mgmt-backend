package util;

import jakarta.annotation.Resource;
import jakarta.mail.*;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


import java.util.Properties;
/**
 * Utility class for sending emails using Jakarta Mail.
 */
public class EmailSender {
    private static final String username = "pedroodev77@gmail.com";
    private static final String password = "bxib kcws nlag bggd";
    @Resource(name = "mail/session")
    private static Session session;
    /**
     * Sends an email with the specified recipient, subject, and content.
     *
     * @param to      the recipient email address
     * @param subject the subject of the email
     * @param content the content of the email
     */
    public static void sendEmail(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        System.out.println("Sending email to " + to + " with subject " + subject + " and content " + content);
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8"); // Set the email content to HTML
            try {
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com", 587, username, password);
                transport.sendMessage(message, message.getAllRecipients());
                System.out.println("Email sent");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Sends a verification email to the specified recipient.
     *
     * @param to               the recipient email address
     * @param firstName        the first name of the recipient
     * @param verificationLink the link for email verification
     */
    public static void sendVerificationEmail(String to, String firstName, String verificationLink) {
        String subject = "Welcome to Critical Software's InnovLab - Verify Your Account";
        String content = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;}"
                + ".container {width: 80%; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}"
                + ".header {background-color: #9b1118; padding: 10px 0; text-align: center; color: #ffffff;}"
                + ".content {line-height: 1.6; color: #333333; padding: 20px;}"
                + ".content h1 {color: #333333;}"
                + ".button {text-align: center; margin: 20px 0;}"
                + ".button a {background-color: #28a745; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;}"
                + ".footer {text-align: center; color: #888888; font-size: 12px; padding: 10px 0;}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h2>Critical Software's InnovLab</h2>"
                + "</div>"
                + "<div class='content'>"
                + "<h1>Welcome, " + firstName + "!</h1>"
                + "<p>We are thrilled to have you on board at Critical Software's InnovLab!</p>"
                + "<p>Thank you for registering an account with us. To complete your registration, please verify your email address by clicking the button below:</p>"
                + "<div class='button'><a href='" + verificationLink + "'>Verify Account</a></div>"
                + "<p>This link is valid for 1 hour.</p>"
                + "<p>If you did not register for this account, please disregard this email.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>&copy; 2024 Critical Software. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        sendEmail(to, subject, content);
    }
    /**
     * Sends a password reset email to the specified recipient.
     *
     * @param to        the recipient email address
     * @param firstName the first name of the recipient
     * @param resetLink the link for password reset
     */
    public static void sendPasswordResetEmail(String to, String firstName, String resetLink) {
        String subject = "Password Reset Request - Critical Software's InnovLab";
        String content = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;}"
                + ".container {width: 80%; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}"
                + ".header {background-color: #9b1118; padding: 20px; text-align: center; color: #ffffff;}"
                + ".header h1 {margin: 0;}"
                + ".content {line-height: 1.6; color: #333333; padding: 20px;}"
                + ".content h1 {color: #333333;}"
                + ".button {text-align: center; margin: 20px 0;}"
                + ".button a {background-color: #28a745; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;}"
                + ".footer {text-align: center; color: #888888; font-size: 12px; padding: 10px 0;}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h1>Critical Software's InnovLab</h1>"
                + "</div>"
                + "<div class='content'>"
                + "<h1>Hello, " + firstName + "!</h1>"
                + "<p>We received a request to reset your password for your Critical Software's InnovLab account.</p>"
                + "<p>To reset your password, click the button below:</p>"
                + "<div class='button'><a href='" + resetLink + "'>Reset Password</a></div>"
                + "<p>This link is valid for 1 hour.</p>"
                + "<p>If you did not request a password reset, please ignore this email or contact support if you have any questions.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>&copy; 2024 Critical Software. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        sendEmail(to, subject, content);
    }
}
