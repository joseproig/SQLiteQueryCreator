package RestController;

import Controller.DBLogic.DBConnection;
import Controller.Logic.Context;
import Controller.Logic.InitializeState;
import Model.ProgramConfig;
import Model.Query.Select;
import Model.RestAPI.Solution;
import Utils.SQLLiteUtils;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;



@RestController
public class RestAPIController {
    @PostMapping (value = "/getPossibilities")
    public Solution createPerson(@RequestParam("config") String programConfigString, @RequestParam("file") MultipartFile file) {
        System.out.println("CONFIG:");
        System.out.println("________________________________________________________");
        System.out.println(programConfigString);
        Gson g = new Gson();
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
            DBConnection.setInstance(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Solution.getInstance();
    }





}
