package com.itechro.iaml.model.ctr;

public class PartyDTO {
	
	private String role;
	private String fundCode;
	private String country;
	private String significance;
	private String comments;
    private PersonNonClientDTO personNonClientDTO;
    private PersonDTO personDTO;
    private EntitiesDTO entitiesDTO;
    private AccountsDTO accountsDTO;
    private EntityNonClientDTO entityNonClientDTO;
    
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSignificance() {
		return significance;
	}
	public void setSignificance(String significance) {
		this.significance = significance;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public PersonNonClientDTO getPersonNonClientDTO() {
		return personNonClientDTO;
	}
	public void setPersonNonClientDTO(PersonNonClientDTO personNonClientDTO) {
		this.personNonClientDTO = personNonClientDTO;
	}
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
	public AccountsDTO getAccountsDTO() {
		return accountsDTO;
	}
	public void setAccountsDTO(AccountsDTO accountsDTO) {
		this.accountsDTO = accountsDTO;
	}
	public EntityNonClientDTO getEntityNonClientDTO() {
		return entityNonClientDTO;
	}
	public void setEntityNonClientDTO(EntityNonClientDTO entityNonClientDTO) {
		this.entityNonClientDTO = entityNonClientDTO;
	}
	
}
