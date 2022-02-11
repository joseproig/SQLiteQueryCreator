package Utils.CSV;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVLogicWrite {
    private CSVWriter writer;
    List<String[]> contentOfCSV;

    public CSVLogicWrite(String ubiOfCSV) {
        try {
            writer = new CSVWriter(new FileWriter(ubiOfCSV));
            contentOfCSV = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in CSV!");
        }
    }

    public void addHeader (String [] header) {
        addContent(header);
    }

    public void addContent (String [] rowToAdd) {
        contentOfCSV.add(rowToAdd);
    }

    public void finishAndCloseCSV () {
        writer.writeAll(contentOfCSV);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while closing CSV File");
        }
    }
}
