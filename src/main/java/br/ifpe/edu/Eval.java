package br.ifpe.edu;

import com.ezylang.evalex.Expression;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Eval {
    public static String eval(final String expStr) {
        Expression e = new Expression(expStr);
        try {
            BigDecimal resultado = e.evaluate().getNumberValue();
            return resultado.setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (Exception _) {
            return "";
        }
    }

    public static String eval(String format, String ...args) {
        return eval(String.format(format, (Object[]) args));
    }
}