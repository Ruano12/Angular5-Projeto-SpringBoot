package br.com.helpDesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.helpDesk.entity.ChangeStatus;

public interface ChangeStatusRepository extends MongoRepository<ChangeStatus, String> {
	
	Iterable<ChangeStatus> findByTicketIdOrderByDateChangeStatusDesc(String ticketId);

}
