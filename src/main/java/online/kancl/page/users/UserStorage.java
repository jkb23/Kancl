package online.kancl.page.users;

import java.sql.Timestamp;

public class UserStorage {

    public static boolean findUser(String username, String hash) {
        return true;
    }

    public static void createUser(String username, String hash) {

    }

    public static void setBadLoginCnt(String username, int cnt) {

    }

    public static int getBadLoginCnt(String username){
        return 1;
    }

    public static void setBadLoginTimestamp(String username, Timestamp timestamp) {

    }

    public static Timestamp getBadLoginTimestamp(String username) {
        return new Timestamp(0);
    }
}
