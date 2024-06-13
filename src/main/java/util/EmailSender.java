package util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


import java.util.Properties;

public class EmailSender {

    private static final String username = "MS_ZkkD6n@trial-pq3enl6079ml2vwr.mlsender.net";
    private static final String password = "SveXcazMDlGxN83r";

    public static void sendEmail(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mailersend.net");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");


        System.out.println("Sending email to " + to + " with subject " + subject + " and content " + content);

        Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password);
            }
        });

        try {
            System.out.println("Sending email...");
            Message message = new MimeMessage(session);
            System.out.println("Message created");
            message.setFrom(new InternetAddress(username));
            System.out.println("From set");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            System.out.println("Recipients set");
            message.setSubject(subject);
            System.out.println("Subject set");
            message.setContent(content, "text/html; charset=utf-8"); // Set the email content to HTML
            System.out.println("Content set");

            try {
                Transport.send(message);
                System.out.println("Email sent");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
/*

    COMENTÁRIO // VERSÃO SEM FORMATAÇÃO HTML

    public static void sendVerificationEmail(String to, String userName, String verificationLink) {
        String subject = "Account Verification - InnovLab Management";
        String content = "<h1>Hello, " + userName + "!</h1>"
                +"<p>Thank you for registering an account with us!</p>"
                + "<p>To verify your account, click the link below:</p>"
                + "<p><a href=\"" + verificationLink + "\">Verify Account</a></p>"
                + "<p>This link is valid for 1 hour.</p>"
                + "<p>If you didn't register, please ignore this email.</p>";

        sendEmail(to, subject, content);
    }


    public static void sendPasswordResetEmail(String to, String userName, String resetLink) {
        String subject = "Password Reset";
        String content = "<h1>Hello, " + userName + "!</h1>"
                + "<p>To reset your password, click the link below:</p>"
                + "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
                + "<p>This link is valid for 1 hour.</p>"
                + "<p>If you didn't request a password reset, please ignore this email.</p>";

        sendEmail(to, subject, content);
    }*/

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