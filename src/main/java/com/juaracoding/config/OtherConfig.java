package com.juaracoding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:other.properties")
public class OtherConfig {

    private static String flagLogging;
    private static String flagTestDebug;
    private static String flagBcrypt;
    private static String flagMaxCounter;
    private static String flagSMTPActive;
    private static String flagSessionValidation;
    private static String urlEndPointVerify;
    private static String urlPathVerifyEmail;
    private static String pathThymeleafTemplateReport;
    private static String pathSeparatorReport;
    private static String howToDownloadReport;
    private static String pathGeneratePDF;

    // Getter methods for each property
    public static String getFlagLogging() {
        return flagLogging;
    }

    public static String getFlagTestDebug() {
        return flagTestDebug;
    }

    public static String getFlagBcrypt() {
        return flagBcrypt;
    }

    public static String getFlagMaxCounter() {
        return flagMaxCounter;
    }

    public static String getFlagSMTPActive() {
        return flagSMTPActive;
    }

    public static String getFlagSessionValidation() {
        return flagSessionValidation;
    }

    public static String getUrlEndPointVerify() {
        return urlEndPointVerify;
    }

    public static String getUrlPathVerifyEmail() {
        return urlPathVerifyEmail;
    }

    public static String getPathThymeleafTemplateReport() {
        return pathThymeleafTemplateReport;
    }

    public static String getPathSeparatorReport() {
        return pathSeparatorReport;
    }

    public static String getHowToDownloadReport() {
        return howToDownloadReport;
    }

    public static String getPathGeneratePDF() {
        return pathGeneratePDF;
    }

    // Setter methods for each property, annotated with @Value to inject properties from other.properties

    @Value("${flag.logging}")
    private void setFlagLogging(String flagLogging) {
        OtherConfig.flagLogging = flagLogging;
    }

    @Value("${flag.test.debug}")
    private void setFlagTestDebug(String flagTestDebug) {
        OtherConfig.flagTestDebug = flagTestDebug;
    }

    @Value("${flag.bcrypts}")
    private void setFlagBcrypt(String flagBcrypt) {
        OtherConfig.flagBcrypt = flagBcrypt;
    }

    @Value("${flag.max.counter.login}")
    private void setFlagMaxCounter(String flagMaxCounter) {
        OtherConfig.flagMaxCounter = flagMaxCounter;
    }

    @Value("${flag.smtp.active}")
    private void setFlagSMTPActive(String flagSMTPActive) {
        OtherConfig.flagSMTPActive = flagSMTPActive;
    }

    @Value("${flag.session.validation}")
    private void setFlagSessionValidation(String flagSessionValidation) {
        OtherConfig.flagSessionValidation = flagSessionValidation;
    }

    @Value("${url.end.point.verify}")
    private void setUrlEndPointVerify(String urlEndPointVerify) {
        OtherConfig.urlEndPointVerify = urlEndPointVerify;
    }

    @Value("${url.path.verify.email}")
    private void setUrlPathVerifyEmail(String urlPathVerifyEmail) {
        OtherConfig.urlPathVerifyEmail = urlPathVerifyEmail;
    }

    @Value("${path.thymeleaf.template.report}")
    private void setPathThymeleafTemplateReport(String pathThymeleafTemplateReport) {
        OtherConfig.pathThymeleafTemplateReport = pathThymeleafTemplateReport;
    }

    @Value("${path.separator.report}")
    private void setPathSeparatorReport(String pathSeparatorReport) {
        OtherConfig.pathSeparatorReport = pathSeparatorReport;
    }

    @Value("${how.to.download.report}")
    private void setHowToDownloadReport(String howToDownloadReport) {
        OtherConfig.howToDownloadReport = howToDownloadReport;
    }

    @Value("${path.generate.pdf}")
    private void setPathGeneratePDF(String pathGeneratePDF) {
        OtherConfig.pathGeneratePDF = pathGeneratePDF;
    }
}
