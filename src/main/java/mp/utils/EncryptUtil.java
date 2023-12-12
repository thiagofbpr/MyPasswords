package mp.utils;

import java.util.concurrent.ThreadLocalRandom;

public class EncryptUtil {

    private static final int BASE_NUMBER = 4;
    private static final int START_CHAR_NUMBER = 33;
    private static final int END_CHAR_NUMBER = 126;

    private EncryptUtil() { }

    public static String encrypt(String pwd) {
        StringBuilder sb = new StringBuilder();
        for(char c : pwd.toCharArray()) {
            for(int i = 0; i < BASE_NUMBER; i++) {
                int randomNum =  ThreadLocalRandom.current().ints(START_CHAR_NUMBER, END_CHAR_NUMBER).findAny().getAsInt() ;
                char aChar = (char) randomNum;
                sb.append(aChar);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String decrypt(String pwd) {
        StringBuilder sb = new StringBuilder();
        for(int i = BASE_NUMBER; i < pwd.length(); i = i + BASE_NUMBER + 1) {
            sb.append(pwd.charAt(i));
        }
        return sb.toString();
    }

    public static String getBlackBullets() {
        return "●●●●●●●●●●";
    }


}
