package RestController;

import Controller.DBLogic.DBConnection;
import Controller.Logic.Context;
import Controller.Logic.InitializeState;
import Model.Columna;
import Model.ParametersOfQuestion.Question;
import Model.ParametersOfQuestion.TemplateQuestion;
import Model.ProgramConfig;
import Model.Query.Select;
import Model.RestAPI.Schema;
import Model.RestAPI.Solution;
import Model.TablesData;
import Model.Taula;
import Utils.SQLLiteUtils;
import Utils.XML.ConvertAnswerToXML;
import com.google.gson.Gson;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@RestController
public class RestAPIController {
    @PostMapping (value = "/answers")
    public Solution getAnswers(@RequestParam("config") String programConfigString, @RequestParam("file") MultipartFile file) {
        System.out.println("CONFIG:");
        System.out.println("________________________________________________________");
        System.out.println(programConfigString);
        Gson g = new Gson();
        DBConnection.setInstance(null);
        ProgramConfig.setInstance(null);
        TablesData.setInstance(null);
        Solution.setInstance(null);
        ProgramConfig programConfig = g.fromJson(programConfigString, ProgramConfig.class);
        ProgramConfig.setInstance(programConfig);
        try {
            String TIMESTAMP = System.currentTimeMillis() + "";
            String TARGET_DB = "src/main/resources/targetFile" + TIMESTAMP + ".db";
            SQLLiteUtils.copyFileLocally (file.getInputStream(),TARGET_DB);
            ProgramConfig.getInstance().setDbPath(TARGET_DB);
            //Funcion que devuelva toda la preguntas generadas, no en formato fichero sino en JSON normal para que el Front pueda procesarlo
            Context context = new Context(new InitializeState());
            context.doStateFunction("0");
            SQLLiteUtils.deleteFileLocally(TARGET_DB);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Solution.getInstance();
    }


    @GetMapping (value = "/schema")
    public Schema getStructureOfAnswer(@RequestParam("config") String programConfigString, @RequestParam("file") MultipartFile file) {
        System.out.println("CONFIG:");
        System.out.println("________________________________________________________");
        System.out.println(programConfigString);
        Gson g = new Gson();
        DBConnection.setInstance(null);
        ProgramConfig.setInstance(null);
        TablesData.setInstance(null);
        Solution.setInstance(null);
        ProgramConfig programConfig = g.fromJson(programConfigString, ProgramConfig.class);
        ProgramConfig.setInstance(programConfig);

        Schema schemaSolution = new Schema("");
        StringBuilder solution =  new StringBuilder("");
        try {
            String TIMESTAMP = System.currentTimeMillis() + "";
            String TARGET_DB = "src/main/resources/targetFile" + TIMESTAMP + ".db";
            SQLLiteUtils.copyFileLocally (file.getInputStream(),TARGET_DB);
            ProgramConfig.getInstance().setDbPath(TARGET_DB);
            //Funcion que devuelva toda la preguntas generadas, no en formato fichero sino en JSON normal para que el Front pueda procesarlo

            solution = generateSchema();

            SQLLiteUtils.deleteFileLocally(TARGET_DB);
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaSolution.setSchemaString(solution.toString());
        return schemaSolution;
    }

    @PostMapping(value="/answers/download", consumes = "application/json")
    public ResponseEntity<Resource> viewAttach(@RequestBody TemplateQuestion question)
    {

        ProgramConfig.getInstance().setDbPath(question.getProject().getPathToDbFile());
        //Funcion que devuelva toda la preguntas generadas, no en formato fichero sino en JSON normal para que el Front pueda procesarlo

        ConvertAnswerToXML convertAnswerToXML = new ConvertAnswerToXML(question,generateSchema().toString());

        File file2Upload = new File(convertAnswerToXML.convert());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file2Upload));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file2Upload.length())
                    .contentType(MediaType.APPLICATION_XML)
                    .body(resource);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return null;

    }


    private StringBuilder generateSchema () {
        HashMap<Integer,Taula> taulesById = new HashMap<>();
        HashMap <String, Taula> taules = DBConnection.getInstance(ProgramConfig.getInstance().getDbPath()).showTables(taulesById);
        StringBuilder solution =  new StringBuilder("");

        for (Map.Entry<String, Taula> entry: taules.entrySet()){
            solution.append("<li><strong><span class=\"\" style=\"color: rgb(51, 102, 255);\">").append(entry.getValue().getNomTaula()).append("(");
            int i = 0;

            for(Columna entry3:entry.getValue().getForeignKeys()) {
                boolean isPK = entry3.getPK();
                if(isPK) {
                    solution.append("<span><u><span class=\"\" style=\"color: rgb(51, 102, 255);\">");
                }
                solution.append(entry3.getColumnName()).append("(FK)");

                if(isPK) {
                    solution.append("</span></u></span>");
                }
                if ((i != entry.getValue().getForeignKeys().size()-1) || (entry.getValue().getColumnes().size() != 0 && i == entry.getValue().getForeignKeys().size()-1)) {
                    solution.append(",");
                }
                i++;
            }

            i = 0;
            for (Map.Entry<String, Columna> entry2:entry.getValue().getColumnes().entrySet()){
                boolean isPK = entry2.getValue().getPK();
                if(isPK) {
                    solution.append("<span><u><span class=\"\" style=\"color: rgb(51, 102, 255);\">");
                }
                solution.append(entry2.getValue().getColumnName());
                if(isPK) {
                    solution.append("</span></u></span>");
                }
                if (i != entry.getValue().getColumnes().entrySet().size()-1) {
                    solution.append(",");
                }
                i++;
            }
            solution.append(")</span></strong></li>");
        }
        return solution;
    }



}
