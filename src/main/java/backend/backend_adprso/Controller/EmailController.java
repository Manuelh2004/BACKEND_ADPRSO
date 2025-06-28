package backend.backend_adprso.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Service.EmailService;
import jakarta.mail.MessagingException;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-test-email")
    public String sendTestEmail() {
        try {
            emailService.sendEmail("test@example.com", "Correo de prueba", "Este es un correo de prueba.");
            return "Correo de prueba enviado exitosamente";
        } catch (MessagingException e) {
            return "Error al enviar el correo: " + e.getMessage();
        }
    }
}