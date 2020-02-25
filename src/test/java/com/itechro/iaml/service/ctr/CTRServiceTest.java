package com.itechro.iaml.service.ctr;

import com.itechro.iaml.exception.impl.AppsException;
import com.itechro.iaml.model.ctr.ReportDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class CTRServiceTest {

    @Autowired
    private CTRService ctrService;


    @Test
    void generateSampleReport() throws JAXBException, AppsException {
        ctrService.generateDummyReport();
    }

    @Test
    void generateWithActualData() throws JAXBException, AppsException {
        ctrService.generateSampleReport();
    }

   /* @Test
    void getReportList() {

        List<ReportDTO> reportDTOS = ctrService.getReportList();

        Assert.assertNull(reportDTOS);
    }*/
}
