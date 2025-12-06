package br.ifpe.edu;

import com.ezylang.evalex.Expression;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Eval {
    public static String eval(String format, String ...args) {
        Expression e = new Expression(String.format(format, (Object[]) args));
        try {
            BigDecimal r = e.evaluate().getNumberValue();
            return r.setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (Exception _) {
            return "";
        }
    }

    public static boolean evalBoolean(String format, String ...args) {
        Expression e = new Expression(String.format(format, (Object[]) args));
        try {
            BigDecimal r = e.evaluate().getNumberValue();
            return r.compareTo(BigDecimal.ZERO) != 0;
        } catch (Exception _) {
            return false;
        }
    }
}