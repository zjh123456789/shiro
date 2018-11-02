package pers.zjh;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

import org.apache.shiro.subject.Subject;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:     shiro 安全框架
 * @Author:         Zhujinghui
 * @CreateDate:     2018/11/1 17:22
 */

public class TestShiro {

    public static void main(String[] args) {

        User user = new User();
        user.setName("tom");
        user.setPassword("123");

        if (login(user)){
            System.out.println("登录成功!");
        }else{
            System.out.println("登录失败!");
        }
    }

    private static Subject getSubject() {
        //加载配置文件，并获取工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //获取安全管理者实例
        SecurityManager securityManager = factory.getInstance();
        //将安全管理者放入全局对象
        SecurityUtils.setSecurityManager(securityManager);
        //全局对象通过安全管理者生成Subject对象
        Subject subject = SecurityUtils.getSubject();

        return subject;
    }

    public static boolean hasRole(String role){
        Subject subject = getSubject();
        return subject.hasRole(role);
    }

    private static boolean isPermitted(String permit){
        Subject subject = getSubject();
        return subject.isPermitted(permit);
    }

    private static boolean login(User user){
        Subject subject = getSubject();

        // 如果已经登录过了，退出
        if (subject.isAuthenticated()){
            subject.logout();
        }

        // 封装用户的数据
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getName(),user.getPassword());
        try{
            // 将用户的数据token 最终传递到Realm 中对比
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e){
            // 验证错误
            return false;
        }

        return subject.isAuthenticated();
    }
}
