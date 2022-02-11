package Controller.Communication;

import Model.EQSPlain.ResponseEqsPlain;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class EQSplainClient {

    private Retrofit retrofit;
    private static EQSplainClient eqSplainClient;
    private APIInterfaceEQSPlain apiInterfaceEQSPlain;
    private static String BASE_URL = "https://darelab.imsi.athenarc.gr";


    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build();

    /**
     * Mètode per obtindre la instancia de Singleton de l'objecte
     * @return Retorna la instancia de l'objecte
     */
    public static EQSplainClient getInstance() {
        if (eqSplainClient == null) {
            eqSplainClient = new EQSplainClient();
        }
        return eqSplainClient;
    }

    public EQSplainClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        apiInterfaceEQSPlain = retrofit.create(APIInterfaceEQSPlain.class);
    }

    /**
     * Metode abstracte que serveix per fer una petició a EQSPlain demanant un enunciat per a una query especifica
     * @param sqlQuery Query que volem l'enunciat
     * @param database Nom de la base de dades (No té importancia)
     * @param k Numero de respostes que volem
     * @return Resposta que rebra l'usuari
     */
    public Call<ResponseEqsPlain> getListOfQuestionsForOneQuery(String sqlQuery, String database, int k) {
        return apiInterfaceEQSPlain.getListOfQuestionsForOneQuery(sqlQuery,database,k);
    }




}
