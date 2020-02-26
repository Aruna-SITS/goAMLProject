package com.itechro.iaml.service.ctr.support;

import com.itechro.iaml.config.IAMLProperties;
import com.itechro.iaml.dao.ctr.CTRJdbcDao;
import com.itechro.iaml.exception.impl.AppsException;
import com.itechro.iaml.model.ctr.*;
import com.itechro.iaml.service.ctr.JavaToXMLAdaptor;
import com.itechro.iaml.util.CalendarUtil;
import com.itechro.iaml.util.XMLFileWriter;
import generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ReportGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGenerator.class);

    private CTRJdbcDao ctrJdbcDao;

    private IAMLProperties applicationProperties;

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


    public void init() {
        ReportDataLoader reportDataLoader = new ReportDataLoader();
        reportDataLoader.setCtrJdbcDao(ctrJdbcDao);
        reportDataLoader.loadData();

        transactionsMap = reportDataLoader.getTransactionsMap();
        entitiesMap = reportDataLoader.getEntitiesMap();
        personMap = reportDataLoader.getPersonMap();
        accountsMap = reportDataLoader.getAccountsMap();
        personNonClientMap = reportDataLoader.getPersonNonClientMap();
        entityNonClientMap = reportDataLoader.getEntityNonClientMap();
        personalIdentificationMap = reportDataLoader.getPersonalIdentificationMap();
        phoneMap = reportDataLoader.getPhoneMap();
        addressMap = reportDataLoader.getAddressMap();
        relatedPartyMap = reportDataLoader.getRelatedPartyMap();
        fromToMappingDTOMap = reportDataLoader.getFromToMappingDTOMap();

    }


    public void generateReport() {
        int a = 0;
        int fileNumber = 1;
//        for (Integer transactionNumber : transactionsMap.keySet()) {
//            if (a == applicationProperties.getNumberOfRecordsToLoad()) {
//
//                XMLFileWriter.writeReportXML("sample_" + fileNumber + ".xml", null);
//                fileNumber++;
//                 report = factory.createReport();
//                a = 0;
//            }
//            a++;
//
//            ReportDTO reportDTO = getATransactionForReportDTO(transactionNumber);
//
//            report.setRentityId(reportDTO.getrEntityId());
//            report.setSubmissionCode(SubmissionType.valueOf(reportDTO.getSubmissionCode()));
//            report.setReportCode(ReportType.valueOf(reportDTO.getReportCode()));
//            report.setSubmissionDate(getXMLGregorianCalendarFromString(applicationProperties.getSubmissionDate()));
//            report.setCurrencyCodeLocal(reportDTO.getCurrencyCodeLocal());
//            report.setEntityReference("");
//
//            Report.Transaction xmlTransaction = factory.createReportTransaction();
//
//            TransactionDTO transaction = reportDTO.getTransactionDTO();
//
//            xmlTransaction.setTransactionnumber(transaction.getTransactionNumber());
//            xmlTransaction.setInternalRefNumber(transaction.getInternalRefNumber());
//            xmlTransaction.setTransactionLocation(transaction.getTransactionLocation());
//            xmlTransaction.setTransactionDescription(transaction.getTransactionDescription());
//            xmlTransaction.setTransmodeCode(transaction.getTransModeCode());
//            xmlTransaction.setAmountLocal(BigDecimal.valueOf(transaction.getAmountLocal()));
//
//
//            FromToMappingDTO from = reportDTO.getTransactionFrom();
//            FromToMappingDTO to = reportDTO.getTransactionTo();
//
//
//            if (from != null) {
//                TForeignCurrency fromForeignCurrency = factory.createTForeignCurrency();
//
//                if (from.getForeignCurrencyCode() != null) {
//                    fromForeignCurrency.setForeignCurrencyCode(from.getForeignCurrencyCode());
//                    fromForeignCurrency.setForeignAmount(BigDecimal.valueOf(from.getForeignAmount()));
//                    fromForeignCurrency.setForeignExchangeRate(BigDecimal.valueOf(from.getForeignExchangeRate()));
//                }
//
//                if (from.getTransactionType().equals(applicationProperties.getFromMyClient())) {
//                    Report.Transaction.TFromMyClient tFromMyClient = factory.createReportTransactionTFromMyClient();
//
//                    tFromMyClient.setFromFundsCode(from.getFundsCode());
//                    tFromMyClient.setFromForeignCurrency(fromForeignCurrency);
//                    tFromMyClient.setFromCountry(from.getCountry());
//                    tFromMyClient.setFromFundsComment(from.getFundsComment());
//                    if (from.getAccountsDTO() != null) {
//
//                        TAccountMyClient tAccountMyClient = getTAccountMyClient(factory, from);
//                        tFromMyClient.setFromAccount(tAccountMyClient);
//
//                    } else if (from.getEntitiesDTO() != null) {
//
//                        TEntityMyClient tEntityMyClient = getTEntityMyClient(factory, from);
//
//                        tFromMyClient.setFromEntity(tEntityMyClient);
//
//                    } else if (from.getPersonDTO() != null) {
//
//                        TPersonMyClient tPersonMyClient = getTPersonMyClient(factory, from.getPersonDTO());
//                        tFromMyClient.setFromPerson(tPersonMyClient);
//
//                    }
//                    xmlTransaction.setTFromMyClient(tFromMyClient);
//                } else if (from.getTransactionType().equals(applicationProperties.getFromNonClient())) {
//
//                    Report.Transaction.TFrom tFrom = factory.createReportTransactionTFrom();
//
//                    tFrom.setFromFundsCode(from.getFundsCode());
//                    tFrom.setFromFundsComment(from.getFundsComment());
//                    tFrom.setFromCountry(from.getCountry());
//                    tFrom.setFromForeignCurrency(fromForeignCurrency);
//
//                    if (from.getEntityNonClientDTO() != null) {
//
//                        EntityNonClientDTO entityNonClient = from.getEntityNonClientDTO();
//                        TEntity tEntity = factory.createTEntity();
//                        tEntity.setName(entityNonClient.getName());
//                        tEntity.setIncorporationCountryCode(entityNonClient.getCountryCode());
//                        tEntity.setIncorporationNumber(entityNonClient.getIncorporationNumber());
//                        tFrom.setFromEntity(tEntity);
//                    } else if (from.getPersonNonClientDTO() != null) {
//
//                        PersonNonClientDTO personNonClient = from.getPersonNonClientDTO();
//                        TPerson tPerson = getTPerson(factory, from);
//                        tFrom.setFromPerson(tPerson);
//                    } else {
//                        LOG.error("The From Details invalid " + from.getTransactionType());
//                    }
//                    xmlTransaction.setTFrom(tFrom);
//                }
//            } else {
//                LOG.error("Transaction from details not found ");
//            }
//            if (to != null) {
//                TForeignCurrency toForeignCurrency = factory.createTForeignCurrency();
//                if (to.getTransactionType().equals(applicationProperties.getToMyClient())) {
//                    Report.Transaction.TToMyClient tToMyClient = factory.createReportTransactionTToMyClient();
//
//                    tToMyClient.setToFundsCode(to.getFundsCode());
//                    tToMyClient.setToForeignCurrency(toForeignCurrency);
//                    tToMyClient.setToCountry(to.getCountry());
//                    tToMyClient.setToFundsComment(to.getFundsComment());
//
//                    if (toForeignCurrency.getForeignCurrencyCode() != null) {
//                        toForeignCurrency.setForeignCurrencyCode(to.getForeignCurrencyCode());
//                        toForeignCurrency.setForeignAmount(BigDecimal.valueOf(to.getForeignAmount()));
//                        toForeignCurrency.setForeignExchangeRate(BigDecimal.valueOf(to.getForeignExchangeRate()));
//                    }
//
//                    if (to.getAccountsDTO() != null) {
//
//                        TAccountMyClient tAccountMyClient = getTAccountMyClient(factory, to);
//                        tToMyClient.setToAccount(tAccountMyClient);
//
//                    } else if (to.getEntitiesDTO() != null) {
//
//                        TEntityMyClient tEntityMyClient = getTEntityMyClient(factory, to);
//
//                        tToMyClient.setToEntity(tEntityMyClient);
//
//                    } else if (to.getPersonDTO() != null) {
//
//                        TPersonMyClient tPersonMyClient = getTPersonMyClient(factory, to.getPersonDTO());
//                        tToMyClient.setToPerson(tPersonMyClient);
//
//                    }
//                    xmlTransaction.setTToMyClient(tToMyClient);
//                } else {
//                    Report.Transaction.TTo tTo = factory.createReportTransactionTTo();
//
//                    tTo.setToForeignCurrency(toForeignCurrency);
//                    tTo.setToFundsCode(to.getFundsCode());
//                    tTo.setToCountry(to.getCountry());
//                    tTo.setToFundsComment(to.getFundsComment());
//
//                    if (to.getEntityNonClientDTO() != null) {
//
//                        EntityNonClientDTO entityNonClient = to.getEntityNonClientDTO();
//                        TEntity tEntity = factory.createTEntity();
//                        tEntity.setName(entityNonClient.getName());
//                        tEntity.setIncorporationCountryCode(entityNonClient.getCountryCode());
//                        tEntity.setIncorporationNumber(entityNonClient.getIncorporationNumber());
//                        tTo.setToEntity(tEntity);
//                    } else if (to.getPersonNonClientDTO() != null) {
//
//                        TPerson tPerson = getTPerson(factory, to);
//                        tTo.setToPerson(tPerson);
//                    } else {
//                        LOG.error("The To Details invalid " + to.getTransactionType());
//                    }
//                    xmlTransaction.setTTo(tTo);
//                }
//            } else {
//                LOG.error("Transaction to details not found ");
//            }
//
//            report.getTransaction().add(xmlTransaction);
//        }
//        if (a != 0) {
//            XMLFileWriter.writeReportXML("sample.xml", null);
//        }
    }


//    private ReportDTO getNewReportDTO(Integer transactionNumber) {
//
//
//        ReportDTO reportDTO = new ReportDTO();
//
//
//        List<FromToMappingDTO> fromToMappingDTO = fromToMappingDTOMap.get(transactionNumber.toString());
//
//
//        reportDTO.setrEntityId(applicationProperties.getPublicREntityId());
//        reportDTO.setSubmissionCode(applicationProperties.getSubmissionCode());
//        reportDTO.setReportCode(applicationProperties.getReportCode());
//        try {
//            final Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(applicationProperties.getSubmissionDate());
//            reportDTO.setSubmissionDate(date);
//        } catch (Exception e) {
//            LOG.error("Could not parse the date ", e);
//        }
//
//        reportDTO.setCurrencyCodeLocal(applicationProperties.getCurrencyLocalCode());
//
//
//        reportDTO.setTransactionDTO(transactionsMap.get(transactionNumber));
//
//        Iterator<FromToMappingDTO> fromToMappingDTOIterator = fromToMappingDTO.iterator();
//        while (fromToMappingDTOIterator.hasNext()) {
//            FromToMappingDTO fromToMapping = fromToMappingDTOIterator.next();
//            if (fromToMapping.getTransactionType().equals(applicationProperties.fromMyClient)
//                    || fromToMapping.getTransactionType().equals(applicationProperties.fromNonClient)) {
//                reportDTO.setTransactionFrom(fromToMapping);
//                setValidLevels(reportDTO, reportDTO.getTransactionFrom());
//            } else {
//                reportDTO.setTransactionTo(fromToMapping);
//                setValidLevels(reportDTO, reportDTO.getTransactionTo());
//            }
//        }
//
//        return reportDTO;
//    }




    public TransactionDTO processLoadedData(String txnUUID) {

        List<FromToMappingDTO> fromToMappingDTO = fromToMappingDTOMap.get(txnUUID);

        TransactionDTO transactionDTO = transactionsMap.get(txnUUID);

        Iterator<FromToMappingDTO> fromToMappingDTOIterator = fromToMappingDTO.iterator();
        while (fromToMappingDTOIterator.hasNext()) {
            FromToMappingDTO fromToMapping = fromToMappingDTOIterator.next();
            if (fromToMapping.getTransactionType().equals(applicationProperties.fromMyClient)
                    || fromToMapping.getTransactionType().equals(applicationProperties.fromNonClient)) {
                transactionDTO.setTransactionFrom(fromToMapping);
                setValidLevels(transactionDTO.getTransactionFrom());
            } else {
                transactionDTO.setTransactionTo(fromToMapping);
                setValidLevels(transactionDTO.getTransactionTo());
            }
        }

        return transactionDTO;
    }


    //To map the data to corresponding types based on account, person, entities
    private void setValidLevels(FromToMappingDTO transactionFromOrTo) {

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
                transactionFromOrTo.setEntitiesDTO(entitiesMap.get(transactionFromOrTo.getCifId()));
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
                if (personMap.containsKey(transactionFromOrTo.getCifId())) {
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

                } else if (entitiesMap.containsKey(transactionFromOrTo.getCifId())) {
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

}
