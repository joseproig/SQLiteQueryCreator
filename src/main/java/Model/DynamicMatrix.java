package Model;

public class DynamicMatrix {
    //De files a columnes (De esquerra a dreta)

    private byte [][] matriu;
    private short quantitat;


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

    public void convertirEnBidireccional () {
        short i= 0 ;
        short j = 0;

        byte [][] matriu2 = new byte[matriu.length][];
        for (int o = 0; o < matriu.length;o++) {
            matriu2[o] = new byte[matriu[o].length];
            System.arraycopy(matriu[o], 0, matriu2[o], 0, matriu[o].length);
        }

        for (short idTaula  = 0; idTaula < quantitat; idTaula++) {
            for (short u = 0; u < matriu[idTaula].length; u++) {
                for (short h = 0; h < 8; h++) {
                    if (((matriu[idTaula][i] >> h) & 1) == 1) {
                        int pepe_2 = h + (i * 8);
                        int pepe_3 = idTaula%8;
                        int pepe_4 = idTaula/8;
                        int pepe_5 = 1 << idTaula%8;
                        matriu2[h + (i * 8)][idTaula/8] = (byte) (matriu2[h + (i * 8)][idTaula/8] | (1 << idTaula%8));
                        j++;
                    }
                }
                i++;
            }
            i = 0;
        }
        matriu = matriu2;
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

    public Short[] retornaTaulesVinculadesAUnaTaula (int idTaula) {
        short i= 0 ;
        short j = 0;
        Short [] resultado = new Short[quantitat];
        for (short u  =0; u < matriu[idTaula].length; u++) {
            for (short h = 0; h < 8; h++) {
                if (((matriu[idTaula][i] >> h) & 1) == 1) {
                    resultado[j] = (short) (h + (i*8));
                    j++;
                }
            }
            i++;
        }
        if (j == 0) {
            return null;
        } else {
            return resultado;
        }
    }

    public short retornaNumComunicacionsDirectesTaula (int idTaula) {
        short i= 0 ;
        short j = 0;
        for (short u  =0; u < matriu[idTaula].length; u++) {
            for (short h = 0; h < 8; h++) {
                if (((matriu[idTaula][i] >> h) & 1) == 1) {
                    j++;
                }
            }
            i++;
        }
        return j;
    }


    public byte[][] getMatriu() {
        return matriu;
    }

    public void setMatriu(byte[][] matriu) {
        this.matriu = matriu;
    }

    public short getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(short cantidad) {
        this.quantitat = cantidad;
    }
}