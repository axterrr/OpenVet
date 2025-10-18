package ua.edu.ukma.objectanalysis.openvet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailService Tests")
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private static final String SENDER_EMAIL = "noreply@openvet.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "sender", SENDER_EMAIL);
    }

    // ===== SEND EMAIL Tests =====

    @Test
    @DisplayName("Should send email successfully")
    void testSendEmail_Success() {
        // Arrange
        String to = "recipient@test.com";
        String subject = "Test Subject";
        String text = "Test email content";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(to, subject, text);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send email with correct parameters")
    void testSendEmail_VerifyParameters() {
        // Arrange
        String to = "recipient@test.com";
        String subject = "Appointment Reminder";
        String text = "Your appointment is tomorrow at 10:00 AM";

        doAnswer(invocation -> {
            SimpleMailMessage message = invocation.getArgument(0);
            assertEquals(to, message.getTo()[0]);
            assertEquals(subject, message.getSubject());
            assertEquals(text, message.getText());
            assertEquals(SENDER_EMAIL, message.getFrom());
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(to, subject, text);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should handle email sending failure")
    void testSendEmail_Failure() {
        // Arrange
        String to = "invalid@test.com";
        String subject = "Test";
        String text = "Test";

        doThrow(new RuntimeException("Mail server unavailable"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> emailService.sendEmail(to, subject, text));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send email with empty subject")
    void testSendEmail_EmptySubject() {
        // Arrange
        String to = "recipient@test.com";
        String subject = "";
        String text = "Test content";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(to, subject, text);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send email with empty text")
    void testSendEmail_EmptyText() {
        // Arrange
        String to = "recipient@test.com";
        String subject = "Test";
        String text = "";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(to, subject, text);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send email to multiple recipients format")
    void testSendEmail_LongContent() {
        // Arrange
        String to = "recipient@test.com";
        String subject = "Long Subject Test";
        String text = "This is a very long email content that might contain multiple paragraphs " +
                "and various information about appointments, medical records, and other details.";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(to, subject, text);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}