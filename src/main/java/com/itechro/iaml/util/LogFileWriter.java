package com.itechro.iaml.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class LogFileWriter {

    private static final Logger LOG = LoggerFactory.getLogger(LogFileWriter.class);

    public static void writeLogFile(int fileNumber, List<String> logData, String fileName, String path) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(path, fileName + fileNumber + ".txt"));
            for (String str : logData) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
        } catch (Exception e) {
            LOG.error("Couldn't create Success log file. fileNumber:{}, FileName:{}, FilePath:{}", fileNumber, fileName, path);
        }
    }

}
