package com.ef.model;

import java.util.ArrayList;
import java.util.List;

public class ClientLogger {
	private String ip;
	private List<LogRecord> list = new ArrayList<>();
	
	//Number of request this client made in a specific period
	private int numOfRequest;
	
	public ClientLogger() {
		numOfRequest = 0;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<LogRecord> getList() {
		return list;
	}

	public void setList(List<LogRecord> list) {
		this.list = list;
	}

	public int getNumOfRequest() {
		return numOfRequest;
	}
	
	public void addOneRequest() {
		numOfRequest++;
	}

	public void setNumOfRequest(int numOfRequest) {
		this.numOfRequest = numOfRequest;
	}
	
	
}
