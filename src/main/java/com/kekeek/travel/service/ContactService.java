package com.kekeek.travel.service;

import com.kekeek.travel.config.EmailConfig;
import com.kekeek.travel.config.SiteConfig;
import com.kekeek.travel.model.ContactForm;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;

@Service
public class ContactService {

    private SiteConfig siteConfig;
    private EmailConfig emailConfig;

    @Autowired
    public ContactService(SiteConfig siteConfig, EmailConfig emailConfig) {
        this.siteConfig = siteConfig;
        this.emailConfig = emailConfig;
    }

    public void processContactForm(ContactForm contactForm) {
        if (contactForm.validate()) {
            Email from = new Email(emailConfig.getFromAddress(), emailConfig.getFromName());
            Email to = new Email(emailConfig.getToAddress());
            String subject = siteConfig.getSiteName() + ": " + contactForm.getSubject();
            String message = contactForm.getMessage() + "\n\nFROM: " + contactForm.getName() + " (" + contactForm.getEmail() + ")";
            message = message.replace("\n", "\n\n");
            Content content = new Content("text/plain", message);

            Mail mail = new Mail(from, subject, to, content);

            SendGrid sendGrid = new SendGrid(emailConfig.getSendGridApiKey());
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                Response response = sendGrid.api(request);

                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            } catch (IOException e) {
                e.printStackTrace();
                contactForm.addError("Message could not be sent. Please try again later.");
            }

            if (contactForm.getErrors().isEmpty()) {
                contactForm.clearFields();

                contactForm.addMessage("Thank you, your message has been sent.");
            }
        }
    }

    public void setContactFormPageAttributes(Model model, ContactForm contactFormData) {
        String title = "Contact Us - " + siteConfig.getSiteName();
        String siteName = siteConfig.getCountry();

        model.addAttribute("pageTitle", title);
        model.addAttribute("keywords", String.join(", ", siteName, "contact"));
        model.addAttribute("description", title);

        model.addAttribute("contactFormData", contactFormData);
    }
}
