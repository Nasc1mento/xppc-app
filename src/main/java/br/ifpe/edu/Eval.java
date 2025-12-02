package br.ifpe.edu;


import com.ezylang.evalex.Expression;

public class Eval {
    public static String eval(final String expStr) {
        Expression e = new Expression(expStr);
        try  {
            return e.evaluate().getStringValue();
        } catch (Exception _) {
            return "";
        }
    }
}
