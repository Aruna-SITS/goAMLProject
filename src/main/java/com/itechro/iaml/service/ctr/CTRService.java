package com.itechro.iaml.service.ctr;

import com.itechro.iaml.config.IAMLProperties;
import com.itechro.iaml.dao.ctr.CTRJdbcDao;
import com.itechro.iaml.exception.impl.AppsException;
import com.itechro.iaml.util.CalendarUtil;
import generated.ObjectFactory;
import generated.Report;
import generated.ReportType;
import generated.SubmissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.Date;

@Service
public class CTRService {

    private static final Logger LOG = LoggerFactory.getLogger(CTRService.class);

    @Autowired
    private CTRJdbcDao ctrJdbcDao;

    //Injection for Application Properties
    @Autowired
    private IAMLProperties applicationProperties;

    public void generateDummyReport() throws JAXBException, AppsException {

        ObjectFactory factory = new ObjectFactory();
        Report report = factory.createReport();
        report.setRentityId(7278);
        report.setSubmissionCode(SubmissionType.E);
        report.setReportCode(ReportType.CTR);
        report.setSubmissionDate(CalendarUtil.toXMLGregorianCalendar(new Date()));
        report.setCurrencyCodeLocal("LKR");
        report.setEntityReference("");

        Report.Transaction transaction1 = factory.createReportTransaction();
        transaction1.setTransactionnumber("0121393120200101124540");
        Report.Transaction.TFromMyClient tFromMyClient1 = factory.createReportTransactionTFromMyClient();
        tFromMyClient1.setFromCountry("LK");
        tFromMyClient1.setFromFundsCode("CASH");

        transaction1.setTFromMyClient(tFromMyClient1);
        report.getTransaction().add(transaction1);

        Report.Transaction transaction2 = factory.createReportTransaction();
        transaction2.setTransactionnumber("0121393120200101124541");
        report.getTransaction().add(transaction2);

        //XMLFileWriter.writeReportXML("sample1.xml",report);
    }

    public void generateSampleReport() throws JAXBException, AppsException {

        JavaToXMLAdaptor adaptor = new JavaToXMLAdaptor();
        adaptor.setApplicationProperties(applicationProperties);
        adaptor.setCtrJdbcDao(this.ctrJdbcDao);
        adaptor.generateReport();
    }
}
