package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Email extends EntityModel {
    private String fromEmail;
    private String fromName;
    private String toEmail;
    private String senderEmail;
    private String senderName;
    private String senderIp;
    private String subject;
    private String message;
    private Boolean success = Boolean.FALSE;
}
