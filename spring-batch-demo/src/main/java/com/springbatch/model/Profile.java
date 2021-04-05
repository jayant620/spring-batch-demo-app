package com.springbatch.model;

public class Profile {
	
	
	private long empCode;
	
	private String empName;
	
	private String profileName;
	
	public Profile(long empCode, String empName, String profileName) {
		this.empCode = empCode;
		this.empName = empName;
		this.profileName = profileName;
	}
	
	public Profile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getEmpCode() {
		return empCode;
	}
	public void setEmpCode(long empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	@Override
	public String toString() {
		return "Profile [empCode=" + empCode + ", empName=" + empName + ", profileName=" + profileName + "]";
	}
	
}