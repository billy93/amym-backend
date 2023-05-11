package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.SystemParameter;
import com.atibusinessgroup.amanyaman.domain.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

// import com.atibusinessgroup.amanyaman.module.master.systemParameter.dto.SystemParameterDTO;
// import io.github.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private static final String CLAIM_BASE_URL = "claimBaseUrl";


    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final SystemParameterService systemParameterService;

    @Value("${app.mail.base-url}")
    private String mailBaseUrl;

    @Value("${app.mail.from}")
    private String from;

    public MailService(JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine, SystemParameterService systemParameterService) {

        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.systemParameterService = systemParameterService;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        }  catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);

        if(templateName.contentEquals("mail/passwordResetClaimEmail")){
            context.setVariable(BASE_URL, mailBaseUrl);
        }
        else {
            context.setVariable(BASE_URL, mailBaseUrl);
        }
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendPasswordResetClaimMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetClaimEmail", "email.reset.title");
    }

    @Async
    public void sendFinishPasswordReset(User user) {
        Optional<SystemParameter> systemParameterOptional = systemParameterService.findOneByName("USER_SETUP_PASSWORD_EMAIL");
        if(systemParameterOptional.isPresent()) {
            SystemParameter systemParameter = systemParameterOptional.get();
            if(systemParameter.getValue() != null && !systemParameter.getValue().contentEquals("")) {
                String[] emails = systemParameter.getValue().split(",");
                for(String email: emails) {
                    Locale locale = Locale.forLanguageTag(user.getLangKey());
                    Context context = new Context(locale);
                    context.setVariable(USER, user);
                    context.setVariable(BASE_URL, mailBaseUrl);
                    String content = templateEngine.process("mail/finishPasswordResetEmail", context);
                    String subject = messageSource.getMessage("email.finish.title", null, locale);
                    sendEmail(email.toLowerCase(Locale.ROOT), subject, content, false, true);
                }
            }
        }
    }
}
