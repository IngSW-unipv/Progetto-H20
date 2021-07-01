package carpool;

import java.util.List;

import carpool.errors.ErrorMessage;

//Questa classe era un esperimento per l'errore di login con dati errati nel modal, inutile per ora
public class ValidationResponse {
	
	private String status;
	private List<ErrorMessage> errorMessageList;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ErrorMessage> getErrorMessageList() {
		return errorMessageList;
	}
	public void setErrorMessageList(List<ErrorMessage> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}
}