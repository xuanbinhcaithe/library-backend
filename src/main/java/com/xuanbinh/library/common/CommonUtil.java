package com.xuanbinh.library.common;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);


    public static Long decimalToLong(BigDecimal value) {
        if (value == null) {
            return 0L;
        }
        return value.longValue();
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().isEmpty());
    }

    public static boolean isNullOrEmpty(List data) {
        return (data == null || data.isEmpty());
    }


    public static Date convertStringToDate(String date) throws ParseException {
        if (isNullOrEmpty(date)) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(date);
    }

    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        }
        String patten = "dd/MM/yyy";
        DateFormat dateFormat = new SimpleDateFormat(patten);
        return dateFormat.format(date);
    }

    public static String convertDateToString(Date date, String patten) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(patten);
        return dateFormat.format(date);
    }

    public static Date convertStringToDate(String str, String pattern) throws ParseException {
        if (isNullOrEmpty(str)) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            LOGGER.error("convertStringToDateTIme", e);
            return null;
        }
    }

    public static String getConfig(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        return rb.getString(key);
    }

    public static Object NVL(Object value, Object defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static Double NVL(Double value, Double defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static Double NVL(Double value) {
        return NVL(value, new Double(0L));
    }

    public static Integer NVL(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static Integer NVL(Integer value) {
        return NVL(value, new Integer(0));
    }

    public static BigDecimal NVL(BigDecimal value) {
        return value == null ? new BigDecimal(0) : value;
    }

    public static String NVL(String s) {
        return isNullOrEmpty(s) ? "" : s;
    }

    public static Long NVL(Long value) {
        return (value == null ? new Long(0L) : value);
    }

    public static Long checkBoxBValue(Long value) {
        if (value != null && value.equals(1L)) {
            return 1L;
        }
        return 0L;
    }

    public static List<String> toList(String str, String pattern) {
        if ("".equals(NVL(str).trim())) {
            return new ArrayList<>();
        }
        List<String> lst = new ArrayList<>(Arrays.asList(str.split(pattern)));
        List<String> result = lst.stream().map(x -> x.trim()).collect(Collectors.toList());
        return result;
    }

    public static List<Long> toLongList(String str, String pattern) {
        List<Long> result = new ArrayList<>();
        String[] lstLong = NVL(str).split(pattern);
        for (String s : lstLong) {
            if (!isNullOrEmpty(s)) {
                result.add(Long.parseLong(s));
            }
        }
        return result;
    }

    public static double monthsBetween(Date endDate, Date startDate) {
        Calendar endCalendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        startCalendar.setTime(startDate);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth;
    }

    public static Integer yearBetween(Date endDate, Date startDate) {
        Calendar endCalendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        startCalendar.setTime(startDate);
        Integer diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        return diffYear;
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static String getBase64StringOfImage(File imageFile) throws IOException {
        String imgString;
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", bout);
            byte[] imgeBytes = bout.toByteArray();
            imgString = Base64.getEncoder().encodeToString(imgeBytes);
        }
        return imgString;
    }

    public static Date TRUNC(Date inputDate) {
        if (inputDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR, 0);
        return cal.getTime();
    }

    public static void filter(String str, StringBuilder queryString, List<Object> paramList, String fields) {
        if (!isNullOrEmpty(str)) {
            queryString.append(" AND LOWER (").append(fields).append(") LIKE ? ESCAPE '/'");
            str = str.replace("  ", " ");
            str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
            paramList.add(str);
        }
    }

    public static String fGetLanguage(String tableName, String columnName, String objectId, String langCode, String defaultValue) {
        return String.format("F_GET_LANGUAGE('%s', '%s', %s, '%s', %s)", tableName, columnName, objectId, langCode, defaultValue);
    }

    public static void filter(Long n, StringBuilder queryString, List<Object> paramList, String field) {
        if (n != null && n > 0L) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    public static void filter(Double n, StringBuilder queryString, List<Object> paramList, String field) {
        if (n != null) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    public static void filter(Boolean n, StringBuilder queryString, List<Object> paramList, String field) {
        if (n != null) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    public static void filter(Integer n, StringBuilder queryString, List<Object> paramList, String field) {
        if (n != null && n > 0) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    public static void filter(Date n, StringBuilder queryString, List<Object> paramList, String field) {
        if (n != null) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    public static void filterSelectInL(String arrId, StringBuilder queryString, List<Object> paramList, String field) {
        if (!CommonUtil.isNullOrEmpty(arrId)) {
            queryString.append(" AND ").append(field).append(" IN(-1 ");
            String[] ids = arrId.split(",");
            for (String s : ids) {
                queryString.append(", ?");
                paramList.add(Long.parseLong(s.trim()));
            }
            queryString.append(" ) ");
        }
    }

    public static void filterSelectInL(List<Long> arrId, StringBuilder queryString, List<Object> paramList, String field) {
        if (!CommonUtil.isNullOrEmpty(arrId)) {
            queryString.append(" AND ").append(field).append(" IN(-1 ");
            for (Long s : arrId) {
                queryString.append(", ?");
                paramList.add(s);
            }
            queryString.append(" ) ");
        }
    }

    public static void filterGe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" >= ? ");
            paramList.add(obj);
        }
    }

    public static void filterGe(Object obj1, Object obj2, StringBuilder queryString, List<Object> paramList, String field1, String field2) {
        if ((obj1 != null && !"".equals(obj1)) && (obj2 == null || "".equals(obj2))) {
            queryString.append(" AND ").append(field1).append(" >= ? ");
            paramList.add(obj1);
        } else if ((obj1 == null || "".equals(obj1)) && (obj2 != null && !"".equals(obj2))) {
            queryString.append(" AND ").append(field2).append(" <= ? ");
            paramList.add(obj2);
        } else if ((obj1 != null && !"".equals(obj1)) && (obj2 != null && !"".equals(obj2))) {
            queryString.append(" AND ( ").append(field2).append(" >= ? ");
            paramList.add(obj1);
            queryString.append(" OR ").append(field2).append(" IS NULL ) ");
            queryString.append(" AND ").append(field1).append(" <= ? ");
            paramList.add(obj2);
        }
    }

    public static void filterGe(Object obj1, Object obj2, StringBuilder queryString, List<Object> paramList, String field) {
        if ((obj1 != null && !"".equals(obj1)) && (obj2 == null || "".equals(obj2))) {
            queryString.append(" AND ( ").append(field).append(" >= DATE(?) ");
            paramList.add(obj1);
            queryString.append(" OR ").append(field).append(" IS NULL ");
        } else if ((obj1 == null || "".equals(obj1)) && (obj2 != null && !"".equals(obj2))) {
            queryString.append(" AND ( ").append(field).append(" <= DATE(?) ");
            paramList.add(obj2);
            queryString.append(" OR ").append(field).append(" IS NULL ");
        } else if ((obj1 != null && !"".equals(obj1)) && (obj2 != null && !"".equals(obj2))) {
            queryString.append(" AND ( ").append(field).append(" >= DATE(?) ");
            paramList.add(obj1);
            queryString.append(" AND ").append(field).append(" <= DATE(?) )");
            paramList.add(obj2);
            queryString.append(" OR ").append(field).append(" IS NULL ");
        }
    }

    public static void filterLe(Object obj, StringBuilder sqlQuery, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            sqlQuery.append(" AND ").append(field).append(" <= ? ");
            paramList.add(obj);
        }
    }

    public static void filter(String value, PreparedStatement preparedStatement, int index) throws SQLException {
        if (value != null) {
            preparedStatement.setString(index, value.trim());
        } else {
            preparedStatement.setNull(index, Types.NULL);
        }
    }

    public static boolean containUnicode(String str) {
        String signChars = "ăâđêôơưàảãạáằẳẵặắầẩẫậấèẻẽẹéềểễệếìỉĩịíòỏõọóồổỗộốờởỡợớùủũụúừửữựứỳỷỹỵý";
        if (isNullOrEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (signChars.indexOf(c) > -1) {
                return true;
            }
        }
        return false;
    }

    public static boolean containPhoneNumber(String str) {
        String strChars = "0123456789";
        if (isNullOrEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (strChars.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    public String replaceSpecialKeys(String str) {
        str = str.replace("  ", " ");
        str = "%" + str.replace("%", "/%").replace("/", "//").replace("_", "/_").trim().toLowerCase() + "%";
        return str;
    }

    public static String formatNumber(Double d) {
        if (d == null) {
            return "";
        }
        DecimalFormat format = new DecimalFormat("######.#####");
        return format.format(d);
    }

    public static String formatNumber(Long n) {
        if (n == null) {
            return "";
        }
        DecimalFormat decimalFormat = new DecimalFormat("######");
        return decimalFormat.format(n);
    }

    public static String formatNumber(Object d, String parten) {
        if (d == null) {
            return "";
        }
        DecimalFormat decimalFormat = new DecimalFormat(parten);
        return decimalFormat.format(d);
    }

    public static List<Long> string2ListLong(String inputString, String separator) {
        if (isNullOrEmpty(inputString) || isNullOrEmpty(separator)) {
            return new ArrayList<>();
        }
        List<String> arrStr = new ArrayList<>(Arrays.asList(inputString.split(separator)));
        List<Long> result = arrStr.stream().map(x -> Long.parseLong(x.trim())).collect(Collectors.toList());
        return result;
    }

    public static List<String> string2ListString(String inputString, String separator) {
        if (isNullOrEmpty(inputString) || isNullOrEmpty(separator)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>(Arrays.asList(inputString.split(separator)));
        result = result.stream().map(x -> x.trim()).collect(Collectors.toList());
        return result;
    }


    public static String convertListToString(List lstObject, String separator) {
        if (lstObject != null && !lstObject.isEmpty()) {
            List<String> lst = (List<String>) lstObject.stream().map(x -> x.toString()).collect(Collectors.toList());
            String result = lst.stream().map(x -> "'" + x.trim() + "'").collect(Collectors.joining(separator));
            return result;
        }
        return "";
    }


    public static Date subDayFromDate(Date date, int day) {
        if (date != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, -day);
            return c.getTime();
        }
        return null;
    }

    public static String convertObjectToStringJson(Object object) {
        if (object == null || "".equals(object)) {
            return "";
        }
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object);
        return jsonStr;
    }

    public static boolean isCollectionEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            if (((String) object).trim().length() == 0) {
                return true;
            }
        }
        if (object instanceof Collection) {
            return isCollectionEmpty((Collection<?>) object);
        }
        return false;
    }

}
