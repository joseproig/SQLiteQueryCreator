package Utils;

import Controller.DBLogic.DBConnection;
import Model.Query.Select;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class SQLLiteUtils {
    public static String getSolutionForOneSelect(Select select) throws IOException {
        //ProcessBuilder builder = new ProcessBuilder("sqlite3", ".open " + DBConnection.getInstance("").getPathFile(),".mode column",TablesData.getInstance().getPossibleQueries().get(numQuestion-1).get(0).toString());
        StringBuilder answer = new StringBuilder("");
        //Hem de ficar la width minima de 10 en les columnes que es vaiguin a mostrar
        StringBuilder widthCommand = new StringBuilder(".width");
        for (String var : select.getColumnaResult().keySet()) {
            widthCommand.append(" 10");
        }
        ProcessBuilder builder = new ProcessBuilder("sqlite3", "", ".open " + DBConnection.getInstance("").getPathFile(), ".mode column", widthCommand.toString(), select.toString() + ";", ".exit");

        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            answer.append(line).append("\n");
        }
        p.destroy();
        return answer.toString();
    }

    public static void copyFileLocally(InputStream file, String outputFile) {
        File targetFile = new File(outputFile);
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = file.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(file);
            IOUtils.closeQuietly(outStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFileLocally (String outputFile) {
        File myObj = new File(outputFile);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        }
    }
}