package com.eebp.accionistas.backend.seguridad.services;

import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
}
