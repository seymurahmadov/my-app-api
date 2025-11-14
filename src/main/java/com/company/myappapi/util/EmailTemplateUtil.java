package com.company.myappapi.util;

public class EmailTemplateUtil {

    public static String buildForgotPasswordEmailBody(String email, String newPassword, String url) {
        return String.format("""
                Email hesabınız: %s
                Sizin yeni şifrəniz: %s
                
                Daxil olmaq üçün bu linkə keçid edin: %s
                
                """, email, newPassword, url);
    }

    public static String buildResetPasswordEmailBody(String email, String password, String url) {
        return String.format("""
                Email hesabınız: %s
                Sizin yeni şifrəniz: %s
                
                Daxil olmaq üçün bu linkə keçid edin: %s
                
                """, email, password, url);
    }

    public static String buildSignUpEmailBody(String email, String password, String url) {
        return String.format("""
                Salam ! 
                
                Mail: %s
                Parol: %s
                
                Daxil olmaq üçün bu linkə keçid edin: %s
                
                Təşəkkürlər !
                """, email, password, url);
    }
}