package br.ifpe.edu;

import com.ezylang.evalex.Expression;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Eval {
    public static String evalDecimal(String format, String ...args) {
        Expression e = new Expression(String.format(format, (Object[]) args));
        return evalNumber(e, true);
    }

    public static String evalInteger(String format, String ...args) {
        Expression e = new Expression(String.format(format, (Object[]) args));
        return evalNumber(e, false);
    }

    private static String evalNumber(Expression e, boolean isFloat) {
        try {
            BigDecimal r = e.evaluate().getNumberValue();
            if (isFloat) {
                return r.setScale(2, RoundingMode.HALF_UP).toPlainString();
            }

            return r.toBigInteger().toString();

        } catch (Exception _) {
            return "";
        }
    }

    public static boolean compare(String v1, String v2) {
        try {
            return Double.parseDouble(v1) >= Double.parseDouble(v2);
        } catch (Exception _) {
            return false;
        }
    }
}