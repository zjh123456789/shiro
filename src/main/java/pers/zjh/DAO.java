package pers.zjh;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description:     提供权限相关查询
 * @Author:         Zhujinghui
 * @CreateDate:     2018/11/2 11:44
 */

public class DAO {

    /**
     * @Describe    主函数入口
     * @param
     * @return
     */
    public static void main(String[] args) {
        System.out.println(new DAO().getPassword("Tom"));
        System.out.println(new DAO().getPassword("Jerry"));
        System.out.println(new DAO().listRoles("Tom"));
        System.out.println(new DAO().listRoles("Jerry"));
        System.out.println(new DAO().listPermissions("Tom"));
        System.out.println(new DAO().listPermissions("Jerry"));
    }

    /**
     * @Describe    DAO 构造函数里连接驱动
     * @param
     * @return
     */
    public DAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * @Describe    建立数据库连接
     * @param
     * @return      Connection
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/shiro?characterEncoding=UTF-8&useSSL=false", "root", "admin");
    }

    /**
     * @Describe    根据用户名取密码
     * @param       userName
     * @return      password
     */
    public String getPassword(String userName){
        String sql = "select password from user where name = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Describe    根据用户名获取对应的角色
     * @param       userName
     * @return      roles
     */
    public Set<String> listRoles(String userName){
        Set<String> roles = new HashSet<>();
        String sql = "select r.name from user u " +
                "left join user_role ur on u.id = ur.user_id " +
                "left join Role r on r.id = ur.role_id " +
                "where u.name = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                roles.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * @Describe    根据用户名获取对应的权限
     * @param       userName
     * @return      permissions
     */
    public Set<String> listPermissions(String userName){
        Set<String> permissions = new HashSet<>();
        String sql = "select p.name from user u " +
                "left join user_role ru on u.id = ru.user_id " +
                "left join role r on r.id = ru.role_id " +
                "left join role_permission rp on r.id = rp.role_id " +
                "left join permission p on p.id = rp.permission_id " +
                "where u.name = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                permissions.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    /**
     * @Describe    注册用户, 将密码加密
     * @param
     * @return
     */
    public String createUser(String name, String password){
        String sql = "insert into user values(null,?,?,?)";

        String salt = new SecureRandomNumberGenerator().nextBytes().toString(); // 盐量随机
        String encodedPassword = new SimpleHash("md5",password,salt,2).toString();

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,encodedPassword);
            preparedStatement.setString(3,salt);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Describe    取出用户信息
     * @param
     * @return
     */
    public User getUser(String userName){
        User user = null;
        String sql = "select * from user where name = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setSalt(resultSet.getString("salt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
