package com.itechro.iaml.service.ctr;

import com.itechro.iaml.exception.impl.AppsException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBException;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class CTRServiceTest {

    @Autowired
    private CTRService ctrService;


    @Test
    void generateSampleReport() throws JAXBException, AppsException {
        ctrService.generateSampleReport();
    }
}
