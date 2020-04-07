package onlineShop;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	protected void configure(HttpSecurity http) throws Exception {
		//admin和用户的权限
		//* 只能match一个层级
		//** match多个层级
		
		//csrf:跨域请求伪造（cross-site request forgery) 
		//这种方法用来防止csrf的攻击，本质是发一个token给你，但是不把这个信息保存在cookie里面
		//这样钓鱼网站就没有办法通过伪造请求来拿到cookie里面的信息
		//本项目不涉及这个问题，所以把默认的csrf()disable掉
		http.csrf().disable().formLogin().loginPage("/login")

				.and().authorizeRequests().antMatchers("/cart/**").hasAuthority("ROLE_USER").antMatchers("/get*/**")
				.hasAnyAuthority("ROLE_USER", "ROLE_ADMIN").antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")
				.anyRequest().permitAll().and().logout().logoutUrl("/logout");

	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin@gmail.com").password("123").authorities("ROLE_ADMIN");
		//可以在这里继续加admin用户
		//或者在db里面直接更改现有user权限

		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")
				.authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");

	}

	@SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

}
