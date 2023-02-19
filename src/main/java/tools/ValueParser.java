package tools;

public class ValueParser {

    private ValueParser(){
    }

    public static boolean isNumeric(Object object){
        try {
            Integer.parseInt((String) object);
        }catch (Exception e){
            return isDouble(object);
        }
        return true;
    }

    public static boolean isDouble(Object object){
        try {
            Double.parseDouble((String) object);
        }catch (Exception e){
            return false;
        }
        return true;
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

        throw new IllegalArgumentException("Invalid objet type.");
    }

    public static Double parseDouble(Object object) throws IllegalArgumentException {
        if(object.getClass() == Double.class)
            return (Double) object;

        if(object.getClass() == Integer.class)
            return Double.valueOf((Integer) object);

        if(object.getClass() == String.class)
            try {
                return Double.parseDouble((String) object);
            }catch (Exception e){
                throw new IllegalArgumentException("Cannot parse "+ object.toString() + " to Double object.");
            }

        throw new IllegalArgumentException("Invalid objet type.");
    }

}