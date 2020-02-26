package com.itechro.iaml.model.ctr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportDTO implements Serializable {

    private Integer rEntityId;

    private String submissionCode;

    private String reportCode;

    private Date submissionDate;

    private String currencyCodeLocal;

    private TransactionDTO transactionDTO;

    private List<TransactionDTO> transactionDTOList;

    private FromToMappingDTO transactionFrom;

    private FromToMappingDTO transactionTo;

    public FromToMappingDTO getTransactionTo() {
        return transactionTo;
    }

    public void setTransactionTo(FromToMappingDTO transactionTo) {
        this.transactionTo = transactionTo;
    }


    public FromToMappingDTO getTransactionFrom() {
        return transactionFrom;
    }

    public void setTransactionFrom(FromToMappingDTO transactionFrom) {
        this.transactionFrom = transactionFrom;
    }

    public Integer getrEntityId() {
        return rEntityId;
    }

    public void setrEntityId(Integer rEntityId) {
        this.rEntityId = rEntityId;
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

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getCurrencyCodeLocal() {
        return currencyCodeLocal;
    }

    public void setCurrencyCodeLocal(String currencyCodeLocal) {
        this.currencyCodeLocal = currencyCodeLocal;
    }

    public TransactionDTO getTransactionDTO() {
        return transactionDTO;
    }

    public void setTransactionDTO(TransactionDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
    }

    public List<TransactionDTO> getTransactionDTOList() {
        if (transactionDTOList == null) {
            transactionDTOList = new ArrayList<>();
        }
        return transactionDTOList;
    }

    public void setTransactionDTOList(List<TransactionDTO> transactionDTOList) {
        this.transactionDTOList = transactionDTOList;
    }
}
