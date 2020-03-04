package com.itechro.iaml.util;

import generated.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class XMLFileWriter {
    private static final Logger LOG = LoggerFactory.getLogger(XMLFileWriter.class);


    public static void writeReportXML(String path,String fileName, Report report) throws JAXBException {
        JAXBContext contextObj = JAXBContext.newInstance(Report.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        try {

            marshallerObj.marshal(report, new FileOutputStream(new File(path,fileName)));
        } catch (FileNotFoundException e) {
            LOG.error("ERROR: Generating xml file failed. {}", report.getRentityId());
        }
    }
}
