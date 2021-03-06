package br.com.helpDesk.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.helpDesk.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
	
	Page<Ticket> findByUserIdOrderByDateDesc(Pageable pages, String userId);
	
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPrioridadeOrderByDateDesc
		(Pageable pages, String title, String status, String prioridade);
	
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPrioridadeAndUserIdOrderByDateDesc
		(Pageable pages, String title, String status, String prioridade, String userId);
	
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPrioridadeAndAssignedIdOrderByDateDesc
		(Pageable pages, String title, String status, String prioridade, String assignedId);
	
	Page<Ticket> findByNumber(Pageable pages, Integer number);
	
}
