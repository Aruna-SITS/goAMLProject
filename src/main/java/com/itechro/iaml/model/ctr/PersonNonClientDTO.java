package com.itechro.iaml.model.ctr;

import java.util.Date;
import java.util.List;

public class PersonNonClientDTO {

    private Integer tranUid;

    private String gender;

    private String title;

    private String firstName;

    private String middleName;

    private String lastName;

    private Date birthDate;

    private String idNumber;

    private List<PhoneDTO> phoneDTO;

    private List<AddressDTO> addressDTO;

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

    public Integer getTranUid() {
        return tranUid;
    }

    public void setTranUid(Integer tranUid) {
        this.tranUid = tranUid;
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
}
