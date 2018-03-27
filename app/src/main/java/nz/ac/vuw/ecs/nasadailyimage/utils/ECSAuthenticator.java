package nz.ac.vuw.ecs.nasadailyimage.utils;

/**
 * Created by yimingpeng on 12/08/16.
 */
public class ECSAuthenticator extends java.net.Authenticator {
    protected java.net.PasswordAuthentication getPasswordAuthentication() {
        //Please change the username and password to yours
        return new java.net.PasswordAuthentication("huangkun", "HK48264586ry".toCharArray());
    }
}

