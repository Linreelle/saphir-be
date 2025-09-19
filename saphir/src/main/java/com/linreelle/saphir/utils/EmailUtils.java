package com.linreelle.saphir.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n" +
                getVerificationUrl(host, token) + "\n\nThe support Team";
    }

    public static String getVerificationUrl(String host, String token) {
    if (!host.endsWith("/#")) {
        host = host.endsWith("/") ? host + "#" : host + "/#";
    }
    return host + "/verification?token=" + token;
    }

    public static String adhesionMail (String name) {
        return "Dear " + name + ", " +
                "\n\nYour adhesion request has been successfully. " +
                "\n Our customer support team " +
                "will get in touch within 24 hours to finalize your subscription. " +
                "\n Thank you for choosing Saphir ASBL";
    }

}
