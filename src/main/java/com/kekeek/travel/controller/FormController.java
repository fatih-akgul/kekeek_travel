package com.kekeek.travel.controller;

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
    public FormController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping({"/contact"})
    public String getContactPage(Model model) {
        model.addAllAttributes(contactService.getContactFormPageAttributes(new ContactForm()));

        return "contact";
    }

    @PostMapping({"/contact"})
    public String postContactPage(ContactForm contactFormData,  Model model) {
        model.addAllAttributes(contactService.getContactFormPageAttributes(contactFormData));
        contactService.processContactForm(contactFormData);

        return "contact";
    }
}
