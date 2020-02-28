package com.itechro.iaml.dao.ctr;

import com.itechro.iaml.config.IAMLProperties;
import com.itechro.iaml.dao.BaseJDBCDao;
import com.itechro.iaml.model.ctr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class CTRJdbcDao extends BaseJDBCDao {

    @Autowired
    private IAMLProperties appProperties;
    private static final Logger LOG = LoggerFactory.getLogger(CTRJdbcDao.class);


    //To be deleted
    public TransactionDTO getTransaction(Integer transactionNumber) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L01_TRANSACTIONS");
        SQL.append(" WHERE TRAN_UID=");
        SQL.append(transactionNumber.toString());
        params.put("transactionNumber", transactionNumber);
        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<TransactionDTO>() {
            @Override
            public TransactionDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return getTransactionDTO(rs);
            }
        });
    }

    public HashMap<Integer, TransactionDTO> getTransactionsAsMap() {
        HashMap<Integer, TransactionDTO> results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L01_TRANSACTIONS");
        SQL.append(" WHERE REPORT_INDICATOR='");
        SQL.append(appProperties.getReportIndicator());
        SQL.append("' ORDER BY TRAN_UID");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<Integer, TransactionDTO>>() {
            @Override
            public HashMap<Integer, TransactionDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    TransactionDTO transactionDTO = getTransactionDTO(rs);
                    results.put(transactionDTO.getTran_uid(),transactionDTO);
                }
                LOG.info("getTransactionsAsMap() completed and loaded the Transactions");
                return results;
            }
        });
    }

public HashMap<String,List<FromToMappingDTO>> getFromToMappingRecordsAsMap() {
    HashMap<String,List<FromToMappingDTO>> results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM (");
        SQL.append("SELECT '")
                .append(appProperties.fromMyClient)
                .append("' AS TRANTYPE ,L02_FROM_MY_CLIENT.* FROM L02_FROM_MY_CLIENT UNION ");
        SQL.append("SELECT '")
                .append(appProperties.fromNonClient)
                .append("' AS TRANTYPE ,L02_FROM_NON_CLIENT.TRAN_UID,FROM_FUNDS_CODE," +
                        "FROM_FUNDS_COMMENT,FROM_FOREIGN_CURRENCY,FROM_COUNTRY,null as ACCT_NUMBER,null AS CIF_ID," +
                        "FOREIGN_CURRENCY_CODE,FOREIGN_AMOUNT,FOREIGN_EXCHANGE_RATE FROM L02_FROM_NON_CLIENT UNION ");
        SQL.append("SELECT '")
                .append(appProperties.toMyClient)
                .append("' AS TRANTYPE ,L02_TO_MY_CLIENT.* FROM L02_TO_MY_CLIENT UNION ");
        SQL.append("SELECT '")
                .append(appProperties.toNonMyClient)
                .append("' AS TRANTYPE ,L02_TO_NON_CLIENT.TRAN_UID,TO_FUNDS_CODE," +
                        "TO_FUNDS_COMMENT,TO_FOREIGN_CURRENCY,TO_COUNTRY,null as ACCT_NUMBER,null AS CIF_ID," +
                        "FOREIGN_CURRENCY_CODE,FOREIGN_AMOUNT,FOREIGN_EXCHANGE_RATE FROM L02_TO_NON_CLIENT");
        SQL.append(") ORDER BY TRAN_UID");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String,List<FromToMappingDTO>>>() {
            @Override
            public HashMap<String,List<FromToMappingDTO>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    FromToMappingDTO fromToMappingDTO = getFormToMappingDTO(rs);
                    putFromToDTOIntoHashMap(results,fromToMappingDTO.getTransId().toString(),fromToMappingDTO);

                }
                LOG.info("getFromToMappingRecordsAsMap() completed and loaded the From & To Transactions");
                return results;
            }
        });
    }


    public HashMap<String, AccountsDTO> getAccountsMap() {
        HashMap<String, AccountsDTO> results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L00_ACCOUNTS");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String, AccountsDTO>>() {
            @Override
            public HashMap<String, AccountsDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    AccountsDTO accountsDTO = getAccountsDTO(rs);
                    results.put(accountsDTO.getAcctNumber(),accountsDTO);
                }
                LOG.info("getAccountsMap() completed and loaded the Accounts");
                return results;
            }
        });
    }

    public  HashMap<String, EntitiesDTO> getEntitiesMap() {
        HashMap<String, EntitiesDTO> results=new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L00_ENTITIES");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String, EntitiesDTO> >() {
            @Override
            public HashMap<String, EntitiesDTO>  extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    EntitiesDTO entitiesDTO = getEntitiesDTO(rs);
                    results.put(entitiesDTO.getCifId(),entitiesDTO);
                }
                LOG.info("getEntitiesMap() completed and loaded the Entities");
                return results;
            }
        });
    }

    public HashMap<String,EntityNonClientDTO> getEntityNonClientList() {
        HashMap<String,EntityNonClientDTO> results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L00_ENTITY_NON_CLIENT");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String,EntityNonClientDTO>>() {
            @Override
            public HashMap<String,EntityNonClientDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    EntityNonClientDTO entityNonClientDTO = getEntityNonClientDTO(rs);
                    results.put(entityNonClientDTO.getTranUid(),entityNonClientDTO);
                }
                LOG.info("getEntityNonClientList() completed and loaded the Entity Non Clients");
                return results;
            }
        });
    }

    public HashMap<String, PersonDTO> getPersonMap() {
        HashMap<String, PersonDTO> results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L00_PERSON");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String, PersonDTO>>() {
            @Override
            public HashMap<String, PersonDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    PersonDTO personDTO = getPersonDTO(rs);
                    results.put(personDTO.getCifId(),personDTO);
                }
                LOG.info("getPersonMap() completed and loaded the Person");
                return results;
            }
        });
    }

    public HashMap<Integer,PersonNonClientDTO> getPersonNonClientMap() {
        HashMap<Integer,PersonNonClientDTO>results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L00_PERSON_NON_CLIENT");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<Integer,PersonNonClientDTO>>() {
            @Override
            public HashMap<Integer,PersonNonClientDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    PersonNonClientDTO personNonClientDTO = getPersonNonClientDTO(rs);
                    results.put(personNonClientDTO.getTranUid(),personNonClientDTO);
                }
                LOG.info("getPersonNonClientMap() completed and loaded the Person Non Clients");
                return results;
            }
        });
    }
    public HashMap<String,List<PhoneDTO>>  xx() {
        HashMap<String,List<PhoneDTO>>   results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L01_PHONE");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String,List<PhoneDTO>>>() {
            @Override
            public HashMap<String,List<PhoneDTO>>   extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    PhoneDTO phoneDTO = getPhoneDTO(rs);
                    putPhoneIntoHashMap(results,phoneDTO.getCifId(),phoneDTO);
                }
                LOG.info("getPhoneMap() completed and loaded the Phone Details ");
                return results;
            }
        });
    }
    public HashMap<String,List<AddressDTO>> getAddressMap() {
        HashMap<String,List<AddressDTO>> results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L01_ADDRESS");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String,List<AddressDTO>> >() {
            @Override
            public HashMap<String,List<AddressDTO>>  extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    AddressDTO addressDTO = getAddressDTO(rs);
                    putAddressIntoHashMap(results,addressDTO.getCifId(),addressDTO);
                }
                LOG.info("getAddressMap() completed and loaded the Address Details ");
                return results;
            }
        });
    }

    public HashMap<String,List<PersonIdentificationDTO>> getPersonIdentificationMap() {
        HashMap<String,List<PersonIdentificationDTO>> results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L01_PERSON_IDENTIFICATION");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String,List<PersonIdentificationDTO>>>() {
            @Override
            public HashMap<String,List<PersonIdentificationDTO>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    PersonIdentificationDTO personIdentificationDTO = getPersonIdentificationDTO(rs);
                    putPersonalIdentificationIntoHashMap(results,personIdentificationDTO.getCifId(),personIdentificationDTO);
                }
                LOG.info("getPersonIdentificationMap() completed and loaded the Personal Identification Details ");
                return results;
            }
        });
    }

    public HashMap<String,List<PhoneDTO>>  getPhoneMap() {
        HashMap<String,List<PhoneDTO>>   results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L01_PHONE");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String,List<PhoneDTO>>>() {
            @Override
            public HashMap<String,List<PhoneDTO>>   extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    PhoneDTO phoneDTO = getPhoneDTO(rs);
                    putPhoneIntoHashMap(results,phoneDTO.getCifId(),phoneDTO);
                }
                LOG.info("getPhoneMap() completed and loaded the Phone Details ");
                return results;
            }
        });
    }

    public HashMap<String,List<RelatedPartyDTO>> getRelatedPartyMap() {
        HashMap<String,List<RelatedPartyDTO>>  results = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT * FROM L02_RELATED_PARTY WHERE ACCT_NUMBER in (SELECT ACCT_NUMBER FROM L00_ACCOUNTS)");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new ResultSetExtractor<HashMap<String,List<RelatedPartyDTO>>>() {
            @Override
            public HashMap<String,List<RelatedPartyDTO>>  extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    RelatedPartyDTO relatedPartyDTO = getRelatedPartyDTO(rs);
                    putRelatedPartyIntoHashMap(results,relatedPartyDTO.getAccountNumber(),relatedPartyDTO);
                }
                LOG.info("getRelatedPartyMap() completed and loaded the Related Details ");
                return results;
            }
        });
    }

    private TransactionDTO getTransactionDTO(ResultSet rs) throws SQLException {

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setTran_uid(rs.getInt("TRAN_UID"));
        transactionDTO.setTransactionNumber(rs.getString("TRAN_NUMBER"));
        transactionDTO.setInternalRefNumber(rs.getString("I_REF_NUMBER"));
        transactionDTO.setTransactionLocation(rs.getString("TRAN_LOCATION"));
        transactionDTO.setTransactionDescription(rs.getString("TRAN_DESC"));
        transactionDTO.setDateTransaction(rs.getDate("TRAN_DATE"));
        transactionDTO.setValueDate(rs.getDate("VALUE_DATE"));
        transactionDTO.setTransModeCode(rs.getString("TRAN_MODE_CODE"));
        transactionDTO.setTransModeComment(rs.getString("TRAN_MODE_COMMENT"));
        transactionDTO.setAmt(rs.getInt("AMT"));
        transactionDTO.setTransCurrencyCode(rs.getString("TRAN_CRNCY_CODE"));
        transactionDTO.setRate(rs.getInt("RATE"));
        transactionDTO.setAmountLocal(rs.getDouble("LOCAL_AMT"));

        return transactionDTO;
    }

    //Returns the relevant ToFROM data by joining the FROM_xxx and To_xxx tables
    private FromToMappingDTO getFormToMappingDTO(ResultSet rs) throws SQLException {

        FromToMappingDTO fromToMappingDTO = new FromToMappingDTO();

        fromToMappingDTO.setTransactionType(rs.getString("TRANTYPE"));
        fromToMappingDTO.setTransId(rs.getInt("TRAN_UID"));
        fromToMappingDTO.setFundsCode(rs.getString("FROMMY_FUNDS_CODE"));
        fromToMappingDTO.setFundsComment(rs.getString("FROMMY_FUNDS_COMMENT"));
        fromToMappingDTO.setForeignCurrency(rs.getString("FROMMY_FOREIGN_CURRENCY"));
        fromToMappingDTO.setCountry(rs.getString("FROMMY_COUNTRY"));
        fromToMappingDTO.setAcctNumber(rs.getString("ACCT_NUMBER"));
        fromToMappingDTO.setCifId(rs.getString("CIF_ID"));
        fromToMappingDTO.setForeignCurrencyCode(rs.getString("FOREIGN_CURRENCY_CODE"));
        fromToMappingDTO.setForeignAmount(rs.getInt("FOREIGN_AMOUNT"));
        fromToMappingDTO.setForeignExchangeRate(rs.getInt("FOREIGN_EXCHANGE_RATE"));

        return fromToMappingDTO;
    }

    private AccountsDTO getAccountsDTO(ResultSet rs) throws SQLException {

        AccountsDTO accountsDTO = new AccountsDTO();

        accountsDTO.setAcctNumber(rs.getString("ACCT_NUMBER"));
        accountsDTO.setCifId(rs.getString("CIF_ID"));
        accountsDTO.setInstituationName(rs.getString("INSTITUATION_NAME"));
        accountsDTO.setInstitutionCode(rs.getString("INSTITUTION_CODE"));
        accountsDTO.setSwift(rs.getString("SWIFT"));
        accountsDTO.setBranch(rs.getString("BRANCH"));
        accountsDTO.setAccount(rs.getString("ACCOUNT"));
        accountsDTO.setCurrencyCode(rs.getString("CURRENCY_CODE"));
        accountsDTO.setAccountName(rs.getString("ACCOUNT_NAME"));
        accountsDTO.setPersonalAccountType(rs.getString("PERSONAL_ACCOUNT_TYPE"));
        accountsDTO.setStatusCode(rs.getString("STATUS_CODE"));
        accountsDTO.setBeneficiary(rs.getString("BENEFICIARY"));
        accountsDTO.setBeneficiaryComment(rs.getString("BENEFICIARY_COMMENT"));
        accountsDTO.setComments(rs.getString("COMMENTS"));
        accountsDTO.setOpened(rs.getDate("OPENED"));
        accountsDTO.setClosed(rs.getDate("CLOSED"));

        return accountsDTO;
    }

    private EntitiesDTO getEntitiesDTO(ResultSet rs) throws SQLException {

        EntitiesDTO entitiesDTO = new EntitiesDTO();

        entitiesDTO.setName(rs.getString("NAME"));
        entitiesDTO.setCommercialName(rs.getString("COMMERCIAL_NAME"));
        entitiesDTO.setIncorporationLegalForm(rs.getString("INCORPORATION_LEGAL_FORM"));
        entitiesDTO.setIncorporationNumber(rs.getString("INCORPORATION_NUMBER"));
        entitiesDTO.setBusiness(rs.getString("BUSINESS"));
        entitiesDTO.setEmail(rs.getString("EMAIL"));
        entitiesDTO.setIncorporationCountryCode(rs.getString("INCORPORATION_COUNTRY_CODE"));
        entitiesDTO.setIncorporationDate(rs.getDate("INCORPORATION_DATE"));
        entitiesDTO.setBusinessClosed(rs.getString("BUSINESS_CLOSED"));
        entitiesDTO.setDateBusinessClosed(rs.getDate("DATE_BUSINESS_CLOSED"));
        entitiesDTO.setTaxNumber(rs.getString("TAX_NUMBER"));
        entitiesDTO.setTaxRegNumber(rs.getString("TAX_REG_NUMBER"));
        entitiesDTO.setComments(rs.getString("COMMENTS"));
        entitiesDTO.setCifId(rs.getString("CIF_ID"));
        entitiesDTO.setCorpId(rs.getString("CORP_ID"));

        return entitiesDTO;
    }

    private EntityNonClientDTO getEntityNonClientDTO(ResultSet rs) throws SQLException {

        EntityNonClientDTO entityNonClientDTO = new EntityNonClientDTO();

        entityNonClientDTO.setCountryCode(rs.getString("COUNTRY_CODE"));
        entityNonClientDTO.setTranUid(rs.getString("TRAN_UID"));
        entityNonClientDTO.setName(rs.getString("NAME"));
        entityNonClientDTO.setIncorporationNumber(rs.getString("INCORPORATION_NUMBNER"));

        return entityNonClientDTO;
    }

    private PersonDTO getPersonDTO(ResultSet rs) throws SQLException {

        PersonDTO personDTO = new PersonDTO();

        personDTO.setCifId(rs.getString("CIF_ID"));
        personDTO.setGender(rs.getString("GENDER"));
        personDTO.setTitle(rs.getString("TITLE"));
        personDTO.setFirstName(rs.getString("FIRST_NAME"));
        personDTO.setMiddleName(rs.getString("MIDDLE_NAME"));
        personDTO.setPrefix(rs.getString("PREFIX"));
        personDTO.setLastName(rs.getString("LAST_NAME"));
        personDTO.setBirthDate(rs.getDate("BIRTHDATE"));
        personDTO.setIdNumber(rs.getString("ID_NUMBER"));
        personDTO.setNationality1(rs.getString("NATIONALITY1"));
        personDTO.setNationality2(rs.getString("NATIONALITY2"));
        personDTO.setResidence(rs.getString("RESIDENCE"));
        personDTO.setEmail(rs.getString("EMAIL"));
        personDTO.setOccupation(rs.getString("OCCUPATION"));
        personDTO.setComments(rs.getString("COMMENTS"));

        return personDTO;
    }

    private PersonNonClientDTO getPersonNonClientDTO(ResultSet rs) throws SQLException {

        PersonNonClientDTO personNonClientDTO = new PersonNonClientDTO();

        personNonClientDTO.setTranUid(rs.getInt("TRAN_UID"));
        personNonClientDTO.setGender(rs.getString("GERNDER"));
        personNonClientDTO.setTitle(rs.getString("TITLE"));
        personNonClientDTO.setFirstName(rs.getString("FIRST_NAME"));
        personNonClientDTO.setMiddleName(rs.getString("MIDDLE_NAME"));
        personNonClientDTO.setLastName(rs.getString("LAST_NAME"));
        personNonClientDTO.setBirthDate(rs.getDate("BIRTHDATE"));
        personNonClientDTO.setIdNumber(rs.getString("ID_NUMBER"));

        return personNonClientDTO;
    }

    private AddressDTO getAddressDTO(ResultSet rs) throws SQLException {

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setCifId(rs.getString("CIF_ID"));
        addressDTO.setAddressType(rs.getString("ADDRESS_TYPE"));
        addressDTO.setAddress(rs.getString("ADDRESS"));
        addressDTO.setTown(rs.getString("TOWN"));
        addressDTO.setCity(rs.getString("CITY"));
        addressDTO.setZip(rs.getString("ZIP"));
        addressDTO.setCountryCode(rs.getString("COUNTRY_CODE"));
        addressDTO.setState(rs.getString("STATE"));
        addressDTO.setComments(rs.getString("COMMENTS"));

        return addressDTO;
    }

    private PersonIdentificationDTO getPersonIdentificationDTO(ResultSet rs) throws SQLException {

        PersonIdentificationDTO personIdentificationDTO = new PersonIdentificationDTO();

        personIdentificationDTO.setCifId(rs.getString("CIF_ID"));
        personIdentificationDTO.setIdentificationType(rs.getString("TYPE"));
        personIdentificationDTO.setNumber(rs.getString("NUMBER"));
        personIdentificationDTO.setIssueDate(rs.getDate("ISSUE_DATE"));
        personIdentificationDTO.setExpiryDate(rs.getDate("EXPIRY_DATE"));
        personIdentificationDTO.setIssuedBy(rs.getString("ISSUED_BY"));
        personIdentificationDTO.setIssueCountry(rs.getString("ISSUE_COUNTRY"));
        personIdentificationDTO.setComment(rs.getString("COMMENT"));

        return personIdentificationDTO;
    }

    private PhoneDTO getPhoneDTO(ResultSet rs) throws SQLException {

        PhoneDTO phoneDTO = new PhoneDTO();

        phoneDTO.setCifId(rs.getString("CIF_ID"));
        phoneDTO.setContactType(rs.getString("CONTACT_TYPE"));
        phoneDTO.setCommunicationType(rs.getString("COMMUNICATION_TYPE"));
        phoneDTO.setCountryPrefix(rs.getString("COUNTRY_PREFIX"));
        phoneDTO.setComments(rs.getString("COMMENTS"));
        phoneDTO.setPhoneNumber(rs.getString("PH_NUMBER"));

        return phoneDTO;
    }

    private RelatedPartyDTO getRelatedPartyDTO(ResultSet rs) throws SQLException {

        RelatedPartyDTO relatedPartyDTO = new RelatedPartyDTO();

        relatedPartyDTO.setAccountNumber(rs.getString("ACCT_NUMBER"));
        relatedPartyDTO.setCifId(rs.getString("CIF_ID"));
        relatedPartyDTO.setIsPrimary(rs.getString("IS_PRIMARY"));
        relatedPartyDTO.setRole(rs.getString("ROLE"));

        return relatedPartyDTO;
    }
    public void putPhoneIntoHashMap(HashMap<String,List<PhoneDTO>> hashMap, String cifId, PhoneDTO PhoneNumber) {
        List<PhoneDTO> list = hashMap.get(cifId);
        if (list == null) {
            list = new ArrayList<PhoneDTO>();
            hashMap.put(cifId, list);
        }
        list.add(PhoneNumber);
    }
    public void putPersonalIdentificationIntoHashMap(HashMap<String,List<PersonIdentificationDTO>> hashMap, String cifId, PersonIdentificationDTO identification) {
        List<PersonIdentificationDTO> list = hashMap.get(cifId);
        if (list == null) {
            list = new ArrayList<PersonIdentificationDTO>();
            hashMap.put(cifId, list);
        }
        list.add(identification);
    }
    public void putAddressIntoHashMap(HashMap<String,List<AddressDTO>> hashMap, String cifId, AddressDTO address) {
        List<AddressDTO> list = hashMap.get(cifId);
        if (list == null) {
            list = new ArrayList<AddressDTO>();
            hashMap.put(cifId, list);
        }
        list.add(address);
    }
    public void putRelatedPartyIntoHashMap(HashMap<String,List<RelatedPartyDTO>> hashMap, String cifId, RelatedPartyDTO relatedParty) {
        List<RelatedPartyDTO> list = hashMap.get(cifId);
        if (list == null) {
            list = new ArrayList<RelatedPartyDTO>();
            hashMap.put(cifId, list);
        }
        list.add(relatedParty);
    }
    public void putFromToDTOIntoHashMap(HashMap<String,List<FromToMappingDTO>> hashMap, String cifId, FromToMappingDTO fromToMappingDTO) {
        List<FromToMappingDTO> list = hashMap.get(cifId);
        if (list == null) {
            list = new ArrayList<FromToMappingDTO>();
            hashMap.put(cifId, list);
        }
        list.add(fromToMappingDTO);
    }
}