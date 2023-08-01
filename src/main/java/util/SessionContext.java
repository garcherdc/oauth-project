package util;


import com.example.demo.entity.UserInfo;


public class SessionContext {
    private static ThreadLocal<UserInfo> CURRENT_USER = new ThreadLocal<>();


    public static void save(UserInfo userInfo) {
        CURRENT_USER.set(userInfo);
    }
    public static void clear(){
        CURRENT_USER.remove();
    }
    public static UserInfo getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static String getCurrentLoginId() {
        return CURRENT_USER.get() == null ? "" : CURRENT_USER.get().getLoginId();
    }
    public static String getCurrentSelectPartner() {
        return CURRENT_USER.get()==null ? null:CURRENT_USER.get().getSelectPartner();
    }
}
