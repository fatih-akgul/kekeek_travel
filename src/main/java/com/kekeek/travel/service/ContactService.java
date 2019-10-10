package com.kekeek.travel.service;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.config.EmailConfig;
import com.kekeek.travel.config.SiteConfig;
import com.kekeek.travel.model.ContactForm;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Service
@CacheConfig(cacheNames={"pages"})
public class ContactService extends BaseService {
    private EmailConfig emailConfig;
    private HttpServletRequest request;

    @Autowired
    public ContactService(RestTemplate restTemplate, SiteConfig siteConfig, EmailConfig emailConfig, ApiConfig apiConfig, HttpServletRequest request) {
        this.siteConfig = siteConfig;
        this.emailConfig = emailConfig;
        this.request = request;
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
    }

    public void processContactForm(ContactForm contactForm) {
        if (contactForm.validate()) {
            Mail mail = constructMail(contactForm);

            String errorMessage = "Message could not be sent. Please try again later.";
            try {
                Response response = sendEmail(mail);

                if (response.getStatusCode() < 200 || response.getStatusCode() > 299) {
                    contactForm.addError(errorMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
                contactForm.addError(errorMessage);
            }

            saveEmailRequest(mail, contactForm);

            if (contactForm.getErrors().isEmpty()) {
                contactForm.clearFields();

                contactForm.addMessage("Thank you, your message has been sent.");
            }
        }
    }

    private Mail constructMail(ContactForm contactForm) {
        Email from = new Email(emailConfig.getFromAddress(), emailConfig.getFromName());
        Email to = new Email(emailConfig.getToAddress());
        String subject = siteConfig.getSiteName() + ": " + contactForm.getSubject();
        String message = contactForm.getMessage() + "\n\nFROM: " + contactForm.getName() + " (" + contactForm.getEmail() + ")";
        message = message.replace("\n", "\n\n");
        Content content = new Content("text/plain", message);

        return new Mail(from, subject, to, content);
    }

    private Response sendEmail(Mail mail) throws IOException {
        SendGrid sendGrid = new SendGrid(emailConfig.getSendGridApiKey());
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        return sendGrid.api(request);
    }

    private void saveEmailRequest(Mail mail, ContactForm contactForm) {
        var email = new com.kekeek.travel.model.Email();
        email.setFromEmail(mail.getFrom().getEmail());
        email.setFromName(mail.getFrom().getName());
        email.setMessage(contactForm.getMessage());
        email.setSenderEmail(contactForm.getEmail());
        email.setSenderIp(getClientIp());
        email.setSenderName(contactForm.getName());
        email.setSubject(contactForm.getSubject());
        email.setToEmail(mail.getPersonalization().get(0).getTos().get(0).getEmail());
        email.setSuccess(contactForm.getErrors().isEmpty());
        try {
            restTemplate.postForObject(apiConfig.getUrlEmails(), getRequestForPost(email), email.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getContactFormPageAttributes(ContactForm contactFormData) {
        Map<String, Object> pageData = getContactPageMetaFields();
        pageData.put("contactFormData", contactFormData);

        return pageData;
    }

    @Cacheable(key = "\"contact\"")
    public Map<String, Object> getContactPageMetaFields() {
        return getMetaFields("contact");
    }

    private String getClientIp() {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
