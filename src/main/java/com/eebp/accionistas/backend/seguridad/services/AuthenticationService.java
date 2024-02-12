package com.eebp.accionistas.backend.seguridad.services;

import com.eebp.accionistas.backend.seguridad.dao.*;
import com.eebp.accionistas.backend.seguridad.exceptions.AuthException;

public interface AuthenticationService {
    JwtAuthenticationResponse signIn(SigninRequest request) throws AuthException;

    JwtAuthenticationResponse signInWithToken(SigninWithTokenRequest token);

    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}
