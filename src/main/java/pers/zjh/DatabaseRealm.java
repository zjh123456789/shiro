package pers.zjh;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @Description:     通过数据库验证用户 和相关授权的类
 * @Author:         Zhujinghui
 * @CreateDate:     2018/11/2 15:23
 */

public class DatabaseRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        // 通过DAO 获取角色和权限
        Set<String> permissions = new DAO().listPermissions(userName);
        Set<String> roles = new DAO().listRoles(userName);

        // 授权对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 把通过DAO获取到的角色和权限放进去
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取账号密码
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getPrincipal().toString();
        String password = new String(token.getPassword());
        // 获取账号中的密码
        User user = new DAO().getUser(userName);
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        String passwordEncoded = new SimpleHash("md5",password,salt,2).toString();

        // 如果为空就是账号不存在，如果不相同就是密码错误，但是都抛出AuthenticationException, 而不是抛出具体错误原因，免得给破解者提供帮助信息
        if (null == passwordInDB || !passwordInDB.equals(password)){
            throw new AuthenticationException();
        }

        // 认证信息里存放账号密码， getName() 是当前Realm 的继承方法, 通常返回当前类名: databaseRealm
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password,getName());
        return authenticationInfo;
    }
}
