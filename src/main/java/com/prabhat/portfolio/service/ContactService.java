package com.prabhat.portfolio.service;

import java.util.List;

import com.prabhat.portfolio.dto.RequestDto;
import com.prabhat.portfolio.dto.ResponseDto;

public interface ContactService {

	public ResponseDto contactUser(RequestDto request);

	List<ResponseDto> getAllContacts();
	
	ResponseDto getContactById(Long id);
	
	void deleteContact(Long id);
	
	public void updateStatus(Long id, String status);

}
