package RestController;

import Controller.DBLogic.DBConnection;
import Controller.Logic.Context;
import Controller.Logic.InitializeState;
import Model.ParametersOfQuestion.Question;
import Model.ParametersOfQuestion.TemplateQuestion;
import Model.ProgramConfig;
import Model.Query.Select;
import Model.RestAPI.Solution;
import Model.TablesData;
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

    @PostMapping(value="/answers/download", consumes = "application/json")
    public ResponseEntity<Resource> viewAttach(@RequestBody TemplateQuestion question)
    {
        ConvertAnswerToXML convertAnswerToXML = new ConvertAnswerToXML(question);
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






}
