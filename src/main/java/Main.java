import Controller.Logic.Logic;
import Model.DynamicMatrix;
import Model.Taula;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Logic logic = new Logic();
        HashMap<String, Taula> taules = logic.getTaules();
        DynamicMatrix dynamicMatrix = logic.initializeGraph(taules);
        System.out.println("El programa ha finalitzat");
    }
}
