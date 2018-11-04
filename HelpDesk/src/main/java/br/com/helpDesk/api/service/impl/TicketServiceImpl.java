package br.com.helpDesk.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.helpDesk.api.repository.ChangeStatusRepository;
import br.com.helpDesk.api.repository.TicketRepository;
import br.com.helpDesk.api.service.TicketService;
import br.com.helpDesk.entity.ChangeStatus;
import br.com.helpDesk.entity.Ticket;
import br.com.helpDesk.entity.User;

@Service
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	private TicketRepository ticketrepository;

	@Autowired
	private ChangeStatusRepository changeStatusRepository;
	
	@Override
	public Ticket createOrUpdate(Ticket ticket) {
		return this.ticketrepository.save(ticket);
	}

	@Override
	public Optional<Ticket> findById(String id) {
		return this.ticketrepository.findById(id);
	}

	@Override
	public void delete(String id) {
		this.ticketrepository.deleteById(id);
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketrepository.findAll(pages);
	}

	@Override
	public ChangeStatus createChangeStatus(ChangeStatus changeStatus) {
		return this.changeStatusRepository.save(changeStatus);
	}

	@Override
	public Iterable<ChangeStatus> listChangeStatus(String ticketId) {
		return this.changeStatusRepository.findByTicketIdOrderByDateChangeStatusDesc(ticketId);
	}

	@Override
	public Page<Ticket> findByCurrentUser(int page, int count, String userId) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketrepository.findByUserIdOrderByDateDesc(pages, userId);
	}

	@Override
	public Page<Ticket> findByParameters(int page, int count, String title, String status, String priority) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketrepository.findByTitleIgnoreCaseContainingAndStatusAndPrioridadeOrderByDateDesc(pages, title, status, priority);
	}

	@Override
	public Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status,
			String priority, String userId) {
		Pageable pages = new PageRequest(page, count);
		return this.findByParametersAndCurrentUser(page, count, title, status, priority, userId);
	}

	@Override
	public Page<Ticket> findByNumber(int page, int count, Integer number) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketrepository.findByNumber(pages, number);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return this.ticketrepository.findAll();
	}

	@Override
	public Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status,
			String priority, String assignedUserId) {
		Pageable pages = new PageRequest(page, count);
		return this.findByParameterAndAssignedUser(page, count, title, status, priority, assignedUserId);
	}
	
	
	
}
