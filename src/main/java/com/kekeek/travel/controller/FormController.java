package com.kekeek.travel.controller;

import com.kekeek.travel.config.SiteConfig;
import com.kekeek.travel.model.ContactForm;
import com.kekeek.travel.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FormController {

    private ContactService contactService;

    @Autowired
    public FormController(ContactService contactService, SiteConfig siteConfig) {
        this.contactService = contactService;
    }

    @GetMapping({"/contact"})
    public String getContactPage(Model model) {
        contactService.setContactFormPageAttributes(model, new ContactForm());

        return "contact";
    }

    @PostMapping({"/contact"})
    public String postContactPage(ContactForm contactFormData,  Model model) {
        contactService.setContactFormPageAttributes(model, contactFormData);
        contactService.processContactForm(contactFormData);

        return "contact";
    }
}