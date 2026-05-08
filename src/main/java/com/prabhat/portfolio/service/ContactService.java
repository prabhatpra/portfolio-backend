package com.prabhat.portfolio.service;

import java.util.List;

import com.prabhat.portfolio.dto.RequestDto;
import com.prabhat.portfolio.dto.ResponseDto;
import com.prabhat.portfolio.enums.ContactStatus;

public interface ContactService {

	public ResponseDto contactUser(RequestDto request);

	List<ResponseDto> getAllContacts();
	
	ResponseDto getContactById(Long id);
	
	void deleteContact(Long id);
	
	public void updateStatus(Long id, ContactStatus status);

}
