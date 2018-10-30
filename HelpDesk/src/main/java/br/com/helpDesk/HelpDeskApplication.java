package br.com.helpDesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.helpDesk.api.repository.UserRepository;
import br.com.helpDesk.entity.User;
import br.com.helpDesk.enums.ProfileEnum;

@SpringBootApplication
public class HelpDeskApplication {

	private String EMAIL = "admin@helpDesk.com";
	
	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUser(userRepository, passwordEncoder);
		};
	}
	
	private void initUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		User admin = new User();
		admin.setEmail(EMAIL);
		admin.setPassword(passwordEncoder.encode("123456"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);
		
		User find = userRepository.findByEmail(EMAIL);
		if(find == null) userRepository.save(admin);
	}
	
	
}
