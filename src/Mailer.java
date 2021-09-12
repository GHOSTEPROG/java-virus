import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @author Amirreza Marzban
 * This class is send decryption key to you
 * Using Gmail SMTP protocol
 */

class Mailer {
  private final static String FROM = "******@gmail.com";
  private final static String PASSWORD = "******";

  /**
   * send email
   * @param to distination email
   * @param sub subject
   * @param msg message
   */
  public static void send(String to, String sub, String msg){
    //Get properties object
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    //get Session
    Session session = Session.getDefaultInstance(props,
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(FROM, PASSWORD);
        }
      });
    //compose message
    try {
      MimeMessage message = new MimeMessage(session);
      message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
      message.setSubject(sub);
      message.setText(msg);
      //send message
      Transport.send(message);
    } catch (MessagingException e) {throw new RuntimeException(e);}

  }
}