package com.moviePocket.service.email;


import com.moviePocket.service.impl.user.EmailSenderService;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Test
    void testSendMailWithAttachment_success() throws Exception {
        // given
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        emailSenderService.sendMailWithAttachment(
                "test@example.com",
                "<h1>Hello</h1>",
                "Test Subject"
        );

        // then
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(mimeMessage);
    }


    @Test
    void testSendMailWithAttachment_whenExceptionThrown_shouldThrowIllegalStateException() {
        // given
        when(javaMailSender.createMimeMessage())
                .thenThrow(new IllegalStateException("failed to send email"));

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            emailSenderService.sendMailWithAttachment(
                    "test@example.com",
                    "<p>Error</p>",
                    "Fail test"
            );
        });

        assertEquals("failed to send email", exception.getMessage());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender, never()).send((MimeMessage) any());
    }
}

