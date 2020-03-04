package com.itechro.iaml.service.ctr;

import com.itechro.iaml.config.IAMLProperties;
import com.itechro.iaml.dao.ctr.CTRJdbcDao;
import com.itechro.iaml.model.ctr.*;
import com.itechro.iaml.service.ctr.support.ReportDataLoader;
import com.itechro.iaml.util.CalendarUtil;
import com.itechro.iaml.util.LogFileWriter;
import com.itechro.iaml.util.XMLFileWriter;
import generated.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JavaToXMLAdaptor {

    private static final Logger LOG = LoggerFactory.getLogger(JavaToXMLAdaptor.class);

    private static List<String> errorsLog = new ArrayList<>();

    private static List<String> warnings = new ArrayList<>();

    private static List<String> successLog = new ArrayList<>();

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

    private String tranNumberForLog;

    public void setCtrJdbcDao(CTRJdbcDao ctrJdbcDao) {
        this.ctrJdbcDao = ctrJdbcDao;
    }

    public void setApplicationProperties(IAMLProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void generateReport() throws JAXBException {


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

        ObjectFactory factory = new ObjectFactory();
        Report report = factory.createReport();

        LOG.info("+++++++++++++++++++++++++ Execution Started ++++++++++++++++++++++++++++");
        successLog.add(new Date() + "+++++++++++++++++++++++++ Execution Started ++++++++++++++++++++++++++++");

        int recordCounter = 0;
        int fileNumber = 1;
        for (Integer transactionNumber : transactionsMap.keySet()) {
            if (recordCounter == applicationProperties.getNumberOfRecordsToLoad()) {

                XMLFileWriter.writeReportXML(applicationProperties.getOutputXmlFilePath(), applicationProperties.getOutputXmlFileName() + fileNumber + ".xml", report);
                report = factory.createReport();

                successLog.add(applicationProperties.getOutputXmlFileName()+".xml file Created in "+applicationProperties.getOutputXmlFilePath());
                LOG.info("{}.xml file created in {} ",applicationProperties.getOutputXmlFileName(),applicationProperties.getOutputXmlFilePath());

                LogFileWriter.writeLogFile(fileNumber, successLog, applicationProperties.getSuccessLogFileName(), applicationProperties.getSuccessLogFilePath());
                LogFileWriter.writeLogFile(fileNumber, errorsLog, applicationProperties.getErrorLogFileName(), applicationProperties.getErrorLogFilePath());
                LogFileWriter.writeLogFile(fileNumber, warnings, applicationProperties.getWarningLogFileName(), applicationProperties.getWarningLogFilePath());

                successLog.clear();
                errorsLog.clear();
                warnings.clear();

                fileNumber++;
                recordCounter = 0;
            }
            recordCounter++;

            ReportDTO reportDTO = getATransactionForReportDTO(transactionNumber);
            tranNumberForLog = reportDTO.getTransactionDTO().getTransactionNumber();

            setReportLevelData(report, reportDTO);

            //TODO: create recordCounter report indicator
            report.setReportIndicators(factory.createReportReportIndicators());//M

            Report.Transaction xmlTransaction = factory.createReportTransaction();

            TransactionDTO transaction = reportDTO.getTransactionDTO();

            setTransactionLevelData(xmlTransaction, transaction);

            FromToMappingDTO from = reportDTO.getTransactionFrom();
            FromToMappingDTO to = reportDTO.getTransactionTo();


            setFromDataForAdaptor(factory, xmlTransaction, from);

            setToDataForAdaptor(factory, xmlTransaction, from, to);

            report.getTransaction().add(xmlTransaction);
        }
        if (recordCounter != 0) {
            XMLFileWriter.writeReportXML(applicationProperties.getOutputXmlFilePath(), applicationProperties.getOutputXmlFileName() + fileNumber + ".xml", report);
        }
        LOG.info("+++++++++++++++++++++++++ Execution Completed ++++++++++++++++++++++++++++");
        successLog.add(new Date() + "+++++++++++++++++++++++++ Execution Completed ++++++++++++++++++++++++++++");

    }

    private void setToDataForAdaptor(ObjectFactory factory, Report.Transaction xmlTransaction, FromToMappingDTO from, FromToMappingDTO to) {
        if (to != null) {
            TForeignCurrency toForeignCurrency = null;
            if (to.getTransactionType().equals(applicationProperties.getToMyClient())) {
                Report.Transaction.TToMyClient tToMyClient = factory.createReportTransactionTToMyClient();

                tToMyClient.setToFundsCode(to.getFundsCode());
                tToMyClient.setToForeignCurrency(toForeignCurrency);
                tToMyClient.setToCountry(to.getCountry());
                tToMyClient.setToFundsComment(to.getFundsComment());

                if (to.getForeignCurrencyCode() != null) {
                    toForeignCurrency = factory.createTForeignCurrency();
                    toForeignCurrency.setForeignCurrencyCode(to.getForeignCurrencyCode());
                    toForeignCurrency.setForeignAmount(new BigDecimal(from.getForeignAmount()).setScale(2, BigDecimal.ROUND_DOWN));
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
                    LOG.error("The To Details invalid Transaction Number " + tranNumberForLog + to.getTransactionType());
                    errorsLog.add("The To Details invalid Transaction Number " + tranNumberForLog + to.getTransactionType());
                }
                xmlTransaction.setTTo(tTo);
            }
        } else {
            LOG.error("Transaction to details not found.Transaction Number " + tranNumberForLog);
            errorsLog.add("Transaction to details not found. Transaction Number " + tranNumberForLog);
        }
    }

    private void setFromDataForAdaptor(ObjectFactory factory, Report.Transaction xmlTransaction, FromToMappingDTO from) {
        if (from != null) {
            TForeignCurrency fromForeignCurrency=null;

            if (from.getForeignCurrencyCode() != null) {
                fromForeignCurrency = factory.createTForeignCurrency();
                fromForeignCurrency.setForeignCurrencyCode(from.getForeignCurrencyCode());
                fromForeignCurrency.setForeignAmount(new BigDecimal(from.getForeignAmount()).setScale(2, BigDecimal.ROUND_DOWN));
                fromForeignCurrency.setForeignExchangeRate(BigDecimal.valueOf(from.getForeignExchangeRate()));
            }

            if (from.getTransactionType().equals(applicationProperties.getFromMyClient())) {
                Report.Transaction.TFromMyClient tFromMyClient = factory.createReportTransactionTFromMyClient();

                if (from.getFundsCode() != null) {
                    tFromMyClient.setFromFundsCode(from.getFundsCode());//M
                } else {
                    LOG.error("No country data found for From Funds Code. CIF_ID: " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                    errorsLog.add("No country data found for From Funds Code. CIF_ID : " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                }
                if (fromForeignCurrency != null) {
                    tFromMyClient.setFromForeignCurrency(fromForeignCurrency);//N
                }

                if (from.getCountry() != null) {
                    tFromMyClient.setFromCountry(from.getCountry());//M
                } else {
                    LOG.error("No country data found for CIF_ID : " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                    errorsLog.add("No country data found for CIF_ID : " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                }
                if (StringUtils.isNotBlank(from.getFundsComment())) {
                    tFromMyClient.setFromFundsComment(from.getFundsComment());//C
                } else if (StringUtils.isNotBlank(from.getFundsComment()) && "OTHER".equalsIgnoreCase(from.getFundsCode())) {
                    //TODO att error log
                }

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
                    if (entityNonClient.getName() != null) {
                        tEntity.setName(entityNonClient.getName());
                    } else {
                        LOG.error("Name of the Entity Non Client is Empty. Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                        errorsLog.add("Name of the Entity Non Client is Empty. Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                    }
                    tEntity.setIncorporationCountryCode(entityNonClient.getCountryCode());
                    tEntity.setIncorporationNumber(entityNonClient.getIncorporationNumber());
                    tFrom.setFromEntity(tEntity);
                } else if (from.getPersonNonClientDTO() != null) {

                    PersonNonClientDTO personNonClient = from.getPersonNonClientDTO();
                    TPerson tPerson = getTPerson(factory, from);
                    tFrom.setFromPerson(tPerson);
                } else {
                    LOG.error("The From Details invalid. Transaction Number " + tranNumberForLog + from.getTransactionType());
                    errorsLog.add("From Details invalid Transaction Number " + tranNumberForLog + "The From Details invalid " + from.getTransactionType());
                }
                xmlTransaction.setTFrom(tFrom);
            }
        } else {
            LOG.error("Transaction from details not found ");
            errorsLog.add("Transaction from details not found");
        }
    }

    private void setTransactionLevelData(Report.Transaction xmlTransaction, TransactionDTO transaction) {
        if (transaction.getTransactionNumber() != null) {
            xmlTransaction.setTransactionnumber(transaction.getTransactionNumber());//M
        } else {
            LOG.error("Value not found for Transaction Number. Transaction_UID " + transaction.getTran_uid());
            errorsLog.add("Value not found for Transaction Number. Transaction_UID " + transaction.getTran_uid());
        }
        if (transaction.getInternalRefNumber() != null) {
            xmlTransaction.setInternalRefNumber(transaction.getInternalRefNumber());//N
        } else {
            LOG.warn("Value not found for Internal Reference Number. Transaction Number " + transaction.getTransactionNumber());
            warnings.add("Value not found for Internal Reference Number. Transaction_UID " + transaction.getTransactionNumber());
        }
        if (transaction.getTransModeCode() != null) {
            xmlTransaction.setTransmodeCode(transaction.getTransModeCode());
            if (applicationProperties.getTransModeCodeValidator().equalsIgnoreCase(transaction.getTransModeCode())) {
                if (transaction.getTransactionLocation() != null) {
                    xmlTransaction.setTransactionLocation(transaction.getTransactionLocation());//Conditionally
                } else {
                    LOG.warn("Value not found for Transaction Location. Transaction_UID " + transaction.getTransactionNumber());
                    warnings.add("Value not found for Transaction Location. Transaction_UID " + transaction.getTransactionNumber());
                }
            }
        } else {
            LOG.error("Value not found for Transaction Mode Code. Transaction_UID " + transaction.getTransactionNumber());
            errorsLog.add("Value not found for Transaction Mode Code. Transaction_UID " + transaction.getTransactionNumber());
        }
        if (transaction.getTransactionDescription() != null) {
            xmlTransaction.setTransactionDescription(transaction.getTransactionDescription());//M
        } else {
            LOG.error("Value not found for Transaction Description. Transaction_UID " + transaction.getTransactionNumber());
            errorsLog.add("Value not found for Transaction Description. Transaction_UID " + transaction.getTransactionNumber());
        }

        if (transaction.getAmountLocal() != null) {
            xmlTransaction.setAmountLocal(new BigDecimal(transaction.getAmountLocal()).setScale(2, BigDecimal.ROUND_DOWN));
        } else {
            LOG.error("Value not found for Amount Local : " + transaction.getDateTransaction() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Amount Local : " + transaction.getDateTransaction() + " Transaction Number  " + tranNumberForLog);
        }

        if (transaction.getValueDate() != null) {
            xmlTransaction.setValueDate(CalendarUtil.getXMLGregorianCalendarFromDate(transaction.getValueDate()));//M
        } else {
            LOG.error("Value not found for transaction number : " + transaction.getDateTransaction() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for transaction number : " + transaction.getDateTransaction() + " Transaction Number " + tranNumberForLog);
        }
        if (transaction.getDateTransaction() != null) {
            xmlTransaction.setDateTransaction(CalendarUtil.getXMLGregorianCalendarFromDate(transaction.getDateTransaction()));//M
        } else {
            LOG.error("No date transaction found for Date Transaction. Transaction number : " + transaction.getTransactionNumber() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("No date transaction found for Date Transaction. Transaction number : " + transaction.getTransactionNumber() + " Transaction Number " + tranNumberForLog);
        }
    }

    private void setReportLevelData(Report report, ReportDTO reportDTO) {
        if (reportDTO.getrEntityId() != null) {
            report.setRentityId(reportDTO.getrEntityId());
        } else {
            LOG.error("Value not found for REntityId.  Please set REntityId value in Application Property File.");
            errorsLog.add("Value not found for REntityId.  Please set REntityId value in Application Property File.");
        }
        if (reportDTO.getSubmissionCode() != null) {
            report.setSubmissionCode(SubmissionType.valueOf(reportDTO.getSubmissionCode()));//M
        } else {
            LOG.error("No value date found for Submission Date.  Please set Submission value in Application Property File.");
            errorsLog.add("No value date found for Submission Date.  Please set Submission value in Application Property File.");
        }
        if (reportDTO.getReportCode() != null) {
            report.setReportCode(ReportType.valueOf(reportDTO.getReportCode()));//M
        } else {
            LOG.error("Value not found for Report Code. Please set recordCounter value in Application Property File.");
            errorsLog.add("Value not found for Report Code.  Please set Report Code value in Application Property File.");
        }
        if (reportDTO.getSubmissionDate() != null) {
            report.setSubmissionDate(CalendarUtil.getXMLGregorianCalendarFromString(applicationProperties.getSubmissionDate()));//M
        } else {
            LOG.error("Value not found for Submission Date. Please set Submission Date value in Application Property File.");
            errorsLog.add("Value not found for Submission Date.  Please set Submission Date value in Application Property File.");
        }

        if (reportDTO.getCurrencyCodeLocal() != null) {
            report.setCurrencyCodeLocal(reportDTO.getCurrencyCodeLocal());//M
        } else {
            LOG.error("Value not found for Currency Local Code. Please set Currency Local Code value in Application Property File.");
            errorsLog.add("Value not found for Currency Local Code.  Please set Currency Local Code value in Application Property File.");
        }
        if (reportDTO.getEntityReference() != null) {
            report.setEntityReference(applicationProperties.getEntityReference());
        } else {
            LOG.error("Value not found for Entity Reference. Please set Entity Reference value in Application Property File.");
            errorsLog.add("Value not found for Entity Reference.  Please set Entity Reference value in Application Property File.");
        }
    }

    //To map non client person to TPerson xml object
    private TPerson getTPerson(ObjectFactory factory, FromToMappingDTO to) {
        PersonNonClientDTO personNonClient = to.getPersonNonClientDTO();
        TPerson tPerson = factory.createTPerson();
        tPerson.setGender(personNonClient.getGender());
        if (personNonClient.getTitle() != null) {
            tPerson.setTitle(factory.createTPersonTitle(personNonClient.getTitle()));
        }
        if(personNonClient.getFirstName()!=null){
            tPerson.setFirstName(personNonClient.getFirstName());
        }else {
            LOG.error("Value not found for First Name. Please set Entity Reference value in Application Property File. Transaction Number "+tranNumberForLog);
            errorsLog.add("Value not found for First Name.  Please set Entity Reference value in Application Property File. Transaction Number "+tranNumberForLog);
        }

        if(personNonClient.getLastName()!=null){
            tPerson.setLastName(personNonClient.getLastName());
        }else {
            LOG.error("Value not found for Last Name. Please set Entity Reference value in Application Property File. Transaction Number "+tranNumberForLog);
            errorsLog.add("Value not found for Last Name.  Please set Entity Reference value in Application Property File. Transaction Number "+tranNumberForLog);
        }

        if (personNonClient.getBirthDate() != null) {
            tPerson.setBirthdate(CalendarUtil.getXMLGregorianCalendarFromDate(personNonClient.getBirthDate()));
        }
        tPerson.setIdNumber(personNonClient.getIdNumber());
        return tPerson;
    }

    //To map Account my client to xml TAccountMyClient
    private TAccountMyClient getTAccountMyClient(ObjectFactory factory, FromToMappingDTO from) {

        AccountsDTO accounts = from.getAccountsDTO();
        TAccountMyClient tAccountMyClient = factory.createTAccountMyClient();

        if (accounts.getInstituationName() != null) {
            tAccountMyClient.setInstitutionName(accounts.getInstituationName());
        } else {
            LOG.error("Value not found for Institution Name. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            errorsLog.add("Value not found for Institution Name. Account Number : " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if (accounts.getInstitutionCode() != null) {
            tAccountMyClient.setInstitutionCode(accounts.getInstitutionCode());
        } else {
            LOG.warn("Value not found for Institution Code. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            warnings.add("Value not found for Institution Code. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if (accounts.getSwift() != null) {
            tAccountMyClient.setSwift(accounts.getSwift());
        } else {
            LOG.warn("Value not found for Swift. Account Number: " + from.getAccountsDTO().getAccount() + "Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            warnings.add("Value not found for Swift. Account Number: " + from.getAccountsDTO().getAccount() + "Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if(accounts.getSwift()==null && accounts.getInstitutionCode()==null){
            LOG.error("Value not found for Swift and Institution Code. Account Number: " + from.getAccountsDTO().getAccount() + "Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            errorsLog.add("Value not found for Swift and Institution Code. Account Number: " + from.getAccountsDTO().getAccount() + "Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if (accounts.getAccount() != null) {
            tAccountMyClient.setAccount(accounts.getAccount());
        } else {
            LOG.error("Value not found for Account. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            errorsLog.add("Value not found for Account. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if (accounts.getBranch() != null) {
            tAccountMyClient.setBranch(accounts.getBranch());
        } else {
            LOG.error("Value not found for Branch. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            errorsLog.add("Value not found for Branch. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }

        if (accounts.getCurrencyCode() != null) {
            tAccountMyClient.setCurrencyCode(accounts.getCurrencyCode());
        } else {
            LOG.error("Value not found for Currency Code. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            errorsLog.add("Value not found for Currency Code. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }

        if (accounts.getAccountName() != null) {
            tAccountMyClient.setAccountName(accounts.getAccountName());
        } else {
            LOG.warn("Value not found for Account Name. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            warnings.add("Value not found for Account Name. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if(accounts.getPersonalAccountType()!=null){
            tAccountMyClient.setPersonalAccountType(accounts.getPersonalAccountType());
        }else{
            LOG.error("Value not found for Personal Account Type. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            errorsLog.add("Value not found for Personal Account Type. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }

        if (from.getAccountsDTO().getEntitiesDTO() != null) {
            FromToMappingDTO aTemp = from;
            aTemp.setEntitiesDTO(accounts.getEntitiesDTO());
            tAccountMyClient.setTEntity(getTEntityMyClient(factory, aTemp));
        }
        if (accounts.getOpened() != null) {
            tAccountMyClient.setOpened(CalendarUtil.getXMLGregorianCalendarFromDate(accounts.getOpened()));
        } else {
            LOG.warn("Value not found for Opened Date. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            warnings.add("Value not found for  Opened Date. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if (accounts.getClosed() != null) {
            tAccountMyClient.setClosed(CalendarUtil.getXMLGregorianCalendarFromDate(accounts.getClosed()));
        }
        if (accounts.getStatusCode() != null) {
            tAccountMyClient.setStatusCode(accounts.getStatusCode());
        } else {
            LOG.error("Value not found for Opened Date. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            errorsLog.add("Value not found for  Opened Date. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if (accounts.getBeneficiary() != null) {
            tAccountMyClient.setBeneficiary(factory.createTAccountBeneficiary(accounts.getBeneficiary()));
        } else {
            LOG.warn("Value not found for Beneficiary. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            warnings.add("Value not found for  Beneficiary. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }
        if (accounts.getBeneficiaryComment() != null) {
            tAccountMyClient.setBeneficiaryComment(factory.createTAccountBeneficiaryComment(accounts.getComments()));
        } else {
            LOG.warn("Value not found for Beneficiary Comment. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            warnings.add("Value not found for  Beneficiary Comment. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }

        if (accounts.getComments() != null) {
            tAccountMyClient.setComments(accounts.getComments());
        } else {
            LOG.warn("Value not found for Comments. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
            warnings.add("Value not found for  Comments. Account Number: " + from.getAccountsDTO().getAccount() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
        }

        if (accounts.getRelatedPartyDTO() != null) {
            Iterator<RelatedPartyDTO> relatedPartyIterator = accounts.getRelatedPartyDTO().iterator();

            while (relatedPartyIterator.hasNext()) {

                RelatedPartyDTO relatedParty = relatedPartyIterator.next();

                if (personMap.containsKey(relatedParty.getCifId())) {
                    PersonDTO person = personMap.get(relatedParty.getCifId());
                    if (person.getCifId() != null) {
                        TAccountMyClient.Signatory signatory = factory.createTAccountMyClientSignatory();
                        signatory.setRole(relatedParty.getRole());
                        signatory.setIsPrimary(relatedParty.getIsPrimary());
                        if (addressMap.containsKey(person.getCifId())) {
                            person.setAddressDTO(addressMap.get(person.getCifId()));
                        } else {
                            LOG.error("Value not found for Address. CIF_ID: " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                            errorsLog.add("Value not found for  Address. CIF_ID : " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                        }
                        if (phoneMap.containsKey(person.getCifId())) {
                            person.setPhoneDTO(phoneMap.get(person.getCifId()));
                        } else {
                            LOG.warn("Value not found for Phone. CIF_ID: " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                            warnings.add("Value not found for  Phone. CIF_ID : " + from.getCifId() + " Transaction Number " + tranNumberForLog + " Transaction Type " + from.getTransactionType());
                        }
                        TPersonMyClient tPersonMyClient = getTPersonMyClient(factory, person);

                        if (person.getPhoneDTO() != null) {
                            Iterator<PhoneDTO> phoneIterator = person.getPhoneDTO().iterator();
                            TPersonMyClient.Phones phones = factory.createTPersonMyClientPhones();
                            setPhoneToXML(factory, phoneIterator, phones.getPhone(), person.getCifId());
                            tPersonMyClient.setPhones(phones);
                        }

                        if (person.getAddressDTO() != null) {
                            TPersonMyClient.Addresses addresses = factory.createTPersonMyClientAddresses();
                            Iterator<AddressDTO> addressIterator = person.getAddressDTO().iterator();
                            setAddressToXML(factory, addressIterator, addresses.getAddress(), from.getCifId());
                        }

                        signatory.setTPerson(tPersonMyClient);
                        tAccountMyClient.getSignatory().add(signatory);
                    } else {
                        LOG.error("Valid Person Not found for the Signatory CIF ID " + from.getCifId() + " Transaction Number " + tranNumberForLog);
                        errorsLog.add("Valid Person Not found for the Signatory CIF ID " + from.getCifId() + " Transaction Number " + tranNumberForLog);
                    }
                } else {
                    LOG.error("Valid Person Not found for the Signatory CIF ID " + from.getCifId() + " Transaction Number " + tranNumberForLog);
                    errorsLog.add("Valid Person Not found for the Signatory CIF ID " + from.getCifId() + " Transaction Number " + tranNumberForLog);

                }
            }
        }


        return tAccountMyClient;
    }

    private TEntityMyClient getTEntityMyClient(ObjectFactory factory, FromToMappingDTO fromToMappingDTO) {

        EntitiesDTO entities = fromToMappingDTO.getEntitiesDTO();
        TEntityMyClient tEntityMyClient = factory.createTEntityMyClient();

        if (entities.getName() != null) {
            tEntityMyClient.setName(entities.getName());
        } else {
            LOG.error("Value not found for Name. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Name CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getCommercialName() != null) {
            tEntityMyClient.setCommercialName(entities.getCommercialName());
        } else {
            LOG.warn("Value not found for Commercial Name. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for getCommercial Name. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getIncorporationLegalForm() != null) {
            tEntityMyClient.setIncorporationLegalForm(entities.getIncorporationLegalForm());
        } else {
            LOG.error("Value not found for Incorporation Legal Form. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Incorporation Legal Form. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getIncorporationNumber() != null) {
            tEntityMyClient.setIncorporationNumber(entities.getIncorporationNumber());
        } else {
            LOG.warn("Value not found for Incorporation Number. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Incorporation Number. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getBusiness() != null) {
            tEntityMyClient.setBusiness(entities.getBusiness());
        } else {
            LOG.error("Value not found for Business. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Business CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getPhoneDTO() != null) {
            Iterator<PhoneDTO> phoneIterator = entities.getPhoneDTO().iterator();
            TEntityMyClient.Phones phones = factory.createTEntityMyClientPhones();
            setPhoneToXML(factory, phoneIterator, phones.getPhone(), entities.getCifId());
            tEntityMyClient.setPhones(phones);
        } else {
            LOG.error("Value not found for Address. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Address CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getAddressDTO() != null) {
            TEntityMyClient.Addresses addresses = factory.createTEntityMyClientAddresses();
            Iterator<AddressDTO> addressIterator = entities.getAddressDTO().iterator();
            setAddressToXML(factory, addressIterator, addresses.getAddress(), fromToMappingDTO.getCifId());
            tEntityMyClient.setAddresses(addresses);
        } else {
            LOG.warn("Value not found for Phone. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Phone. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }

        if (entities.getEmail() != null) {
            tEntityMyClient.setEmail(entities.getEmail());
        } else {
            LOG.warn("Value not found for Email. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Email. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }

        if (entities.getIncorporationCountryCode() != null) {
            tEntityMyClient.setIncorporationCountryCode(entities.getIncorporationCountryCode());
        } else {
            LOG.error("Value not found for Incorporation Country Code. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Incorporation Country Code. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getIncorporationDate() != null) {
            tEntityMyClient.setIncorporationDate(CalendarUtil.getXMLGregorianCalendarFromDate(entities.getIncorporationDate()));
        } else {
            LOG.warn("Value not found for Incorporation Date. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Incorporation Date. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getBusinessClosed() != null) {
            tEntityMyClient.setBusinessClosed(Boolean.valueOf(entities.getBusinessClosed()));
        } else {
            LOG.warn("Value not found for Business Closed. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Business Closed. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getDateBusinessClosed() != null) {
            tEntityMyClient.setDateBusinessClosed(CalendarUtil.getXMLGregorianCalendarFromDate(entities.getDateBusinessClosed()));
        } else {
            LOG.warn("Value not found for Date Business Closed. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Date Business Closed. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getTaxNumber() != null) {
            tEntityMyClient.setTaxNumber(entities.getTaxNumber());
        } else {
            LOG.warn("Value not found for Date Business Closed. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Date Business Closed. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getTaxRegNumber() != null) {
            tEntityMyClient.setTaxRegNumber(entities.getTaxRegNumber());
        } else {
            LOG.warn("Value not found for Tax Reg Number. CIF ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Tax Reg Number. CIF_ID " + fromToMappingDTO.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (entities.getComments() != null) {
            tEntityMyClient.setComments(entities.getComments());
        }
        return tEntityMyClient;
    }

    private void setAddressToXML(ObjectFactory factory, Iterator<AddressDTO> addressIterator, List<TAddress> address2, String cifId) {
        while ((addressIterator.hasNext())) {
            TAddress tAddress = factory.createTAddress();
            AddressDTO address = addressIterator.next();
            if (address.getAddressType() != null) {
                tAddress.setAddressType(address.getAddressType());
            } else {
                LOG.error("Value not found for Address Type. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Address Type. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (address.getAddress() != null) {
                tAddress.setAddress(address.getAddress());
            } else {
                LOG.error("Value not found for Address. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Address. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (address.getCountryCode() != null) {
                tAddress.setCountryCode(address.getCountryCode());
            } else {
                LOG.error("Value not found for Country Code. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Country Code. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (address.getCity() != null) {
                tAddress.setCity(address.getCity());
            } else {
                LOG.error("Value not found for City. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for City. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (address.getTown() != null) {
                tAddress.setTown(address.getTown());
            } else {
                LOG.warn("Value not found for Town. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Town. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (address.getZip() != null) {
                tAddress.setZip(address.getZip());
            } else {
                LOG.warn("Value not found for Zip. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Zip. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (address.getState() != null) {
                tAddress.setState(address.getState());
            } else {
                LOG.warn("Value not found for State. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for State. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (address.getComments() != null) {
                tAddress.setComments(address.getComments());
            } else {
                LOG.warn("Value not found for Comments. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Comments. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            address2.add(tAddress);
        }
    }

    private TPersonMyClient getTPersonMyClient(ObjectFactory factory, PersonDTO person) {
        TPersonMyClient tPersonMyClient = factory.createTPersonMyClient();

        tPersonMyClient.setGender(person.getGender());//n
        if (person.getTitle() != null) {
            tPersonMyClient.setTitle(factory.createTPersonTitle(person.getTitle()));
        }else {
            LOG.error("Value not found for Title. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Title. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getFirstName() != null) {
            tPersonMyClient.setFirstName(person.getFirstName());//M
        }else {
            LOG.error("Value not found for First Name. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for First Name. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getLastName() != null) {
            tPersonMyClient.setLastName(person.getLastName());//M
        }else {
            LOG.error("Value not found for Last Name. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Last Name. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getBirthDate() != null) {
            tPersonMyClient.setBirthdate(CalendarUtil.getXMLGregorianCalendarFromDate(person.getBirthDate()));//M
        }else {
            LOG.error("Value not found for Birth Date. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Birth Date. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getNationality1() != null) {
            tPersonMyClient.setNationality1(person.getNationality1());
        }else {
            LOG.error("Value not found for Nationality. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Nationality. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if ("LK".equals(person.getNationality1())) {
            tPersonMyClient.setIdNumber(person.getIdNumber());//Conditional
        }
        if (person.getNationality2() != null) {
            tPersonMyClient.setNationality2(person.getNationality2());
        }else {
            LOG.warn("Value not found for Nationality 2. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Nationality 2. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getResidence() != null) {
            tPersonMyClient.setResidence(person.getResidence());//M
        }else {
            LOG.warn("Value not found for Residence. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Residence. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }

        if (person.getPhoneDTO() != null) {
            Iterator<PhoneDTO> phoneIterator = person.getPhoneDTO().iterator();
            TPersonMyClient.Phones phones = factory.createTPersonMyClientPhones();

            setPhoneToXML(factory, phoneIterator, phones.getPhone(), person.getCifId());
            tPersonMyClient.setPhones(phones);
        }else {
            LOG.warn("Value not found for Phone. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Phone. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getAddressDTO() != null) {
            TPersonMyClient.Addresses addresses = factory.createTPersonMyClientAddresses();//M
            Iterator<AddressDTO> addressIterator = person.getAddressDTO().iterator();//M
            setAddressToXML(factory, addressIterator, addresses.getAddress(), person.getCifId());//M
            tPersonMyClient.setAddresses(addresses);
        }else {
            LOG.warn("Value not found for Address. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Address. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getPersonIdentificationDTO() != null) {
            setTPersonalIdentification(factory, person, tPersonMyClient);//Conditionally if not a sri lankan
        } else {
            LOG.error("Data not found for Personal Identification. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Data not found for Personal Identification. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }

        if (person.getEmail() != null) {
            tPersonMyClient.getEmail().add(person.getEmail());//The actual email format should be a list
        }else {
            LOG.warn("Value not found for Email. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            warnings.add("Value not found for Email. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getOccupation() != null) {
            tPersonMyClient.setOccupation(person.getOccupation());//M
        }else {
            LOG.error("Value not found for Occupation. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            errorsLog.add("Value not found for Occupation. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
        }
        if (person.getComments() != null) {
            tPersonMyClient.setComments(person.getComments());
        }
        return tPersonMyClient;
    }

    private void setTPersonalIdentification(ObjectFactory factory, PersonDTO person, TPersonMyClient tPersonMyClient) {
        Iterator<PersonIdentificationDTO> personicaIdentiIterator = person.getPersonIdentificationDTO().iterator();
        TPersonIdentification tPersonIdentification = factory.createTPersonIdentification();

        while (personicaIdentiIterator.hasNext()) {
            PersonIdentificationDTO personIdentification = personicaIdentiIterator.next();
            /*if(addedIdentity.contains(personIdentification.getCifId())){
                continue;
            }*/

            if (personIdentification.getIdentificationType() != null) {
                tPersonIdentification.setType(personIdentification.getIdentificationType());
            } else {
                LOG.error("Value not found for Identification Type. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Identification Type. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            }
            if (personIdentification.getNumber() != null) {
                tPersonIdentification.setNumber(personIdentification.getNumber());
            } else {
                LOG.error("Value not found for Identification Number. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Identification Number. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            }
            if (personIdentification.getIssueCountry() != null) {
                tPersonIdentification.setIssueCountry(personIdentification.getIssueCountry());
            } else {
                LOG.error("Value not found for Identification Issue Country. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Identification Issue Country. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            }
            if (personIdentification.getIssueDate() != null) {
                tPersonIdentification.setIssueDate(CalendarUtil.getXMLGregorianCalendarFromDate(personIdentification.getIssueDate()));
            } else {
                LOG.warn("Value not found for Identification Issue Date. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Identification Issue Date. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            }
            if (personIdentification.getExpiryDate() != null) {
                tPersonIdentification.setExpiryDate(CalendarUtil.getXMLGregorianCalendarFromDate(personIdentification.getExpiryDate()));
            } else {
                LOG.warn("Value not found for Identification Expiry Date. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Identification Expiry Date. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            }
            if (personIdentification.getIssuedBy() != null) {
                tPersonIdentification.setIssuedBy(personIdentification.getIssuedBy());
            } else {
                LOG.warn("Value not found for Identification Issue Country. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Identification Issue Country. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            }
            if (personIdentification.getComment() != null) {
                tPersonIdentification.setComments(personIdentification.getComment());
            } else {
                LOG.warn("Value not found for Identification Comment. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Identification Comment. CIF ID " + person.getCifId() + " Transaction Number " + tranNumberForLog);
            }

            tPersonMyClient.getIdentification().add(tPersonIdentification);
        }
    }

    private void setPhoneToXML(ObjectFactory factory, Iterator<PhoneDTO> phoneIterator, List<TPhone> phone, String cifId) {
        while (phoneIterator.hasNext()) {
            TPhone tPhone = factory.createTPhone();
            PhoneDTO aPhone = phoneIterator.next();

            if (aPhone.getContactType() != null) {
                tPhone.setTphContactType(aPhone.getContactType());
            } else {
                LOG.error("Value not found for Contact Type. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Zip. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (aPhone.getCommunicationType() != null) {
                tPhone.setTphCommunicationType(aPhone.getCommunicationType());
            } else {
                LOG.error("Value not found for Communication Type. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Communication Type. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (aPhone.getCountryPrefix() != null) {
                tPhone.setTphCountryPrefix(aPhone.getCountryPrefix());
            } else {
                LOG.error("Value not found for Country Prefix. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Country Prefix. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (aPhone.getPhoneNumber() != null) {
                tPhone.setTphNumber(aPhone.getPhoneNumber());
            } else {
                LOG.error("Value not found for Phone Number. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                errorsLog.add("Value not found for Phone Number. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            if (aPhone.getComments() != null) {
                tPhone.setComments(aPhone.getComments());
            } else {
                LOG.warn("Value not found for Phone Comments. Type. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
                warnings.add("Value not found for Phone Comments. CIF ID " + cifId + " Transaction Number " + tranNumberForLog);
            }
            phone.add(tPhone);
        }
    }

    private ReportDTO getATransactionForReportDTO(Integer transactionNumber) {

        List<FromToMappingDTO> fromToMappingDTO = fromToMappingDTOMap.get(transactionNumber.toString());

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setrEntityId(applicationProperties.getPublicREntityId());
        reportDTO.setSubmissionCode(applicationProperties.getSubmissionCode());
        reportDTO.setReportCode(applicationProperties.getReportCode());
        reportDTO.setEntityReference(applicationProperties.getEntityReference());
        try {
            final Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(applicationProperties.getSubmissionDate());
            reportDTO.setSubmissionDate(date);
        } catch (Exception e) {
            LOG.error("Could not parse the date ", e);
            errorsLog.add("Could not parse the date " + e.toString());
        }

        reportDTO.setCurrencyCodeLocal(applicationProperties.getCurrencyLocalCode());
        reportDTO.setTransactionDTO(transactionsMap.get(transactionNumber));

        Iterator<FromToMappingDTO> fromToMappingDTOIterator = fromToMappingDTO.iterator();
        while (fromToMappingDTOIterator.hasNext()) {
            FromToMappingDTO fromToMapping = fromToMappingDTOIterator.next();
            if (fromToMapping.getTransactionType().equals(applicationProperties.fromMyClient)
                    || fromToMapping.getTransactionType().equals(applicationProperties.fromNonClient)) {
                reportDTO.setTransactionFrom(fromToMapping);
                setValidLevels(reportDTO, reportDTO.getTransactionFrom());
            } else {
                reportDTO.setTransactionTo(fromToMapping);
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
            successLog.add("Person Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());
            LOG.info("Person Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());
            if (transactionFromOrTo.getCifId() != null && personMap.containsKey(transactionFromOrTo.getCifId())) {
                transactionFromOrTo.setPersonDTO(personMap.get(transactionFromOrTo.getCifId()));
                if (personalIdentificationMap.containsKey(transactionFromOrTo.getCifId())) {
                    transactionFromOrTo.getPersonDTO().setPersonIdentificationDTO(personalIdentificationMap.get(transactionFromOrTo.getCifId()));
                } else {
                    LOG.error("Transaction CIF_ID : " + transactionFromOrTo.getCifId() + " Personal Identification Details not found. Transaction Number " + tranNumberForLog);
                    errorsLog.add("Transaction CIF_ID : " + transactionFromOrTo.getCifId() + " Personal Identification Details not found. Transaction Number " + tranNumberForLog);
                }
                if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                    transactionFromOrTo.getPersonDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }
                if (transactionFromOrTo.getCifId() != null && addressMap.containsKey(transactionFromOrTo.getCifId())) {
                    transactionFromOrTo.getPersonDTO().setAddressDTO(addressMap.get(transactionFromOrTo.getCifId()));
                }


            } else {
                errorsLog.add("Person Level  Details Not Found, Type : " + transactionFromOrTo.getTransactionType() + " CIF_ID " + transactionFromOrTo.getCifId() + " Transaction Number " + tranNumberForLog);
                LOG.error("Person Level  Details Not Found, Type : " + transactionFromOrTo.getTransactionType() + " CIF_ID " + transactionFromOrTo.getCifId() + " Transaction Number " + tranNumberForLog);
            }


        } else if (transactionFromOrTo.getAcctNumber() == null && entitiesMap.containsKey(transactionFromOrTo.getCifId())) {

            successLog.add("Entity Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType() + " Transaction Number " + tranNumberForLog);
            LOG.info("Entity Level Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType() + " Transaction Number " + tranNumberForLog);
            if (transactionFromOrTo.getCifId() != null) {
                transactionFromOrTo.setEntitiesDTO(entitiesMap.get(transactionFromOrTo.getCifId()));
                if (transactionFromOrTo.getCifId() != null && phoneMap.containsKey(transactionFromOrTo.getCifId())) {
                    transactionFromOrTo.getEntitiesDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }
            } else {
                errorsLog.add("Entity Level Details Not Found. Type : " + transactionFromOrTo.getTransactionType() + " CIF_ID " + transactionFromOrTo.getCifId() + " Transaction Number " + tranNumberForLog);
                LOG.error("Entity Level Details Not Found. Type : " + transactionFromOrTo.getTransactionType() + " CIF_ID " + transactionFromOrTo.getCifId() + " Transaction Number " + tranNumberForLog);
            }
            if (transactionFromOrTo.getCifId() != null && addressMap.containsKey(transactionFromOrTo.getCifId())) {
                transactionFromOrTo.getEntitiesDTO().setAddressDTO(addressMap.get(transactionFromOrTo.getCifId()));
            }

        } else if (transactionFromOrTo.getAcctNumber() != null) {

            successLog.add("Account Level Transaction No : " + tranNumberForLog + " Type : " + transactionFromOrTo.getTransactionType());
            LOG.info("Account Level Transaction No : " + tranNumberForLog + " Type : " + transactionFromOrTo.getTransactionType());
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
                        errorsLog.add("Transaction CIF_ID : " + transactionFromOrTo.getCifId() + " Personal Identification Details not found. Transaction Number " + tranNumberForLog);
                        LOG.error("Transaction CIF_ID : " + transactionFromOrTo.getCifId() + " Personal Identification Details not found. Transaction Number " + tranNumberForLog);
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
                if (transactionFromOrTo.getCifId() != null && relatedPartyMap.containsKey(transactionFromOrTo.getAcctNumber())) {
                    transactionFromOrTo.getAccountsDTO().setRelatedPartyDTO(relatedPartyMap.get(transactionFromOrTo.getAcctNumber()));
                }
            } else {
                errorsLog.add("Account Details not found, Account Number : " + transactionFromOrTo.getAcctNumber());
                LOG.error("Account Details not found, Account Number : " + transactionFromOrTo.getAcctNumber());

            }

        } else if (transactionFromOrTo.getCifId() == null) {

            if (personNonClientMap.containsKey(transactionNumber)) {

                LOG.info("Person Non Client Transaction No : " + tranNumberForLog + " Type : " + transactionFromOrTo.getTransactionType());
                successLog.add("Person Non Client Transaction No : " + tranNumberForLog + " Type : " + transactionFromOrTo.getTransactionType());

                transactionFromOrTo.setPersonNonClientDTO(personNonClientMap.get(transactionNumber));
                if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                    transactionFromOrTo.getPersonNonClientDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }

            } else if (entityNonClientMap.containsKey(transactionNumber.toString())) {
                LOG.info("Entity Non Client Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());
                successLog.add("Entity Non Client Transaction No : " + transactionNumber + " Type : " + transactionFromOrTo.getTransactionType());
                transactionFromOrTo.setEntityNonClientDTO(entityNonClientMap.get(transactionNumber.toString()));
                if (phoneMap.containsKey(transactionFromOrTo.getCifId()) && transactionFromOrTo.getCifId() != null) {
                    transactionFromOrTo.getEntityNonClientDTO().setPhoneDTO(phoneMap.get(transactionFromOrTo.getCifId()));
                }

            } else {

                errorsLog.add("Can't find the relevant Non Client Person or Non Client Entity for transaction number : " + tranNumberForLog);
                LOG.error("Can't find the relevant Non Client Person or Non Client Entity for transaction number : " + tranNumberForLog);
            }
        }
    }

}