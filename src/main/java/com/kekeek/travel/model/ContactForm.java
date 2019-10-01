package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.util.StringUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactForm extends BaseForm {
    private String name = "";
    private String email = "";
    private String subject = "";
    private String message = "";

    @Override
    public boolean validate() {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email) || StringUtils.isEmpty(subject) || StringUtils.isEmpty(message)) {
            addError("All fields are required");
        }

        if (!StringUtils.isEmpty(email)) {
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.isValid(email, null)) {
                addError("Invalid email address: " + email);
            }
        }

        return getErrors().isEmpty();
    }

    @Override
    public void clearFields() {
        name = "";
        email = "";
        subject = "";
        message = "";
    }
}
