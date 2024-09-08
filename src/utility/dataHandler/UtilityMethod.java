package utility.dataHandler;
import java.util.ArrayList;

public class UtilityMethod {
    public static String addPrefix(String prefix, String id) {
        return prefix + id;
    }

    public static int seperateID(String strID) {
        String[] part = strID.split("(?<=\\D)(?=\\d)");
        //part[0] gives Prefix, part [1] gives numeric part
        //System.out.println(part[0]);
        //System.out.println(part[1]);
        return Integer.parseInt(part[1]);
    }

    public static boolean checkDataAvailability(ArrayList<String> list, String value){
        return list.contains(value);
    }
}
