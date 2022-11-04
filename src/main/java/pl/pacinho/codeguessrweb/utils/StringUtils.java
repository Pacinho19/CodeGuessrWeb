package pl.pacinho.codeguessrweb.utils;

public class StringUtils {
    public static String replaceSlashes(String pid) {
        return pid.replace("\\", "/");
    }
}
