package com.itechro.iaml.service.ctr.support;

import com.itechro.iaml.dao.ctr.CTRJdbcDao;
import com.itechro.iaml.model.ctr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class ReportDataLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ReportDataLoader.class);


    private CTRJdbcDao ctrJdbcDao;

    private HashMap<Integer, TransactionDTO> transactionsMap;

    private HashMap<String, List<FromToMappingDTO>> fromToMappingDTOMap;

    private HashMap<String, EntitiesDTO> entitiesMap;

    private HashMap<String, PersonDTO> personMap;

    private HashMap<String, AccountsDTO> accountsMap;

    private HashMap<Integer, PersonNonClientDTO> personNonClientMap;

    private HashMap<String, EntityNonClientDTO> entityNonClientMap;

    private HashMap<String, List<PersonIdentificationDTO>> personalIdentificationMap;

    private HashMap<String, List<PhoneDTO>> phoneMap;

    private HashMap<String, List<AddressDTO>> addressMap;

    private HashMap<String, List<RelatedPartyDTO>> relatedPartyMap;

    public void loadData() {

        transactionsMap = this.ctrJdbcDao.getTransactionsAsMap();

        entitiesMap = this.ctrJdbcDao.getEntitiesMap();

        personMap = this.ctrJdbcDao.getPersonMap();

        accountsMap = this.ctrJdbcDao.getAccountsMap();

        personNonClientMap = this.ctrJdbcDao.getPersonNonClientMap();

        entityNonClientMap = this.ctrJdbcDao.getEntityNonClientList();

        personalIdentificationMap = this.ctrJdbcDao.getPersonIdentificationMap();

        phoneMap = this.ctrJdbcDao.getPhoneMap();

        addressMap = this.ctrJdbcDao.getAddressMap();

        relatedPartyMap = this.ctrJdbcDao.getRelatedPartyMap();

        fromToMappingDTOMap = this.ctrJdbcDao.getFromToMappingRecordsAsMap();
    }

    public HashMap<Integer, TransactionDTO> getTransactionsMap() {
        return transactionsMap;
    }

    public HashMap<String, List<FromToMappingDTO>> getFromToMappingDTOMap() {
        return fromToMappingDTOMap;
    }

    public HashMap<String, EntitiesDTO> getEntitiesMap() {
        return entitiesMap;
    }

    public HashMap<String, PersonDTO> getPersonMap() {
        return personMap;
    }

    public HashMap<String, AccountsDTO> getAccountsMap() {
        return accountsMap;
    }

    public HashMap<Integer, PersonNonClientDTO> getPersonNonClientMap() {
        return personNonClientMap;
    }

    public HashMap<String, EntityNonClientDTO> getEntityNonClientMap() {
        return entityNonClientMap;
    }

    public HashMap<String, List<PersonIdentificationDTO>> getPersonalIdentificationMap() {
        return personalIdentificationMap;
    }

    public HashMap<String, List<PhoneDTO>> getPhoneMap() {
        return phoneMap;
    }

    public HashMap<String, List<AddressDTO>> getAddressMap() {
        return addressMap;
    }

    public HashMap<String, List<RelatedPartyDTO>> getRelatedPartyMap() {
        return relatedPartyMap;
    }

    public void setCtrJdbcDao(CTRJdbcDao ctrJdbcDao) {
        this.ctrJdbcDao = ctrJdbcDao;
    }
}
