package Controller.Communication;

import Model.EQSPlain.ResponseEqsPlain;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface APIInterfaceEQSPlain {

    @POST("/eqsplain/api/sql_explanations/")
    Call<ResponseEqsPlain> getListOfQuestionsForOneQuery(@Query("sqlQuery") String sqlQuery, @Query("database") String database, @Query("k") int k);


}
