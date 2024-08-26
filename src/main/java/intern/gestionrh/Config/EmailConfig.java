package intern.gestionrh.Config;

import intern.gestionrh.Services.Impl.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
    @Bean
    public EmailService emailService() {
        return new EmailService();
    }
}
