package pers.zjh;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by admin on 2018/11/2
 */
public class TestEncryption {

    public static void main(String[] args) {
        String password = "123";
        String encodedPassword = new Md5Hash(password).toString();

        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodingPassword = new SimpleHash(algorithmName,password,salt,times).toString();

        System.out.println(encodingPassword);
    }
}
