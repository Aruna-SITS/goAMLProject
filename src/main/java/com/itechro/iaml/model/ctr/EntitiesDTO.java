package com.itechro.iaml.model.ctr;

import java.util.Date;
import java.util.List;

public class EntitiesDTO {

    private String name;

    private String commercialName;

    private String incorporationLegalForm;

    private String incorporationNumber;

    private String business;

    private String email;

    private String incorporationCountryCode;

    private Date incorporationDate;

    private String businessClosed;

    private Date dateBusinessClosed;

    private String taxNumber;

    private String taxRegNumber;

    private String comments;

    private String cifId;

    private String corpId;

    private List<PhoneDTO> phoneDTO;

    private List<AddressDTO> addressDTO;
    
    //New line added
    private List<PersonDirectorDTO> personDirectors;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getIncorporationLegalForm() {
        return incorporationLegalForm;
    }

    public void setIncorporationLegalForm(String incorporationLegalForm) {
        this.incorporationLegalForm = incorporationLegalForm;
    }

    public String getIncorporationNumber() {
        return incorporationNumber;
    }

    public void setIncorporationNumber(String incorporationNumber) {
        this.incorporationNumber = incorporationNumber;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIncorporationCountryCode() {
        return incorporationCountryCode;
    }

    public void setIncorporationCountryCode(String incorporationCountryCode) {
        this.incorporationCountryCode = incorporationCountryCode;
    }

    public Date getIncorporationDate() {
        return incorporationDate;
    }

    public void setIncorporationDate(Date incorporationDate) {
        this.incorporationDate = incorporationDate;
    }

    public String getBusinessClosed() {
        return businessClosed;
    }

    public void setBusinessClosed(String businessClosed) {
        this.businessClosed = businessClosed;
    }

    public Date getDateBusinessClosed() {
        return dateBusinessClosed;
    }

    public void setDateBusinessClosed(Date dateBusinessClosed) {
        this.dateBusinessClosed = dateBusinessClosed;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTaxRegNumber() {
        return taxRegNumber;
    }

    public void setTaxRegNumber(String taxRegNumber) {
        this.taxRegNumber = taxRegNumber;
    }

    public List<PersonDirectorDTO> getPersonDirectors() {
		return personDirectors;
	}

	public void setPersonDirectors(List<PersonDirectorDTO> personDirectors) {
		this.personDirectors = personDirectors;
	}

	public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCifId() {
        return cifId;
    }

    public void setCifId(String cifId) {
        if(cifId!=null){
            cifId = cifId.trim();
        }
        this.cifId = cifId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
