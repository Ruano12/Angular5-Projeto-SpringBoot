package br.com.helpDesk.api.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.helpDesk.api.dto.Summary;
import br.com.helpDesk.api.response.Response;
import br.com.helpDesk.api.security.jwt.JwtTokenUtil;
import br.com.helpDesk.api.service.TicketService;
import br.com.helpDesk.api.service.UserService;
import br.com.helpDesk.entity.ChangeStatus;
import br.com.helpDesk.entity.Ticket;
import br.com.helpDesk.entity.User;
import br.com.helpDesk.enums.ProfileEnum;
import br.com.helpDesk.enums.StatusEnum;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	protected JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> create(HttpServletRequest request, @RequestBody Ticket ticket, 
			BindingResult result){
		Response<Ticket> response = new Response<Ticket>();
		
		try {
			validateCreateTicket(ticket, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			ticket.setStatus(StatusEnum.New);
			ticket.setUser(userFromRequest(request));
			ticket.setDate(Calendar.getInstance().getTime());
			ticket.setNumber(generateNumber());
			Ticket ticketPersist = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersist);
		}catch(Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateCreateTicket(Ticket ticket, BindingResult result) {
		if(ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Titulo não informado"));
		}
	}
	
	private User userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}
	
	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt();
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket, 
			BindingResult result){
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateUpdateTicket(ticket, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(ticket.getId()).get();
			ticket.setStatus(ticketCurrent.getStatus());
			ticket.setUser(ticketCurrent.getUser());
			ticket.setDate(ticketCurrent.getDate());
			ticket.setNumber(ticketCurrent.getNumber());
			if(ticketCurrent.getAssigned() != null) {
				ticket.setAssigned(ticketCurrent.getAssigned());
			}
			Ticket ticketPersist = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersist);
		}catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateUpdateTicket(Ticket ticket, BindingResult result) {
		if(ticket.getId() == null) {
			result.addError(new ObjectError("Ticket", "Id não informado"));
		}
		if(ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Titulo não informado"));
		}
	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id){
		Response<Ticket> response = new Response<Ticket>();
		Ticket ticket = ticketService.findById(id).get();
		if(ticket == null) {
			response.getErrors().add(new StringBuilder().append("Registro não encontrado id:").append(id).toString());
			return ResponseEntity.badRequest().body(response);
		}
		List<ChangeStatus> changes = new ArrayList<ChangeStatus>();
		Iterable<ChangeStatus> changesCurrent = ticketService.listChangeStatus(ticket.getId());
		for (Iterator iterator = changesCurrent.iterator(); iterator.hasNext();) {
			ChangeStatus changeStatus = (ChangeStatus) iterator.next();
			changeStatus.setTicket(null);
			changes.add(changeStatus);
		}
		ticket.setChanges(changes);
		response.setData(ticket);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id){
		Response<String> response = new Response<String>();
		Ticket ticket = ticketService.findById(id).get();
		if(ticket == null) {
			response.getErrors().add(new StringBuilder().append("Registro não encontrado id:").append(id).toString());
			return ResponseEntity.badRequest().body(response);
		}
		ticketService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest request, @PathVariable("page") Integer page, @PathVariable("count") Integer count){
		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket> tickets = null;
		User userRequest = userFromRequest(request);
		if(userRequest.getProfile().equals(ProfileEnum.ROLE_TECHNICIAN)) {
			tickets = ticketService.listTicket(page, count);			
		}else if(userRequest.getProfile().equals(ProfileEnum.ROLE_CUSTOMER)) {
			tickets = ticketService.findByCurrentUser(page, count, userRequest.getId());
		}
		response.setData(tickets);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "{page}/{count}/{number}/{title}/{status}/{priority}/{assigned}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findByParams(HttpServletRequest request, 
					@PathVariable("page") Integer page, 
					@PathVariable("count") Integer count,
					@PathVariable("number") Integer number, 
					@PathVariable("title") String title, 
					@PathVariable("status") String status,
					@PathVariable("priority") String priority, 
					@PathVariable("assigned") boolean assigned){
		
		title = title.equals("uninformed") ? "" : title;
		status = status.equals("uninformed") ? "" : status;
		priority = priority.equals("uninformed") ? "" : priority;
		
		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket> tickets = null;
		if(number > 0) {
			tickets = ticketService.findByNumber(page, count, number);
		}else {
			User userRequest = userFromRequest(request);
			if(userRequest.getProfile().equals(ProfileEnum.ROLE_TECHNICIAN)) {
				if(assigned) {
					tickets = ticketService.findByParameterAndAssignedUser(page, count, title, status, priority, userRequest.getId());
				}else {
					tickets = ticketService.findByParameters(page, count, title, status, priority);
				}
			}else if(userRequest.getProfile().equals(ProfileEnum.ROLE_CUSTOMER)) {
				tickets = ticketService.findByParametersAndCurrentUser(page, count, title, status, priority, userRequest.getId());
			}
		}
		
		response.setData(tickets);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping(value = "{id}/{status}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> changeStatus(
			@PathParam("id") String id,
			@PathParam("status") String status,
			HttpServletRequest request,
			@RequestBody Ticket ticket,
			BindingResult result){
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateChangeStatus(id, status, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(id).get();
			ticketCurrent.setStatus(StatusEnum.getStatus(status));
			if(status.equals("Assigned")) {
				ticketCurrent.setAssigned(userFromRequest(request));
			}
			Ticket ticketPersist = (Ticket) ticketService.createOrUpdate(ticketCurrent);
			ChangeStatus changeStatus = new ChangeStatus();
			changeStatus.setUserChange(userFromRequest(request));
			changeStatus.setDateChangeStatus(Calendar.getInstance().getTime());
			changeStatus.setStatus(StatusEnum.getStatus(status));
			changeStatus.setTicket(ticketPersist);
			ticketService.createChangeStatus(changeStatus);
			response.setData(ticketPersist);
		}catch(Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateChangeStatus(String id, String status, BindingResult result) {
		if(id == null || id.equals("")) {
			result.addError(new ObjectError("id", "Id não informado"));
		}
		if(status == null || status.equals("")) {
			result.addError(new ObjectError("status", "status não informado"));
		}
	}
	
	@PutMapping(value = "/summary")
	public ResponseEntity<Response<Summary>> findSummary(){
		Response<Summary> response = new Response<Summary>();
		Summary summary = new Summary();
		
		Iterable<Ticket> tickets = ticketService.findAll();
		for(Iterator<Ticket> iterator = tickets.iterator(); iterator.hasNext();) {
			Ticket ticket = (Ticket) iterator.next();
			if(ticket.getStatus().equals(StatusEnum.New)) {
				summary.sumAmountNew();
			}else if(ticket.getStatus().equals(StatusEnum.Resolved)) {
				summary.sumAmountResolved();
			}else if(ticket.getStatus().equals(StatusEnum.Approved)) {
				summary.sumAmountApproved();
			}else if(ticket.getStatus().equals(StatusEnum.Disapproved)) {
				summary.sumAmountDisapproved();
			}else if(ticket.getStatus().equals(StatusEnum.Assigned)) {
				summary.sumAmountAssigned();
			}else if(ticket.getStatus().equals(StatusEnum.Closed)) {
				summary.sumAmountClosed();
			}
		}
		response.setData(summary);
		return ResponseEntity.ok(response);
	}
}
