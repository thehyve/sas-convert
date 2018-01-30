/**
 * Copyright (c) 2015 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */
package thehyve.sas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

import com.epam.parso.Column;
import com.epam.parso.SasFileProperties;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.SasFileReaderImpl;

/**
 * Command-line utility to convert files in SAS7BDAT format to 
 * comma-separated format (CSV). 
 * Based on the Parso library ({@link http://lifescience.opensource.epam.com/parso.html})
 * and opencsv ({@link http://opencsv.sourceforge.net/})
 * 
 * @author gijs@thehyve.nl
 */
public class Convert {

    public static final String USAGE = "Usage: Convert <file.sas> [file.csv]\n\nOptions:\n\n-o, --only-column-names\n       Only write column names before data rows (default is to write three header lines: labels, names, and formats)";
    private static final Logger log = LoggerFactory.getLogger(Convert.class);

    public void convert(InputStream in, OutputStream out, boolean onlyColumnNames) throws IOException {
        Date start = new Date();
        SasFileReader reader = new SasFileReaderImpl(in);
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out));
        Object[] data;
        SasFileProperties properties = reader.getSasFileProperties();
        log.info("Reading file " + properties.getName());
        log.info(properties.getRowCount() + " rows.");
        List<Column> columns = reader.getColumns();
        String[] outData = new String[columns.size()];
        if (!onlyColumnNames) {
            // Writing column labels
            for(int i=0; i < columns.size(); i++) {
                outData[i] = columns.get(i).getLabel();
            }
            writer.writeNext(outData);
        }
        // Writing column names
        for(int i=0; i < columns.size(); i++) {
            outData[i] = columns.get(i).getName();
        }
        writer.writeNext(outData);
        if (!onlyColumnNames) {
            // Writing column format
            for(int i=0; i < columns.size(); i++) {
                outData[i] = columns.get(i).getFormat();
            }
            writer.writeNext(outData);
        }

        try {
            log.info("Writing data...");
            long rowCount = 0;
            while((data = reader.readNext()) != null) {
                assert(columns.size() == data.length);
                for(int i=0; i < data.length; i++) {
                    outData[i] = data[i] == null ? "" : data[i].toString();
                }
                writer.writeNext(outData);
                rowCount++;
            }
            log.info("Done writing data.");
            log.info(rowCount + " rows written.");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        Date end = new Date();
        log.info("Converting took {} seconds.", (end.getTime() - start.getTime())/1000);
    }
    
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "Help");
        options.addOption("o", "only-column-names", false, "Only column names");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cl = parser.parse(options, args);
            if (cl.hasOption("help")) {
                System.err.printf(USAGE + "\n");
                return;
            }
            List<String> argList = cl.getArgList();
            if (argList.size() < 1) {
                System.err.printf("Too few parameters.\n\n" + USAGE + "\n");
                return;
            }
            try {
                FileInputStream fin = new FileInputStream(argList.get(0));
                OutputStream fout;
                if (argList.size() > 1) {
                    String out_filename = argList.get(1);
                    log.info("Writing to file: {}", out_filename);
                    fout = new FileOutputStream(out_filename);
                } else {
                    log.info("Writing to stdout.");
                    fout = System.out;
                }
                Convert converter = new Convert();
                converter.convert(fin, fout, cl.hasOption("only-column-names"));
                fin.close();
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            System.err.printf(USAGE + "\n");
            e.printStackTrace();
            return;
        }
    }

}
