package com.mvp.vending;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mvp.vending.controller.UserDetailsServiceImpl;
import com.mvp.vending.entity.Coin;
import com.mvp.vending.entity.Machine;
import com.mvp.vending.entity.Product;
import com.mvp.vending.entity.Role;
import com.mvp.vending.entity.User;
import com.mvp.vending.repository.CoinRepository;
import com.mvp.vending.repository.MachineRepository;
import com.mvp.vending.repository.ProductRepository;
import com.mvp.vending.repository.UserRepository;

/**
 * Created by Majied on 05/10/2021.
 */
@SpringBootApplication
public class MvpVendingMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvpVendingMachineApplication.class, args);
	}
	

    /**
     * Create bean using CommandLineRunner to initialize 
     * vending machines and other models
     * @param productRepository product repository
     * @param machineRepository machine repository
     * @param coinRepository    coin repository
     * @param coinRepository    coin repository
     */	
	@Bean
	CommandLineRunner init(MachineRepository machineRepository,
						   ProductRepository productRepository,
						   CoinRepository coinRepository,
						   UserRepository userRepository) {
		return (evt) -> Arrays.asList(
				"Vendingmachine1,Vendingmachine2".split(","))
				.forEach(
						a -> {
							// Create the new vending machine
							Machine machine = machineRepository.save(new Machine(a, 0));

							// Add 3 default products
							productRepository.save(new Product(machine,
									"Coca Cola", 120,10));
							productRepository.save(new Product(machine,
									"Chocolates", 300, 5));
							productRepository.save(new Product(machine,
									"Chips", 50, 5));	

							// Add some cash float to the machine
							coinRepository.save(new Coin(machine, 50, 6));
							coinRepository.save(new Coin(machine, 100, 2));
							coinRepository.save(new Coin(machine, 20, 4));
							coinRepository.save(new Coin(machine, 5, 10));
							
							//TO DO
							//USER CREATION NEEDS TO BE OPTIMIZED

							Role role1= new Role("SELLER");
							Role role2= new Role("BUYER");								
							
							User user1= new User ("USER1","password",false);
							User user2= new User ("USER2","password",false);
							
							Set roleSet1 = new HashSet();
							roleSet1.add(user1.getId());
							roleSet1.add(role1.getId());
							
							Set roleSet2 = new HashSet();
							roleSet2.add(user2.getId());
							roleSet2.add(role2.getId());							
							//user1 with seller role
							user1.setRoles(roleSet1);
							//user2 with buyer role
							user2.setRoles(roleSet2);
							
						});
	}
	
	@Configuration
	public class MvcConfig implements WebMvcConfigurer {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/403").setViewName("403");
		}
	}
	
	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	 
	    @Bean
	    public UserDetailsService userDetailsService() {
	        return (UserDetailsService) new UserDetailsServiceImpl();
	    }
	     
	    @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	     
	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());
	         
	        return authProvider;
	    }
	 
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.authenticationProvider(authenticationProvider());
	    }
	 
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.authorizeRequests()
	            .antMatchers("/").hasAnyAuthority("SELLER", "BUYER")
//	            .antMatchers("/*/coins/refund").hasAnyAuthority("SELLER")
//	            .antMatchers("/*/*/add").hasAnyAuthority("BUYER")
//	            .anyRequest().authenticated()
//	            .and()
//	            .formLogin().permitAll()
//	            .and()
//	            .logout().permitAll()
	            .and()
	            .exceptionHandling().accessDeniedPage("/403")
	            ;
	    }
	    
	    //Allow POST requests
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/*/**", "/*/**");

		}
	}	
	
}
