package com.liumapp.qtools.data.helper;

import com.liumapp.qtools.data.core.Row;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import static com.liumapp.qtools.data.helper.StringType.*;

/**
 * file RowSetter.java
 * author liumapp
 * github https://github.com/liumapp
 * email liumapp.com@gmail.com
 * homepage http://www.liumapp.com
 * date 2018/9/8
 */
final public class RowSetter {
    public static void setRow(Row row, Class c, String s, Object k, String format){

        if (c.equals(String.class)) row.setColumn(k, string2String(s), c);

        else if (c.equals(Byte.class)) row.setColumn(k, string2Byte(s), c);

        else if (c.equals(Short.class)) row.setColumn(k, string2Short(s), c);

        else if (c.equals(Integer.class)) row.setColumn(k, string2Integer(s), c);

        else if (c.equals(Long.class)) row.setColumn(k, string2Long(s), c);

        else if (c.equals(BigInteger.class)) row.setColumn(k, string2BigInteger(s), c);

        else if (c.equals(BigDecimal.class)) row.setColumn(k, string2BigDecimal(s), c);

        else if (c.equals(Time.class)) row.setColumn(k, string2Time(s, format), c);

        else if (c.equals(Date.class)) row.setColumn(k, string2Date(s, format), c);

        else if (c.equals(Timestamp.class)) row.setColumn(k, string2Timestamp(s, format), c);

        else if (c.equals(Double.class)) row.setColumn(k, string2Double(s), c);

    }
}
