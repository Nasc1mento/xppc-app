package br.ifpe.edu.utils;

import org.apache.commons.jexl3.*;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class Eval {

    private static final JexlEngine JEXL_ENGINE = new JexlBuilder()
            .cache(512)
            .strict(true)
            .silent(false)
            .create();

    public static boolean evalBoolean(String format, String ...args) {
        Object r = execute(format, args);
        if (r != null) {
            return (Boolean) r;
        }

        return false;
    }

    public static String evalDecimal(String format, String ...args) {
        Object r = execute(format, args);
        if (r != null) {
            double val = ((Number) r).doubleValue();
            return String.format(Locale.US, "%.2f", val);
        }

        return "0.00";
    }

    public static String evalInteger(String format, String ...args) {
        Object r = execute(format, args);
        if (r != null) {
            return r.toString();
        }

        return "0";
    }

    public static Object eval(String expression, Map<String, Object> vars) {
        try {
            JexlContext context = new MapContext(vars != null ? vars : Collections.emptyMap());
            JexlExpression jexlExpr = JEXL_ENGINE.createExpression(expression);
            return jexlExpr.evaluate(context);
        } catch (JexlException ex) {
            return null;
        }
    }

    private static Object execute(String format, String... args) {
        String expression = String.format(format, (Object[]) args);
        return eval(expression, Collections.emptyMap());
    }

}