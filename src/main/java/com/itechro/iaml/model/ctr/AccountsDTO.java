package com.itechro.iaml.model.ctr;

import generated.TAccountMyClient;

import java.util.Date;
import java.util.List;

public class AccountsDTO {

    private String AcctNumber;

    private String CifId;

    private String InstituationName;

    private String InstitutionCode;

    private String Swift;

    private String Branch;

    private String Account;

    private String CurrencyCode;

    private String AccountName;

    private String PersonalAccountType;

    private String StatusCode;

    private String Beneficiary;

    private String BeneficiaryComment;

    private String Comments;

    private Date Opened;

    private Date Closed;

    private PersonDTO personDTO;

    private EntitiesDTO entitiesDTO;

    private List<PhoneDTO> phoneDTO;

    private List<AddressDTO> addressDTO;

    private List<RelatedPartyDTO> relatedPartyDTO;

    public PersonDTO getPersonDTO() {
        return personDTO;
    }

    public void setPersonDTO(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    public EntitiesDTO getEntitiesDTO() {
        return entitiesDTO;
    }

    public void setEntitiesDTO(EntitiesDTO entitiesDTO) {
        this.entitiesDTO = entitiesDTO;
    }

    public List<RelatedPartyDTO> getRelatedPartyDTO() {
        return relatedPartyDTO;
    }

    public void setRelatedPartyDTO(List<RelatedPartyDTO> relatedPartyDTO) {
        this.relatedPartyDTO = relatedPartyDTO;
    }

    public List<PhoneDTO> getPhoneDTO() {
        return phoneDTO;
    }

    public void setPhoneDTO(List<PhoneDTO> phoneDTO) {
        this.phoneDTO = phoneDTO;
    }

    public List<AddressDTO> getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(List<AddressDTO> addressDTO) {
        this.addressDTO = addressDTO;
    }

    public String getAcctNumber() {
        return AcctNumber;
    }

    public void setAcctNumber(String acctNumber) {
        if(acctNumber!=null){
            acctNumber = acctNumber.trim();
        }
        AcctNumber=acctNumber;
    }

    public String getCifId() {
        return CifId;
    }

    public void setCifId(String cifId) {
        if(cifId!=null){
            cifId = cifId.trim();
        }
        CifId=cifId;
    }

    public String getInstituationName() {
        return InstituationName;
    }

    public void setInstituationName(String instituationName) {
        InstituationName = instituationName;
    }

    public String getInstitutionCode() {
        return InstitutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        InstitutionCode = institutionCode;
    }

    public String getSwift() {
        return Swift;
    }

    public void setSwift(String swift) {
        Swift = swift;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getPersonalAccountType() {
        return PersonalAccountType;
    }

    public void setPersonalAccountType(String personalAccountType) {
        PersonalAccountType = personalAccountType;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getBeneficiary() {
        return Beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        Beneficiary = beneficiary;
    }

    public String getBeneficiaryComment() {
        return BeneficiaryComment;
    }

    public void setBeneficiaryComment(String beneficiaryComment) {
        BeneficiaryComment = beneficiaryComment;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public Date getOpened() {
        return Opened;
    }

    public void setOpened(Date opened) {
        Opened = opened;
    }

    public Date getClosed() {
        return Closed;
    }

    public void setClosed(Date closed) {
        Closed = closed;
    }
}
