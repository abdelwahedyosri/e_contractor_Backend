package group_6.e_contractor_backend.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
       // mailSender.setUsername("abdelwahedyosri28880533@gmail.com");
        // mailSender.setPassword("ccozgyblmfprgxrr");

        // spring.mail.username=abdelwahedyosri28880533@gmail.com
        // spring.mail.password=ccozgyblmfprgxrr
        mailSender.setUsername("econtractar@gmail.com");
        mailSender.setPassword("dwkcbachkyodudvy");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
