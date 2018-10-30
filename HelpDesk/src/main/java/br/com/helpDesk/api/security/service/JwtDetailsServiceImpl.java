package br.com.helpDesk.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.helpDesk.api.security.jwt.JwtUserFactory;
import br.com.helpDesk.api.service.UserService;
import br.com.helpDesk.entity.User;

@Service
public class JwtDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("No user foud with username '%s'.", email));
		}else {
			return JwtUserFactory.create(user);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
