package com.flab.marketgola;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.hibernate.engine.jdbc.internal.FormatStyle;

/**
 * P6spy를 사용해서 쿼리 파라미터를 찍을 때 예쁘게 찍힐 수 있게 도와주는 클래스
 */
public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
            String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        Date currentDate = new Date();

        SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd HH:mm:ss");

        //return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + sql;
        return format1.format(currentDate) + " | " + "OperationTime : " + elapsed + "ms" + sql;
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().equals("")) {
            return sql;
        }

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith(
                    "comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
            sql = "|\nHeFormatSql(P6Spy sql,Hibernate format):" + sql;
        }

        return sql;
    }
}


