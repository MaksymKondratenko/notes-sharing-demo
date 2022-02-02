package org.mk.notessharedemo.note.application.adapters.outbound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mk.notessharedemo.note.application.NotificationProps;
import org.mk.notessharedemo.note.application.ports.outbound.NoteSharedNotificationService;
import org.mk.notessharedemo.note.domain.model.SharedNoteMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Component
public class EmailNoteSharedNotificationService implements NoteSharedNotificationService {
    private final NotificationProps.EmailProps emailProps;

    public EmailNoteSharedNotificationService(NotificationProps props) {
        emailProps = props.getEmail();
    }

    public void sendNotification(SharedNoteMetadata meta) {
        String recipientAddress = meta.getRecipientEmail();
        if (Objects.isNull(recipientAddress)) {
            log.warn("Recipient email is not specified. A notification won't be sent.");
            return;
        }
        MimeMessage message = new MimeMessage(Session.getDefaultInstance(mailAttributes()));
        try {
            message.setFrom(new InternetAddress(emailProps.getServiceAddress()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
            message.setSubject("Note shared notification");
            message.setText(String.format("Hello,\n\nThere is a note shared with you:\n\tId: %s\n\tPassword: %s",
                    meta.getId(), meta.getAssociatedKey()));
            Transport.send(message);
            log.debug("Event notification for note with id: {} emailed successfully.", meta.getId());
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private Properties mailAttributes() {
        Properties attributes = System.getProperties();
        attributes.setProperty("mail.smtp.host", emailProps.getHostname());
        attributes.setProperty("mail.smtp.port", emailProps.getPort());
        return attributes;
    }
}
