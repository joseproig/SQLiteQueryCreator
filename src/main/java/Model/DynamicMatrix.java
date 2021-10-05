package Model;

public class DynamicMatrix {
    //De files a columnes (De esquerra a dreta)

    private byte [][] matriu;
    private int quantitat;


    public DynamicMatrix() {
        matriu = new byte [8][1];
    }

    public DynamicMatrix (int n_taules) {
        int multiplicador = 1;
        int i = 1;
        while (multiplicador <= n_taules) {
            multiplicador =  i* 8;
            i++;
        }
        matriu = new byte [multiplicador] [i-1];
    }

    public void insertarTaula (int posicio, int [] taulesApuntades) {
        if (quantitat == matriu.length) {
            ampliarMatriu ();
        }
        for (int j = 0; j < matriu[posicio].length;j++) {
            matriu[posicio][j] = 0;
        }
        for (int i  = 0; i < taulesApuntades.length; i++) {
            matriu[posicio][taulesApuntades[i]/8] = (byte) (matriu[posicio][taulesApuntades[i]/8]| (1 << (taulesApuntades[i]%8)));
        }
        quantitat++;
    }


    private void ampliarMatriu() {
        byte [][] matrizNueva = new byte [2*quantitat][matriu[0].length+1];
        for(int y = 0; y < matriu.length; y++) {
            matrizNueva [y] = matriu [y];
        }
        matriu = matrizNueva;
    }

    public void eliminarTaula (int indice) {
        matriu[indice] = null;
        for(int i= 0; i < matriu.length; i++) {
            if (matriu[i] != null) {
                matriu[i][indice / 8] = (byte) (matriu[i][indice / 8] & ~(1 << (indice % 8)));
            }
        }
    }

    public Integer[] retornaTaulesVinculadesAUnaTaula (int idUser) {
        int i= 0 ;
        int j = 0;
        Integer [] resultado = new Integer[matriu[idUser].length];
        for (int u  =0; u < matriu[idUser].length; u++) {
            for (int h = 0; h < 8; h++) {
                if ((matriu[idUser][i] & 1) == 1) {
                    resultado[j] = h + (i*8);
                    j++;
                }
                matriu[idUser][i] = (byte) (matriu[idUser][i] >> 1);
            }
            i++;
        }
        return resultado;
    }

    public byte[][] getMatriu() {
        return matriu;
    }

    public void setMatriu(byte[][] matriu) {
        this.matriu = matriu;
    }

    public int getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(int cantidad) {
        this.quantitat = cantidad;
    }
}