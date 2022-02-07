import Controller.Logic.Context;
import Controller.Logic.InitializeState;
import Model.BestPathsMatrix;
import Model.DynamicMatrix;
import Model.MatrixOfConnections;
import Model.Taula;
import Utils.Algorithms.FloydWarshall;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        //Filtrem a aquells camins que interessen
        Context context = new Context(new InitializeState());
        context.doStateFunction("0");


        //TODO: Resposta per descartar solucions repetides fer Hashmap de ids de les taules ordenats
        System.out.println("El programa ha finalitzat");
        System.exit(0);
    }
}
