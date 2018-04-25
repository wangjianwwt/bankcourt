package com.born.bc.sysconfig;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.born.bc.sysconfig.realm.MyShiroRealm;
import com.born.bc.sysconfig.utils.MyExceptionHandler;
import com.born.bc.sysconfig.utils.MySessionManager;

/**
 * shiro配置
 * 
 * @author wangjian
 *
 */
@Configuration
public class ShiroConfiguration {

	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.timeout}")
	private int timeout;
	@Value("${redis.password}")
	private String password;

	/**
	 * 配置shiro核心过滤器
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		// shiro核心安全接口
		bean.setSecurityManager(securityManager);
		// 身份认证失败跳转路径
		bean.setLoginUrl("/msg/sessionout");
		// 权限认证失败跳转路径
		bean.setUnauthorizedUrl("/msg/unauthorized");

		// 配置访问权限
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 托管请求
		filterChainDefinitionMap.put("/resources/**", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/api/**", "anon");
		filterChainDefinitionMap.put("/**/login", "anon");
		filterChainDefinitionMap.put("/msg/**", "anon");
		// 认证请求
		filterChainDefinitionMap.put("/user/**", "perms[user:*]");
		filterChainDefinitionMap.put("/role/**", "perms[role:*]");
		filterChainDefinitionMap.put("/permission/**", "perms[permission:*]");
		filterChainDefinitionMap.put("/**", "authc");

		bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return bean;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了 ）
	 * 
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashAlgorithmName("md5");
		// 散列的次数，比如散列两次，相当于 md5(md5(""));
		hashedCredentialsMatcher.setHashIterations(1);
		return hashedCredentialsMatcher;
	}

	/**
	 * 定义认证登陆器
	 * 
	 * @return
	 */
	@Bean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}

	/**
	 * 定义安全管理器
	 * @return
	 */
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		// 自定义session管理 使用redis
		securityManager.setSessionManager(sessionManager());
		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}

	/**
	 * 自定义sessionManager
	 * 
	 * @return
	 */
	@Bean
	public SessionManager sessionManager() {
		MySessionManager mySessionManager = new MySessionManager();
		mySessionManager.setSessionDAO(redisSessionDAO());
		return mySessionManager;
	}

	/**
	 * 配置shiro redisManager
	 * <p>
	 * 使用的是shiro-redis开源插件
	 * 
	 * @return
	 */
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(host);
		redisManager.setPort(port);
		redisManager.setExpire(1800);// 配置缓存过期时间
		redisManager.setTimeout(timeout);
		redisManager.setPassword(password);
		return redisManager;
	}

	/**
	 * cacheManager 缓存 redis实现
	 * <p>
	 * 使用的是shiro-redis开源插件
	 * 
	 * @return
	 */
	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis
	 * <p>
	 * 使用的是shiro-redis开源插件
	 */
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 注册全局异常处理
	 * 
	 * @return
	 */
	@Bean(name = "exceptionHandler")
	public HandlerExceptionResolver handlerExceptionResolver() {
		return new MyExceptionHandler();
	}

}
