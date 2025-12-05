package br.ifpe.edu;

import com.ezylang.evalex.Expression;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Eval {
    public static String eval(final String expStr) {
        Expression e = new Expression(expStr);
        try {
            BigDecimal r = e.evaluate().getNumberValue();
            return r.setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (Exception _) {
            return "";
        }
    }

    public static String eval(String format, String ...args) {
        return eval(String.format(format, (Object[]) args));
    }

    public static boolean evalBoolean(final String expStr) {
        Expression e = new Expression(expStr);
        try {
            BigDecimal r = e.evaluate().getNumberValue();
            return r.compareTo(BigDecimal.ZERO) != 0;
        } catch (Exception _) {
            return false;
        }
    }

    public static boolean evalBoolean(String format, String ...args) {
        return evalBoolean(String.format(format, (Object[]) args));
    }
}