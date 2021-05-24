package com.baizhi.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.baizhi.shiro.cache.RedisCacheManager;
import com.baizhi.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//用来整合shiro相关的控制类
@Configuration
public class ShiroConfig {
    @Bean(name="shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect() ;
    }

    //创建shiroFilter         //负责拦截所有的请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean() ;
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //配置系统的受限资源
        //配置系统公共资源
        Map<String,String> map = new HashMap<>() ;
        map.put("/login.html","anon") ;
        map.put("/user/getImage","anon") ;
        map.put("/user/register","anon") ;
        map.put("/user/registervie","anon") ;
        map.put("/user/login","anon") ;
        map.put("/**","authc") ;//请求这个资源需要认证和授权

        //默认认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/user/loginvie");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean ;

    }

    //2创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getRealm") Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置realm
        defaultWebSecurityManager.setRealm(realm);

        return defaultWebSecurityManager ;
    }
    //3创建自定义realm
    @Bean
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();

        //修改凭证校验匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
//        设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("MD5");
//        设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);

//        开启缓存管理

        customerRealm.setCacheManager(new RedisCacheManager());
        customerRealm.setCachingEnabled(true);//开启全局缓存
        customerRealm.setAuthenticationCachingEnabled(true);//启动认证缓存
        customerRealm.setAuthenticationCacheName("authenticationCache");
        customerRealm.setAuthorizationCachingEnabled(true);//启动授权缓存
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }
}
