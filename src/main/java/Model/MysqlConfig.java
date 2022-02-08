package Model;

import Model.ParametersOfQuestion.ParametersConfig;

public class MysqlConfig {
    private static MysqlConfig instance;
    private String mysql_user;
    private String mysql_passwd;

    public static MysqlConfig getInstance() {
        if (instance == null) {
            instance = new MysqlConfig();
        }
        return instance;
    }

    public static void setInstance(MysqlConfig instance) {
        MysqlConfig.instance = instance;
    }

    public String getMysql_user() {
        return mysql_user;
    }

    public void setMysql_user(String mysql_user) {
        this.mysql_user = mysql_user;
    }

    public String getMysql_passwd() {
        return mysql_passwd;
    }

    public void setMysql_passwd(String mysql_passwd) {
        this.mysql_passwd = mysql_passwd;
    }
}
