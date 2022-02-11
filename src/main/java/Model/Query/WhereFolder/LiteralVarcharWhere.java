package Model.Query.WhereFolder;



public class LiteralVarcharWhere extends WhereOperand {
    String varchar;

    public LiteralVarcharWhere(String varchar) {
        super("LiteralVarcharWhere");
        this.varchar = varchar.replace("'","''");
    }

    public String getVarchar() {
        return varchar;
    }

    public void setVarchar(String varchar) {
        this.varchar = varchar;
    }

    @Override
    public String toString() {
        return "'" + varchar + "'";
    }
}
