package com.itechro.iaml.model.ctr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PersonDTO {

    private String cifId;

    private String gender;

    private String title;

    private String firstName;

    private String middleName;

    private String prefix;

    private String lastName;

    private Date birthDate;

    private String idNumber;


    private String nationality1;

    private String nationality2;

    private String residence;

    private String email;

    private String occupation;

    private String comments;

    private PersonIdentificationDTO personIdentificationDTO;

    private List<PhoneDTO> phoneDTO;

    private List<AddressDTO> addressDTO;

    public List<PhoneDTO> getPhoneDTO() {
        return phoneDTO;
    }

    public List<AddressDTO> getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(List<AddressDTO> addressDTO) {
        this.addressDTO = addressDTO;
    }

    public void setPhoneDTO(List<PhoneDTO> phoneDTO) {
        this.phoneDTO = phoneDTO;
    }

    public void addAddress(AddressDTO addressDTO) {
        this.addressDTO.add(addressDTO);
    }

    public PersonIdentificationDTO getPersonIdentificationDTO() {
        return personIdentificationDTO;
    }

    public void setPersonIdentificationDTO(PersonIdentificationDTO personIdentificationDTO) {
        this.personIdentificationDTO = personIdentificationDTO;
    }

    public String getCifId() {
        return cifId;
    }

    public void setCifId(String cifId) {
        this.cifId = cifId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getNationality1() {
        return nationality1;
    }

    public void setNationality1(String nationality1) {
        this.nationality1 = nationality1;
    }

    public String getNationality2() {
        return nationality2;
    }

    public void setNationality2(String nationality2) {
        this.nationality2 = nationality2;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
