package com.itechro.iaml.model.ctr;

import org.springframework.data.relational.core.sql.In;

public class FromToMappingDTO {

    private String transactionType;

    private Integer transId;

    private String fundsCode;

    private String fundsComment;

    private String foreignCurrency;

    private String country;

    private String acctNumber;

    private String cifId;
    
    private String direction;

    private String foreignCurrencyCode;

    private Integer foreignAmount;

    private Integer foreignExchangeRate;

    private AccountsDTO accountsDTO;

    private EntitiesDTO entitiesDTO;

    private PersonDTO personDTO;
    
    private String role;

    private PersonNonClientDTO personNonClientDTO;

    private EntityNonClientDTO entityNonClientDTO;
    
    public PersonNonClientDTO getPersonNonClientDTO() {
        return personNonClientDTO;
    }

    public void setPersonNonClientDTO(PersonNonClientDTO personNonClientDTO) {
        this.personNonClientDTO = personNonClientDTO;
    }

    public EntityNonClientDTO getEntityNonClientDTO() {
        return entityNonClientDTO;
    }

    public void setEntityNonClientDTO(EntityNonClientDTO entityNonClientDTO) {
        this.entityNonClientDTO = entityNonClientDTO;
    }

    public AccountsDTO getAccountsDTO() {
        return accountsDTO;
    }

    public void setAccountsDTO(AccountsDTO accountsDTO) {
        this.accountsDTO = accountsDTO;
    }

    public EntitiesDTO getEntitiesDTO() {
        return entitiesDTO;
    }

    public void setEntitiesDTO(EntitiesDTO entitiesDTO) {
        this.entitiesDTO = entitiesDTO;
    }

    public PersonDTO getPersonDTO() {
        return personDTO;
    }

    public void setPersonDTO(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    public String getFundsCode() {
        return fundsCode;
    }

    public void setFundsCode(String fundsCode) {
        this.fundsCode = fundsCode;
    }

    public String getFundsComment() {
        return fundsComment;
    }

    public void setFundsComment(String fundsComment) {
        this.fundsComment = fundsComment;
    }

    public String getForeignCurrency() {
        return foreignCurrency;
    }

    public void setForeignCurrency(String foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAcctNumber() {
        return acctNumber;
    }

    public void setAcctNumber(String acctNumber) {
        this.acctNumber = acctNumber;
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

    public String getForeignCurrencyCode() {
        return foreignCurrencyCode;
    }

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setForeignCurrencyCode(String foreignCurrencyCode) {
        this.foreignCurrencyCode = foreignCurrencyCode;
    }

    public Integer getForeignAmount() {
        return foreignAmount;
    }

    public void setForeignAmount(Integer foreignAmount) {
        this.foreignAmount = foreignAmount;
    }

    public Integer getForeignExchangeRate() {
        return foreignExchangeRate;
    }

    public void setForeignExchangeRate(Integer foreignExchangeRate) {
        this.foreignExchangeRate = foreignExchangeRate;
    }

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
