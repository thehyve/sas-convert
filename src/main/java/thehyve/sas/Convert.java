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

import au.com.bytecode.opencsv.CSVWriter;

import com.ggasoftware.parso.Column;
import com.ggasoftware.parso.SasFileProperties;
import com.ggasoftware.parso.SasFileReader;

public class Convert {

    public static final String USAGE = "Usage: Convert <file.sas> <file.csv>";
    Logger log = LoggerFactory.getLogger(getClass());
    
    public void convert(InputStream in, OutputStream out) throws IOException {
        Date start = new Date();
        SasFileReader reader = new SasFileReader(in);
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out));
        Object[] data;
        SasFileProperties properties = reader.getSasFileProperties();
        log.info("Reading file " + properties.getName());
        log.info(properties.getRowCount() + " rows.");
        List<Column> columns = reader.getColumns();
        String[] outData = new String[columns.size()];
        // Writing column labels
        for(int i=0; i < columns.size(); i++) {
            outData[i] = columns.get(i).getLabel();
        }
        writer.writeNext(outData);
        // Writing column names
        for(int i=0; i < columns.size(); i++) {
            outData[i] = columns.get(i).getName();
        }
        writer.writeNext(outData);
        // Writing column format
        for(int i=0; i < columns.size(); i++) {
            outData[i] = columns.get(i).getFormat();
        }
        writer.writeNext(outData);

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
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cl = parser.parse(options, args);
            if (cl.hasOption("help")) {
                System.out.printf(USAGE + "\n");
                return;
            }
            List<String> argList = cl.getArgList();
            if (argList.size() < 2) {
                System.out.printf("Too few parameters.\n" + USAGE + "\n");
                return;
            }
            try {
                FileInputStream fin = new FileInputStream(argList.get(0));
                FileOutputStream fout = new FileOutputStream(argList.get(1));
                Convert converter = new Convert();
                converter.convert(fin, fout);
                fin.close();
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            System.out.printf(USAGE + "\n");
            e.printStackTrace();
            return;
        }
    }

}
