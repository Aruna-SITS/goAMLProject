package com.itechro.iaml.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IAMLProperties {

    @Value("${public.REntity.Id}")
    public Integer publicREntityId;

    @Value("${public.submission.code}")
    public String submissionCode;

    @Value("${public.report.code}")
    public String reportCode;

    @Value("${public.submission.date}")
    public String submissionDate;

    @Value("${public.currency.local.code}")
    public String currencyLocalCode;

    @Value("${public.to.non.client}")
    public String toNonMyClient;

    @Value("${public.to.my.client}")
    public String toMyClient;

    @Value("${public.from.non.client}")
    public String fromNonClient;

    @Value("${public.from.my.client}")
    public String fromMyClient;

    @Value("${public.number.of.records.to.load}")
    public Integer numberOfRecordsToLoad;

    @Value("${public.output.xml.file.name}")
    public String outputXmlFileName;

    @Value("${public.entity.reference}")
    public String entityReference;

    @Value("${public.error.log.file.name}")
    public String errorLogFileName;

    @Value("${public.warning.log.file.name}")
    public String warningLogFileName;

    @Value("${public.success.log.file.name}")
    public String successLogFileName;

    @Value("${public.error.log.file.path}")
    public String errorLogFilePath;

    @Value("${public.warning.log.file.path}")
    public String warningLogFilePath;

    @Value("${public.success.log.file.path}")
    public String successLogFilePath;

    @Value("${public.output.xml.file.path}")
    public String outputXmlFilePath;

    @Value("${public.report.indicator}")
    public String reportIndicator;

    @Value("${public.trans.mode,code.validator}")
    public String transModeCodeValidator;

    public String getToNonMyClient() {
        return toNonMyClient;
    }

    public void setToNonMyClient(String toNonMyClient) {
        this.toNonMyClient = toNonMyClient;
    }

    public String getToMyClient() {
        return toMyClient;
    }

    public void setToMyClient(String toMyClient) {
        this.toMyClient = toMyClient;
    }

    public String getFromNonClient() {
        return fromNonClient;
    }

    public void setFromNonClient(String fromNonClient) {
        this.fromNonClient = fromNonClient;
    }

    public String getFromMyClient() {
        return fromMyClient;
    }

    public void setFromMyClient(String fromMyClient) {
        this.fromMyClient = fromMyClient;
    }

    public Integer getPublicREntityId() {
        return publicREntityId;
    }

    public void setPublicREntityId(Integer publicREntityId) {
        this.publicREntityId = publicREntityId;
    }

    public String getSubmissionCode() {
        return submissionCode;
    }

    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getCurrencyLocalCode() {
        return currencyLocalCode;
    }

    public void setCurrencyLocalCode(String currencyLocalCode) {
        this.currencyLocalCode = currencyLocalCode;
    }

    public Integer getNumberOfRecordsToLoad() {
        return numberOfRecordsToLoad;
    }

    public void setNumberOfRecordsToLoad(Integer numberOfRecordsToLoad) {
        this.numberOfRecordsToLoad = numberOfRecordsToLoad;
    }

    public String getOutputXmlFileName() {
        return outputXmlFileName;
    }

    public void setOutputXmlFileName(String outputXmlFileName) {
        this.outputXmlFileName = outputXmlFileName;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public String getErrorLogFileName() {
        return errorLogFileName;
    }

    public void setErrorLogFileName(String errorLogFileName) {
        this.errorLogFileName = errorLogFileName;
    }

    public String getWarningLogFileName() {
        return warningLogFileName;
    }

    public void setWarningLogFileName(String warningLogFileName) {
        this.warningLogFileName = warningLogFileName;
    }

    public String getSuccessLogFileName() {
        return successLogFileName;
    }

    public void setSuccessLogFileName(String successLogFileName) {
        this.successLogFileName = successLogFileName;
    }

    public String getErrorLogFilePath() {
        return errorLogFilePath;
    }

    public void setErrorLogFilePath(String errorLogFilePath) {
        this.errorLogFilePath = errorLogFilePath;
    }

    public String getWarningLogFilePath() {
        return warningLogFilePath;
    }

    public void setWarningLogFilePath(String warningLogFilePath) {
        this.warningLogFilePath = warningLogFilePath;
    }

    public String getSuccessLogFilePath() {
        return successLogFilePath;
    }

    public void setSuccessLogFilePath(String successLogFilePath) {
        this.successLogFilePath = successLogFilePath;
    }

    public String getOutputXmlFilePath() {
        return outputXmlFilePath;
    }

    public void setOutputXmlFilePath(String outputXmlFilePath) {
        this.outputXmlFilePath = outputXmlFilePath;
    }

    public String getReportIndicator() {
        return reportIndicator;
    }

    public void setReportIndicator(String reportIndicator) {
        this.reportIndicator = reportIndicator;
    }

    public String getTransModeCodeValidator() {
        return transModeCodeValidator;
    }

    public void setTransModeCodeValidator(String transModeCodeValidator) {
        this.transModeCodeValidator = transModeCodeValidator;
    }
}
