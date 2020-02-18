package com.itechro.iaml.service.ctr;

import com.itechro.iaml.exception.impl.AppsException;
import com.itechro.iaml.util.CalendarUtil;
import com.itechro.iaml.util.XMLFileWriter;
import generated.ObjectFactory;
import generated.Report;
import generated.ReportType;
import generated.SubmissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

@Service
public class CTRService {

    private static final Logger LOG = LoggerFactory.getLogger(CTRService.class);


    public void generateSampleReport() throws JAXBException, AppsException {

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
        report.getTransaction().add(transaction1);

        XMLFileWriter.writeReportXML("sample.xml",report);
    }





}
