package br.com.helpDesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.helpDesk.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByEmail(String email);
}
