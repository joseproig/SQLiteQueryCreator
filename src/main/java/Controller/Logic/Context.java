package Controller.Logic;

import java.io.IOException;

public class Context {
    private State actualstate;

    public Context(State initialState) {
        actualstate = initialState;
        initialState.setContext(this);
    }

    /**
     * Mètode per canviar l'estat actual del programa
     * @param state Estat al que volem canviar
     */
    public void changeState (State state) {
        actualstate = state;
        actualstate.setContext(this);
    }

    /**
     * Mètode que realitza la funció de l'estat actual
     * @param string Parametre que se li passa al estat per realitzar la funció
     */
    public void doStateFunction (String string) {
        try {
            actualstate.doYourFunction(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
