package com.itechro.iaml.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
}
