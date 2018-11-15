package br.com.helpDesk.api.dto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.x509;

import java.io.Serializable;

public class Summary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer amountNew;
	private Integer amountResolved;
	private Integer amountApproved;
	private Integer amountDisapproved;
	private Integer amountAssigned;
	private Integer amountClosed;
	
	public Summary() {
		this.amountNew = 0;
		this.amountResolved = 0;
		this.amountApproved = 0;
		this.amountDisapproved = 0;
		this.amountAssigned = 0;
		this.amountClosed = 0;
	}
	
	public Integer getAmountNew() {
		return amountNew;
	}
	public void setAmountNew(Integer amountNew) {
		this.amountNew = amountNew;
	}
	public void sumAmountNew() {
		this.amountNew++;
	}
	public Integer getAmountResolved() {
		return amountResolved;
	}
	public void setAmountResolved(Integer amountResolved) {
		this.amountResolved = amountResolved;
	}
	public void sumAmountResolved() {
		this.amountResolved++;
	}
	public Integer getAmountApproved() {
		return amountApproved;
	}
	public void setAmountApproved(Integer amountApproved) {
		this.amountApproved = amountApproved;
	}
	public void sumAmountApproved() {
		this.amountApproved++;
	}
	public Integer getAmountDisapproved() {
		return amountDisapproved;
	}
	public void setAmountDisapproved(Integer amountDisapproved) {
		this.amountDisapproved = amountDisapproved;
	}
	public void sumAmountDisapproved() {
		this.amountDisapproved++;
	}
	public Integer getAmountAssigned() {
		return amountAssigned;
	}
	public void setAmountAssigned(Integer amountAssigned) {
		this.amountAssigned = amountAssigned;
	}
	public void sumAmountAssigned() {
		this.amountAssigned++;
	}
	public Integer getAmountClosed() {
		return amountClosed;
	}
	public void setAmountClosed(Integer amountClosed) {
		this.amountClosed = amountClosed;
	}
	public void sumAmountClosed() {
		this.amountClosed++;
	}
}
