package com.itechro.iaml.model.ctr;

import java.util.Date;

public class TransactionDTO {

    private Integer tran_uid;

    private String transactionNumber;

    private String internalRefNumber;

    private String transactionLocation;

    private String transactionDescription;

    private Date dateTransaction;

    private Date valueDate;

    private String transModeCode;

    private String transModeComment;

    private Integer amt;

    private String transCurrencyCode;

    private Integer rate;

    private Integer amountLocal;

    public Integer getTran_uid() {
        return tran_uid;
    }

    public void setTran_uid(Integer tran_uid) {
        this.tran_uid = tran_uid;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getInternalRefNumber() {
        return internalRefNumber;
    }

    public void setInternalRefNumber(String internalRefNumber) {
        this.internalRefNumber = internalRefNumber;
    }

    public String getTransactionLocation() {
        return transactionLocation;
    }

    public void setTransactionLocation(String transactionLocation) {
        this.transactionLocation = transactionLocation;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Date getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Date dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getTransModeCode() {
        return transModeCode;
    }

    public void setTransModeCode(String transModeCode) {
        this.transModeCode = transModeCode;
    }

    public String getTransModeComment() {
        return transModeComment;
    }

    public void setTransModeComment(String transModeComment) {
        this.transModeComment = transModeComment;
    }

    public Integer getAmt() {
        return amt;
    }

    public void setAmt(Integer amt) {
        this.amt = amt;
    }

    public String getTransCurrencyCode() {
        return transCurrencyCode;
    }

    public void setTransCurrencyCode(String transCurrencyCode) {
        this.transCurrencyCode = transCurrencyCode;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getAmountLocal() {
        return amountLocal;
    }

    public void setAmountLocal(Integer amountLocal) {
        this.amountLocal = amountLocal;
    }

}
