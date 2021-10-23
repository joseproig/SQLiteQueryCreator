package Controller.Logic;

import java.io.IOException;

public abstract class State {
     protected Context context;

     /**
      * Funció que realitzara l'estat concret
      * @param string Parametres que se li poden passar al mètode
      * @throws IOException Excepció de comunicació que hi pot haver en la comunicació de l'estat
      */
     abstract void doYourFunction(String string) throws IOException;


     public void setContext(Context context) {
          this.context = context;
     }
}
