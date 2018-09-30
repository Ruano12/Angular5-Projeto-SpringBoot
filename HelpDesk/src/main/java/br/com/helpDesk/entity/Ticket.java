package br.com.helpDesk.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.helpDesk.enums.PriorityEnum;
import br.com.helpDesk.enums.PriorityEnum;
import br.com.helpDesk.enums.StatusEnum;

@Document
public class Ticket {

	@Id
	private String id;
	
	@DBRef(lazy = true)
	private User user;
	
	private Date date;
	
	private String title;
	
	private Integer number;
	
	private StatusEnum status;
	
	private PriorityEnum prioridade;
	
	@DBRef(lazy = true)
	private User assigned;
	
	private String description;
	
	private String image;
	
	private List<ChangeStatus> changes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public PriorityEnum getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(PriorityEnum prioridade) {
		this.prioridade = prioridade;
	}

	public User getAssigned() {
		return assigned;
	}

	public void setAssigned(User assigned) {
		this.assigned = assigned;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<ChangeStatus> getChanges() {
		return changes;
	}

	public void setChanges(List<ChangeStatus> changes) {
		this.changes = changes;
	}
	
	
}
