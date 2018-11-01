package pers.zjh;

import org.apache.shiro.SecurityUtils;
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

        // 创建三个用户
        User Tom = new User("Tom","123");
        User Jerry = new User("Jerry","12");
        User Bob = new User("Bob","1");

        // 将用户加入到容器中
        List<User> users = new ArrayList<User>();
        users.add(Tom);
        users.add(Jerry);
        users.add(Bob);

        //  角色
        String roleAdmin = "admin";
        String roleProductManager = "productManager";

        // 将角色加入到容器
        List<String> roles = new ArrayList<String>();
        roles.add(roleAdmin);
        roles.add(roleProductManager);

        // 权限
        String permitAddProduct = "addProduct";
        String permitAddOrder = "addOrder";

        // 将权限加到容器
        List<String> permits = new ArrayList<String>();
        permits.add(permitAddProduct);
        permits.add(permitAddOrder);

        // 登录每个用户
        for (User user : users){
            if (login(user)){
                System.out.printf("%s \t成功登陆， 用的密码是 %s\t", user.getName(), user.getPassword());
            }else{
                System.out.printf("%s \t登录失败， 用的密码是 %s\t", user.getName(), user.getPassword());
            }
        }

        // 判断能登录的用户是否拥有某个权限
        for (User user : users){
            for (String role : roles){

            }
        }
    }

    private static Subject getSubject(User user) {
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

    public static boolean hasRole(User user, String role){
        Subject subject = getSubject(user);
        return subject.hasRole(role);
    }

    private static boolean isPermitted(User user, String permit){
        Subject subject = getSubject(user);
        return subject.hasRole(permit);
    }

    private static boolean login(User user){
        Subject subject = getSubject(user);

        // 如果已经登录过了，退出
        if (subject.isAuthenticated()){
            subject.logout();
        }

        // 封装用户的数据
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getName(),user.getPassword());
        // 将用户的数据token UI中传递到Realm中进行对比
        subject.login(usernamePasswordToken);

        return subject.isAuthenticated();
    }
}
