package com.itechro.iaml.service.ctr;

import com.itechro.iaml.config.IAMLProperties;
import com.itechro.iaml.dao.ctr.CTRJdbcDao;
import com.itechro.iaml.model.ctr.*;
import generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JavaToXMLAdaptor {

    private static final Logger LOG = LoggerFactory.getLogger(JavaToXMLAdaptor.class);

    private CTRJdbcDao ctrJdbcDao;

    private IAMLProperties applicationProperties;

    private HashMap<Integer, TransactionDTO> transactionsMap;

    private HashMap<String, EntitiesDTO> entitiesMap;

    private HashMap<String, PersonDTO> personMap;

    private HashMap<String, AccountsDTO> accountsMap;

    private HashMap<Integer, PersonNonClientDTO> personNonClientMap;

    private HashMap<String, EntityNonClientDTO> entityNonClientMap;

    private HashMap<String, List<PersonIdentificationDTO>> personalIdentificationMap;

    private HashMap<String, List<PhoneDTO>> phoneMap;

    private HashMap<String, List<AddressDTO>> addressMap;

    private HashMap<String, List<RelatedPartyDTO>> relatedPartyMap;

    public void setCtrJdbcDao(CTRJdbcDao ctrJdbcDao) {
        this.ctrJdbcDao = ctrJdbcDao;
    }

    public void setApplicationProperties(IAMLProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public Report getReport() {

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

        ObjectFactory factory = new ObjectFactory();
        Report report = factory.createReport();

        Iterator<Map.Entry<Integer, TransactionDTO>> transactionsIterator = transactionsMap.entrySet().iterator();
        while (transactionsIterator.hasNext()) {
            Map.Entry<Integer, TransactionDTO> aTransaction = transactionsIterator.next();
            ReportDTO reportDTO = getATransactionForReportDTO(aTransaction.getKey());

            report.setRentityId(reportDTO.getrEntityId());
            report.setSubmissionCode(SubmissionType.valueOf(reportDTO.getSubmissionCode()));
            report.setReportCode(ReportType.valueOf(reportDTO.getReportCode()));
            report.setSubmissionDate(getXMLGregorianCalendarFromString(applicationProperties.getSubmissionDate()));
            report.setCurrencyCodeLocal(reportDTO.getCurrencyCodeLocal());
            report.setEntityReference("");

            Report.Transaction xmlTransaction = factory.createReportTransaction();

            TransactionDTO transaction = reportDTO.getTransactionDTO();

            xmlTransaction.setTransactionnumber(transaction.getTransactionNumber());
            xmlTransaction.setInternalRefNumber(transaction.getInternalRefNumber());
            xmlTransaction.setTransactionLocation(transaction.getTransactionLocation());
            xmlTransaction.setTransactionDescription(transaction.getTransactionDescription());
            xmlTransaction.setTransmodeCode(transaction.getTransModeCode());
            xmlTransaction.setAmountLocal(BigDecimal.valueOf(transaction.getAmountLocal()));


            FromToMappingDTO from = reportDTO.getTransactionFrom();
            FromToMappingDTO to = reportDTO.getTransactionTo();


            if (from != null) {
                TForeignCurrency fromForeignCurrency = factory.createTForeignCurrency();

                if (from.getForeignCurrencyCode() != null) {
                    fromForeignCurrency.setForeignCurrencyCode(from.getForeignCurrencyCode());
                    fromForeignCurrency.setForeignAmount(BigDecimal.valueOf(from.getForeignAmount()));
                    fromForeignCurrency.setForeignExchangeRate(BigDecimal.valueOf(from.getForeignExchangeRate()));
                }

                if (from.getTransactionType().equals(applicationProperties.getFromMyClient())) {
                    Report.Transaction.TFromMyClient tFromMyClient = factory.createReportTransactionTFromMyClient();

                    tFromMyClient.setFromFundsCode(from.getFundsCode());
                    tFromMyClient.setFromForeignCurrency(fromForeignCurrency);
                    tFromMyClient.setFromCountry(from.getCountry());
                    tFromMyClient.setFromFundsComment(from.getFundsComment());
                    if (from.getAccountsDTO() != null) {

                        TAccountMyClient tAccountMyClient = getTAccountMyClient(factory, from);
                        tFromMyClient.setFromAccount(tAccountMyClient);

                    } else if (from.getEntitiesDTO() != null) {

                        TEntityMyClient tEntityMyClient = getTEntityMyClient(factory, from);

                        tFromMyClient.setFromEntity(tEntityMyClient);

                    } else if (from.getPersonDTO() != null) {

                        TPersonMyClient tPersonMyClient = getTPersonMyClient(factory, from.getPersonDTO());
                        tFromMyClient.setFromPerson(tPersonMyClient);

                    }
                    xmlTransaction.setTFromMyClient(tFromMyClient);
                } else if (from.getTransactionType().equals(applicationProperties.getFromNonClient())) {

                    Report.Transaction.TFrom tFrom = factory.createReportTransactionTFrom();

                    tFrom.setFromFundsCode(from.getFundsCode());
                    tFrom.setFromFundsComment(from.getFundsComment());
                    tFrom.setFromCountry(from.getCountry());
                    tFrom.setFromForeignCurrency(fromForeignCurrency);

                    if (from.getEntityNonClientDTO() != null) {

                        EntityNonClientDTO entityNonClient = from.getEntityNonClientDTO();
                        TEntity tEntity = factory.createTEntity();
                        tEntity.setName(entityNonClient.getName());
                        tEntity.setIncorporationCountryCode(entityNonClient.getCountryCode());
                        tEntity.setIncorporationNumber(entityNonClient.getIncorporationNumber());
                        tFrom.setFromEntity(tEntity);
                    } else if (from.getPersonNonClientDTO() != null) {

                        PersonNonClientDTO personNonClient = from.getPersonNonClientDTO();
                        TPerson tPerson = getTPerson(factory, from);
                        tFrom.setFromPerson(tPerson);
                    } else {
                        LOG.error("The From Details invalid " + from.getTransactionType());
                    }
                    xmlTransaction.setTFrom(tFrom);
                }
            } else {
                LOG.error("Transaction from details not found ");
            }
            if (to != null) {
                TForeignCurrency toForeignCurrency = factory.createTForeignCurrency();
                if (to.getTransactionType().equals(applicationProperties.getToMyClient())) {
                    Report.Transaction.TToMyClient tToMyClient = factory.createReportTransactionTToMyClient();

                    tToMyClient.setToFundsCode(to.getFundsCode());
                    tToMyClient.setToForeignCurrency(toForeignCurrency);
                    tToMyClient.setToCountry(to.getCountry());
                    tToMyClient.setToFundsComment(to.getFundsComment());

                    if (toForeignCurrency.getForeignCurrencyCode() != null) {
                        toForeignCurrency.setForeignCurrencyCode(to.getForeignCurrencyCode());
                        toForeignCurrency.setForeignAmount(BigDecimal.valueOf(to.getForeignAmount()));
                        toForeignCurrency.setForeignExchangeRate(BigDecimal.valueOf(to.getForeignExchangeRate()));
                    }

                    if (to.getAccountsDTO() != null) {

                        TAccountMyClient tAccountMyClient = getTAccountMyClient(factory, to);
                        tToMyClient.setToAccount(tAccountMyClient);

                    } else if (to.getEntitiesDTO() != null) {

                        TEntityMyClient tEntityMyClient = getTEntityMyClient(factory, to);

                        tToMyClient.setToEntity(tEntityMyClient);

                    } else if (to.getPersonDTO() != null) {

                        TPersonMyClient tPersonMyClient = getTPersonMyClient(factory, to.getPersonDTO());
                        tToMyClient.setToPerson(tPersonMyClient);

                    }
                    xmlTransaction.setTToMyClient(tToMyClient);
                } else {
                    Report.Transaction.TTo tTo = factory.createReportTransactionTTo();

                    tTo.setToForeignCurrency(toForeignCurrency);
                    tTo.setToFundsCode(to.getFundsCode());
                    tTo.setToCountry(to.getCountry());
                    tTo.setToFundsComment(to.getFundsComment());

                    if (to.getEntityNonClientDTO() != null) {

                        EntityNonClientDTO entityNonClient = to.getEntityNonClientDTO();
                        TEntity tEntity = factory.createTEntity();
                        tEntity.setName(entityNonClient.getName());
                        tEntity.setIncorporationCountryCode(entityNonClient.getCountryCode());
                        tEntity.setIncorporationNumber(entityNonClient.getIncorporationNumber());
                        tTo.setToEntity(tEntity);
                    } else if (to.getPersonNonClientDTO() != null) {

                        TPerson tPerson = getTPerson(factory, to);
                        tTo.setToPerson(tPerson);
                    } else {
                        LOG.error("The To Details invalid " + to.getTransactionType());
                    }
                    xmlTransaction.setTTo(tTo);
                }
            } else {
                LOG.error("Transaction to details not found ");
            }

            report.getTransaction().add(xmlTransaction);
        }
        return report;
    }

    //To map non client person to TPerson xml object
    private TPerson getTPerson(ObjectFactory factory, FromToMappingDTO to) {
        PersonNonClientDTO personNonClient = to.getPersonNonClientDTO();
        TPerson tPerson = factory.createTPerson();
        tPerson.setGender(personNonClient.getGender());
        if (personNonClient.getTitle() != null) {
            tPerson.setTitle(factory.createTPersonTitle(personNonClient.getTitle()));
        }
        tPerson.setFirstName(personNonClient.getFirstName());
        tPerson.setLastName(personNonClient.getLastName());
        if (personNonClient.getBirthDate() != null) {
            tPerson.setBirthdate(getXMLGregorianCalendarFromDate(personNonClient.getBirthDate()));
        }
        tPerson.setIdNumber(personNonClient.getIdNumber());
        return tPerson;
    }

    //To map Account my client to xml TAccountMyClient
    private TAccountMyClient getTAccountMyClient(ObjectFactory factory, FromToMappingDTO from) {

        AccountsDTO accounts = from.getAccountsDTO();
        TAccountMyClient tAccountMyClient = factory.createTAccountMyClient();

        tAccountMyClient.setInstitutionName(accounts.getInstituationName());
        tAccountMyClient.setInstitutionCode(accounts.getInstitutionCode());
        tAccountMyClient.setSwift(accounts.getSwift());
        tAccountMyClient.setBranch(accounts.getBranch());
        tAccountMyClient.setAccount(accounts.getAccount());
        tAccountMyClient.setCurrencyCode(accounts.getCurrencyCode());
        tAccountMyClient.setAccountName(accounts.getAccountName());

        if(from.getAccountsDTO().getEntitiesDTO()!=null){
            FromToMappingDTO aTemp=new FromToMappingDTO();
            aTemp.setEntitiesDTO(accounts.getEntitiesDTO());
            tAccountMyClient.setTEntity(getTEntityMyClient(factory,aTemp));
        }

        if (accounts.getRelatedPartyDTO() != null) {
            Iterator<RelatedPartyDTO> relatedPartyIterator = accounts.getRelatedPartyDTO().iterator();

            while (relatedPartyIterator.hasNext()) {

                TAccountMyClient.Signatory signatory = factory.createTAccountMyClientSignatory();

                RelatedPartyDTO relatedParty = relatedPartyIterator.next();

                signatory.setRole(relatedParty.getRole());
                signatory.setIsPrimary(relatedParty.getIsPrimary());
                if (personMap.containsKey(from.getCifId())) {
                    PersonDTO person = personMap.get(from.getCifId());

                    TPersonMyClient tPersonMyClient = getTPersonMyClient(factory, person);

                    if (person.getPhoneDTO() != null) {
                        Iterator<PhoneDTO> phoneIterator = person.getPhoneDTO().iterator();
                        TPersonMyClient.Phones phones = factory.createTPersonMyClientPhones();
                        setPhoneToXML(factory, phoneIterator, phones.getPhone());
                        tPersonMyClient.setPhones(phones);
                    }

                    if (person.getAddressDTO() != null) {
                        TPersonMyClient.Addresses addresses = factory.createTPersonMyClientAddresses();
                        Iterator<AddressDTO> addressIterator = person.getAddressDTO().iterator();
                        setAddressToXML(factory, addressIterator, addresses.getAddress());
                    }

                    if(person.getPersonIdentificationDTO()!=null){

                        setTPersonalIdentification(factory, person, tPersonMyClient);

                    }
                    signatory.setTPerson(tPersonMyClient);
                } else {
                    LOG.error("Valid Person Not found for the Signatory CIF ID " + from.getCifId() + " Transaction Number " + from.getTransId());
                }
                tAccountMyClient.getSignatory().add(signatory);
            }
        }

        if (accounts.getOpened() != null) {
            tAccountMyClient.setOpened(getXMLGregorianCalendarFromDate(accounts.getOpened()));
        }
        if (accounts.getClosed() != null) {
            tAccountMyClient.setClosed(getXMLGregorianCalendarFromDate(accounts.getClosed()));
        }

        tAccountMyClient.setStatusCode(accounts.getStatusCode());
        if (accounts.getBeneficiary() != null) {
            tAccountMyClient.setBeneficiary(factory.createTAccountBeneficiary(accounts.getBeneficiary()));
        }
        if (accounts.getBeneficiaryComment() != null) {
            tAccountMyClient.setBeneficiaryComment(factory.createTAccountBeneficiaryComment(accounts.getComments()));
        }
        tAccountMyClient.setComments(accounts.getComments());
        return tAccountMyClient;
    }

    private TEntityMyClient getTEntityMyClient(ObjectFactory factory, FromToMappingDTO to) {

        EntitiesDTO entities = to.getEntitiesDTO();
        TEntityMyClient tEntityMyClient = factory.createTEntityMyClient();

        tEntityMyClient.setName(entities.getName());
        tEntityMyClient.setCommercialName(entities.getCommercialName());
        tEntityMyClient.setIncorporationLegalForm(entities.getIncorporationLegalForm());
        tEntityMyClient.setIncorporationNumber(entities.getIncorporationNumber());
        tEntityMyClient.setBusiness(entities.getBusiness());

        if (entities.getPhoneDTO() != null) {
            Iterator<PhoneDTO> phoneIterator = entities.getPhoneDTO().iterator();
            TEntityMyClient.Phones phones = factory.createTEntityMyClientPhones();
            setPhoneToXML(factory, phoneIterator, phones.getPhone());
            tEntityMyClient.setPhones(phones);
        }
        if (entities.getAddressDTO() != null) {
            TEntityMyClient.Addresses addresses = factory.createTEntityMyClientAddresses();
            Iterator<AddressDTO> addressIterator = entities.getAddressDTO().iterator();
            setAddressToXML(factory, addressIterator, addresses.getAddress());
            tEntityMyClient.setAddresses(addresses);
        }

        if (entities.getEmail() != null) {
            tEntityMyClient.setEmail(entities.getEmail());
        }

        tEntityMyClient.setIncorporationCountryCode(entities.getIncorporationCountryCode());
        if (entities.getIncorporationDate() != null) {
            tEntityMyClient.setIncorporationDate(getXMLGregorianCalendarFromDate(entities.getIncorporationDate()));
        }
        tEntityMyClient.setBusinessClosed(Boolean.valueOf(entities.getBusinessClosed()));
        if (entities.getDateBusinessClosed() != null) {
            tEntityMyClient.setDateBusinessClosed(getXMLGregorianCalendarFromDate(entities.getDateBusinessClosed()));
        }
        tEntityMyClient.setTaxNumber(entities.getTaxNumber());
        tEntityMyClient.setTaxRegNumber(entities.getTaxRegNumber());
        return tEntityMyClient;
    }

    private void setAddressToXML(ObjectFactory factory, Iterator<AddressDTO> addressIterator, List<TAddress> address2) {
        while ((addressIterator.hasNext())) {
            TAddress tAddress = factory.createTAddress();
            AddressDTO address = addressIterator.next();
            tAddress.setAddressType(address.getAddressType());
            tAddress.setAddress(address.getAddress());
            tAddress.setTown(address.getTown());
            tAddress.setCity(address.getCity());
            tAddress.setZip(address.getZip());
            tAddress.setCountryCode(address.getCountryCode());
            tAddress.setState(address.getState());
            tAddress.setComments(address.getComments());
            address2.add(tAddress);
        }
    }

    private TPersonMyClient getTPersonMyClient(ObjectFactory factory, PersonDTO person) {
        TPersonMyClient tPersonMyClient = factory.createTPersonMyClient();

        tPersonMyClient.setGender(person.getGender());
        if (person.getTitle() != null) {
            tPersonMyClient.setTitle(factory.createTPersonTitle(person.getTitle()));
        }
        tPersonMyClient.setFirstName(person.getFirstName());
        tPersonMyClient.setLastName(person.getLastName());
        tPersonMyClient.setLastName(person.getLastName());
        if (person.getBirthDate() != null) {
            tPersonMyClient.setBirthdate(getXMLGregorianCalendarFromDate(person.getBirthDate()));
        }
        tPersonMyClient.setIdNumber(person.getIdNumber());
        tPersonMyClient.setNationality1(person.getNationality1());
        tPersonMyClient.setNationality2(person.getNationality2());
        //tPersonMyClient.setNationality3(); No Column
        tPersonMyClient.setResidence(person.getResidence());

        if (person.getPhoneDTO() != null) {
            Iterator<PhoneDTO> phoneIterator = person.getPhoneDTO().iterator();
            TPersonMyClient.Phones phones = factory.createTPersonMyClientPhones();

            setPhoneToXML(factory, phoneIterator, phones.getPhone());
            tPersonMyClient.setPhones(phones);
        }
        if (person.getAddressDTO() != null) {
            TPersonMyClient.Addresses addresses = factory.createTPersonMyClientAddresses();
            Iterator<AddressDTO> addressIterator = person.getAddressDTO().iterator();
            setAddressToXML(factory, addressIterator, addresses.getAddress());
            tPersonMyClient.setAddresses(addresses);
        }


        setTPersonalIdentification(factory, person, tPersonMyClient);

        if (person.getEmail() != null) {
            tPersonMyClient.getEmail().add(person.getEmail());//The actual email format should be a list
        }
        tPersonMyClient.setOccupation(person.getOccupation());
        tPersonMyClient.setComments(person.getComments());
        return tPersonMyClient;
    }

    private void setTPersonalIdentification(ObjectFactory factory, PersonDTO person, TPersonMyClient tPersonMyClient) {
        Iterator<PersonIdentificationDTO> phoneIterator = person.getPersonIdentificationDTO().iterator();
        TPersonIdentification tPersonIdentification = factory.createTPersonIdentification();
        while (phoneIterator.hasNext()) {
            PersonIdentificationDTO personIdentification = phoneIterator.next();
            tPersonIdentification.setType(personIdentification.getIdentificationType());
            tPersonIdentification.setNumber(personIdentification.getNumber());
            if (personIdentification.getIssueDate() != null) {
                tPersonIdentification.setIssueDate(getXMLGregorianCalendarFromDate(personIdentification.getIssueDate()));
            }
            if (personIdentification.getExpiryDate() != null) {
                tPersonIdentification.setExpiryDate(getXMLGregorianCalendarFromDate(personIdentification.getExpiryDate()));
            }
            tPersonIdentification.setIssuedBy(personIdentification.getIssuedBy());
            tPersonIdentification.setIssueCountry(personIdentification.getIssueCountry());
            tPersonIdentification.setComments(personIdentification.getComment());
            tPersonMyClient.getIdentification().add(tPersonIdentification);
        }
    }

    private void setPhoneToXML(ObjectFactory factory, Iterator<PhoneDTO> phoneIterator, List<TPhone> phone) {
        while (phoneIterator.hasNext()) {
            TPhone tPhone = factory.createTPhone();
            PhoneDTO aPhone = phoneIterator.next();
            tPhone.setTphContactType(aPhone.getContactType());
            tPhone.setTphCommunicationType(aPhone.getCommunicationType());
            tPhone.setTphCountryPrefix(aPhone.getCountryPrefix());
            tPhone.setTphNumber(aPhone.getPhoneNumber());
            tPhone.setComments(aPhone.getComments());
            phone.add(tPhone);
        }
    }

    private ReportDTO getATransactionForReportDTO(Integer transactionUid) {

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setrEntityId(applicationProperties.getPublicREntityId());
        reportDTO.setSubmissionCode(applicationProperties.getSubmissionCode());
        reportDTO.setReportCode(applicationProperties.getReportCode());
        try {
            final Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(applicationProperties.getSubmissionDate());
            reportDTO.setSubmissionDate(date);
        } catch (Exception e) {
            LOG.error("Could not parse the date ", e);
        }

        reportDTO.setCurrencyCodeLocal(applicationProperties.getCurrencyLocalCode());

        reportDTO.setTransactionDTO(transactionsMap.get(transactionUid));

        List<FromToMappingDTO> fromToMappingData = this.ctrJdbcDao.getFromToMappingRecords(transactionUid);

        Iterator<FromToMappingDTO> fromToMappingDTOIterator = fromToMappingData.iterator();
        while (fromToMappingDTOIterator.hasNext()) {
            FromToMappingDTO fromToMappingDTO = fromToMappingDTOIterator.next();
            if (fromToMappingDTO.getTransactionType().equals(applicationProperties.fromMyClient)
                    || fromToMappingDTO.getTransactionType().equals(applicationProperties.fromNonClient)) {
                reportDTO.setTransactionFrom(fromToMappingDTO);
                setValidLevels(reportDTO, reportDTO.getTransactionFrom());
            } else {
                reportDTO.setTransactionTo(fromToMappingDTO);
                setValidLevels(reportDTO, reportDTO.getTransactionTo());
            }
        }

        return reportDTO;
    }

    //To map the data to corresponding types based on account, person, entities
    private void setValidLevels(ReportDTO reportDTO, FromToMappingDTO transactionFromOrTo) {

        Integer transactionNumber = transactionFromOrTo.getTransId();

        if (transactionFromOrTo.getAcctNumber() == null
                && transactionFromOrTo.getCifId() != null
                && !entitiesMap.containsKey(transactionFromOrTo.getCifId())
        ) {

            LOG.info("Person Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());
            if (transactionFromOrTo.getCifId() != null && personMap.containsKey(transactionFromOrTo.getCifId())) {
                transactionFromOrTo.setPersonDTO(personMap.get(transactionFromOrTo.getCifId()));
                if (personalIdentificationMap.containsKey(transactionFromOrTo.getCifId())) {
                    transactionFromOrTo.getPersonDTO().setPersonIdentificationDTO(personalIdentificationMap.get(transactionFromOrTo.getCifId()));
                } else {
                    LOG.error("Transaction CIF_ID : " + transactionFromOrTo.getCifId() + " Personal Identification Details not found");
                }
                if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                    transactionFromOrTo.getPersonDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }
                if (transactionFromOrTo.getCifId() != null && addressMap.containsKey(transactionFromOrTo.getCifId())) {
                    transactionFromOrTo.getPersonDTO().setAddressDTO(addressMap.get(transactionFromOrTo.getCifId()));
                }


            } else {
                LOG.error("Person Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType() + " CIF_ID " + transactionFromOrTo.getCifId() + " Cif_Id in Person Table  " + personMap.containsKey(transactionFromOrTo.getCifId()));
            }


        } else if (transactionFromOrTo.getAcctNumber() == null && entitiesMap.containsKey(transactionFromOrTo.getCifId())) {

            LOG.info("Entity Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());
            if (transactionFromOrTo.getCifId() != null) {
                transactionFromOrTo.setEntitiesDTO(entitiesMap.get(reportDTO.getTransactionFrom().getCifId()));
                if (transactionFromOrTo.getCifId() != null && phoneMap.containsKey(transactionFromOrTo.getCifId())) {
                    transactionFromOrTo.getEntitiesDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }
            } else {
                LOG.error("Entity Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType() + " CIF_ID " + transactionFromOrTo.getCifId());
            }
            if (transactionFromOrTo.getCifId() != null && addressMap.containsKey(transactionFromOrTo.getCifId())) {
                transactionFromOrTo.getEntitiesDTO().setAddressDTO(addressMap.get(transactionFromOrTo.getCifId()));
            }

        } else if (transactionFromOrTo.getAcctNumber() != null) {

            LOG.info("Account Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());
            if (accountsMap.containsKey(transactionFromOrTo.getAcctNumber())) {
                transactionFromOrTo.setAccountsDTO(accountsMap.get(transactionFromOrTo.getAcctNumber()));
                if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                    transactionFromOrTo.getAccountsDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }
                if(personMap.containsKey(transactionFromOrTo.getCifId())){
                    transactionFromOrTo.getAccountsDTO().setPersonDTO(personMap.get(transactionFromOrTo.getCifId()));
                    if (personalIdentificationMap.containsKey(transactionFromOrTo.getCifId())) {
                        transactionFromOrTo.getAccountsDTO().getPersonDTO().setPersonIdentificationDTO(personalIdentificationMap.get(transactionFromOrTo.getCifId()));
                    } else {
                        LOG.error("Transaction CIF_ID : " + transactionFromOrTo.getCifId() + " Personal Identification Details not found");
                    }
                    if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                        transactionFromOrTo.getAccountsDTO().getPersonDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                    }
                    if (transactionFromOrTo.getCifId() != null && addressMap.containsKey(transactionFromOrTo.getCifId())) {
                        transactionFromOrTo.getAccountsDTO().getPersonDTO().setAddressDTO(addressMap.get(transactionFromOrTo.getCifId()));
                    }

                }else if(entitiesMap.containsKey(transactionFromOrTo.getCifId())){
                    transactionFromOrTo.getAccountsDTO().setEntitiesDTO(entitiesMap.get(transactionFromOrTo.getCifId()));

                    if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                        transactionFromOrTo.getAccountsDTO().getEntitiesDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                    }
                    if (transactionFromOrTo.getCifId() != null && addressMap.containsKey(transactionFromOrTo.getCifId())) {
                        transactionFromOrTo.getAccountsDTO().getEntitiesDTO().setAddressDTO(addressMap.get(transactionFromOrTo.getCifId()));
                    }
                }
                if (transactionFromOrTo.getCifId() != null && relatedPartyMap.containsKey(transactionFromOrTo.getCifId())) {

                    transactionFromOrTo.getAccountsDTO().setRelatedPartyDTO(relatedPartyMap.get(transactionFromOrTo.getCifId()));

                }
            } else {
                LOG.error("Account Details not found, Account Number : " + transactionFromOrTo.getAcctNumber());

            }

        } else if (transactionFromOrTo.getAcctNumber() == null && transactionFromOrTo.getCifId() == null) {

            if (personNonClientMap.containsKey(transactionNumber)) {

                LOG.info("Person Non Client Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());

                transactionFromOrTo.setPersonNonClientDTO(personNonClientMap.get(transactionNumber));
                if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                    transactionFromOrTo.getPersonNonClientDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }

            } else if (entityNonClientMap.containsKey(transactionNumber.toString())) {

                transactionFromOrTo.setEntityNonClientDTO(entityNonClientMap.get(transactionNumber.toString()));
                if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                    transactionFromOrTo.getEntityNonClientDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }

            } else {
                LOG.error("Can't find the relevant Non Client Person or Non Client Entity for transaction number : " + transactionNumber);
            }
        }
    }

    //to get the XMLGregorianCalendar date form java date
    private static XMLGregorianCalendar getXMLGregorianCalendarFromDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            LOG.error("Submission Date is null");
            return null;
        }
        String dateStr = date.toString();
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(date);
        XMLGregorianCalendar calendar = null;
        try {
            calendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(
                            gregory);
        } catch (DatatypeConfigurationException e) {
            LOG.error("Error while parsing date ", e);
        }
        return calendar;
    }

    //to get the XMLGregorianCalendar date form java String
    private static XMLGregorianCalendar getXMLGregorianCalendarFromString(String date) {
        Date xmlDate = null;
        try {
            xmlDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            LOG.error("Date Parse Error ", e);
        }
        return getXMLGregorianCalendarFromDate(xmlDate);
    }
}