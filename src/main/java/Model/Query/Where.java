package Model.Query;

import Model.Query.WhereFolder.Expression;

import java.util.ArrayList;
import java.util.List;

public class Where implements Cloneable{
    private List<Expression> expressions;
    private Boolean negateExpression = false;

    public Where() {
        this.expressions = new ArrayList<>();
    }

    public Where (List<Expression> expressions) {
        this.expressions = new ArrayList<>(expressions);
    }

    public List<Expression> getExpression() {
        return expressions;
    }

    public void setExpression(List<Expression> expression) {
        this.expressions = expression;
    }

    public void addExpression (Expression expression){
        expressions.add(expression);
    }

    public Boolean getNegateExpression() {
        return negateExpression;
    }

    public void setNegateExpression(Boolean negateExpression) {
        this.negateExpression = negateExpression;
    }

    public String toString () {
        if (expressions.size() != 0) {
            StringBuilder where = new StringBuilder(" WHERE ");
            if (negateExpression) {
                where.append("NOT (");
            }
            for (int j = 0; j < (expressions.size() - 1); j++) {
                where.append(expressions.get(j).toString()).append(" AND ");
            }
            where.append(expressions.get(expressions.size() - 1));
            if (negateExpression) {
                where.append(")");
            }
            return where.toString();
        }
        return "";
    }
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
