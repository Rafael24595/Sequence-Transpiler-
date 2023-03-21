package tools;

import java.util.*;

public class ValueParser {

    private ValueParser(){
    }

    public static boolean isString(Object object){
        try {
            return object instanceof String;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isBoolean(Object object){
        if (object instanceof Boolean)
            return true;

        try {
            String str = (String) object;
            return str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("FALSE");
        }catch (Exception e){
        }

        return false;
    }

    public static boolean instanceOfString(Object object){
        return object instanceof String;
    }

    public static boolean instanceOfBoolean(Object object){
        return object instanceof Boolean;
    }

    public static boolean instanceOfNumber(Object object){
        return object instanceof Double || object instanceof Long || object instanceof Integer;
    }

    public static boolean isNumeric(Object object){
        if (object instanceof Double || object instanceof Integer)
            return true;

        try {
            Integer.parseInt((String) object);
        }catch (Exception e){
            return isDouble(object);
        }
        return true;
    }

    public static boolean isInteger(Object object){
        if (object instanceof Integer)
            return true;

        try {
            Integer.parseInt((String) object);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean isLong(Object object){
        if (object instanceof Long)
            return true;

        try {
            Long.parseLong((String) object);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean isDouble(Object object){
        if (object instanceof Double)
            return true;

        try {
            Double.parseDouble((String) object);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static Boolean parseBoolean(Object object) throws IllegalArgumentException {
        if(object.getClass() == Boolean.class)
            return (Boolean) object;

        if(object.getClass() == String.class)
            try {
                return Boolean.parseBoolean((String) object);
            }catch (Exception e){
                throw new IllegalArgumentException("Cannot parse "+ object.toString() + " to Boolean object.");
            }

        throw new IllegalArgumentException("Invalid object type.");
    }

    public static Integer parseInteger(Object object) throws IllegalArgumentException {
        if(object.getClass() == Integer.class)
            return (Integer) object;

        if(object.getClass() == String.class)
            try {
                return Integer.parseInt((String) object);
            }catch (Exception e){
                throw new IllegalArgumentException("Cannot parse "+ object.toString() + " to Integer object.");
            }

        throw new IllegalArgumentException("Invalid object type.");
    }

    public static Long parseLong(Object object) throws IllegalArgumentException {
        if(object.getClass() == Long.class)
            return (Long) object;

        if(object.getClass() == Integer.class)
            return Long.valueOf(parseInteger(object));

        if(object.getClass() == String.class)
            try {
                return Long.parseLong((String) object);
            }catch (Exception e){
                throw new IllegalArgumentException("Cannot parse "+ object.toString() + " to Integer object.");
            }

        throw new IllegalArgumentException("Invalid object type.");
    }

    public static Double parseDouble(Object object) throws IllegalArgumentException {
        if(object.getClass() == Double.class)
            return (Double) object;

        if(object.getClass() == Long.class)
            return Double.valueOf(parseLong(object));

        if(object.getClass() == Integer.class)
            return Double.valueOf(parseInteger(object));

        if(object.getClass() == String.class)
            try {
                return Double.parseDouble((String) object);
            }catch (Exception e){
                throw new IllegalArgumentException("Cannot parse "+ object.toString() + " to Double object.");
            }

        throw new IllegalArgumentException("Invalid object type.");
    }

    public static boolean typeOf(Object value1, Object value2){
        return value1.getClass().isAssignableFrom(value2.getClass());
    }

    public static Set<String> joinMapKeys(Map<String, Object> map1, Map<String, Object> map2) {
        Set<String> joinSet = new HashSet<>();
        if(map1 != null)
            joinSet.addAll(map1.keySet());
        joinSet.addAll(map2.keySet());

        return joinSet;
    }

    public static List<Object> joinListValues(List<Object> list1, List<Object> list2){
        LinkedHashSet<Object> hash = new LinkedHashSet<>();
        hash.addAll(list2);
        hash.addAll(list1);

        return new ArrayList<>(hash);
    }

}