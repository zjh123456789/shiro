package pers.zjh;

/**
 * @Description:     用户类，用于存放账号和密码
 * @Author:         Zhujinghui
 * @CreateDate:     2018/11/1 17:20
 */

public class User {

    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
