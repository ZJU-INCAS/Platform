package team.educoin.transaction.util;

/**
 * @description: 指纹虹膜登录验证模块，C++本地方法
 * @author: PandaClark
 * @create: 2019-06-17
 */
public class VerifyUtil {

    public static native boolean verifyFingerprint(String s1, String s2);

    static {
        try {
            System.loadLibrary("VerifyUtil");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e.getMessage());
        }
    }
}
