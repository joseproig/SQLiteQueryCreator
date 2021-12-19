package Model.Query.WhereFolder;



public class Expression extends WhereOperand {
    private WhereOperand expression;
    private String operator;
    private WhereOperand expression_2;

    public Expression(WhereOperand expression, String operator, WhereOperand expression_2) {
        super("Expression");
        this.expression = expression;
        this.operator = operator;
        this.expression_2 = expression_2;
    }

    public WhereOperand getExpression() {
        return expression;
    }

    public void setExpression(WhereOperand expression) {
        this.expression = expression;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public WhereOperand getExpression_2() {
        return expression_2;
    }

    public void setExpression_2(WhereOperand expression_2) {
        this.expression_2 = expression_2;
    }

    public String toString () {
        return expression.toString() + operator + expression_2.toString();
    }
}
