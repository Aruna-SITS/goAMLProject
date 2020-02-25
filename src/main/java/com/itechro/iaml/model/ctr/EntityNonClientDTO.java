package com.itechro.iaml.model.ctr;

import java.util.List;

public class EntityNonClientDTO {

    private String countryCode;

    private String tranUid;

    private String   name;

    private  String incorporationNumber;

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTranUid() {
        return tranUid;
    }

    public void setTranUid(String tranUid) {
        this.tranUid = tranUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIncorporationNumber() {
        return incorporationNumber;
    }

    public void setIncorporationNumber(String incorporationNumber) {
        this.incorporationNumber = incorporationNumber;
    }
}
