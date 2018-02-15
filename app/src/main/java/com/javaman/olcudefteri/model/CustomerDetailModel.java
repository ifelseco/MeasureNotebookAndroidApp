package com.javaman.olcudefteri.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetailModel {

	@SerializedName("id")
	@Expose
	private Long id;

	@SerializedName("mobilePhone")
	@Expose
	private String mobilePhone;

	@SerializedName("nameSurname")
	@Expose
	private String nameSurname;

	@SerializedName("fixedPhone")
	@Expose
	private String fixedPhone;

	@SerializedName("address")
	@Expose
	private String address;

	@SerializedName("newsletterAccepted")
	@Expose
	private boolean newsletterAccepted;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNameSurname() {
		return nameSurname;
	}
	public void setNameSurname(String nameSurname) {
		this.nameSurname = nameSurname;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getFixedPhone() {
		return fixedPhone;
	}
	public void setFixedPhone(String fixedPhone) {
		this.fixedPhone = fixedPhone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isNewsletterAccepted() {
		return newsletterAccepted;
	}
	public void setNewsletterAccepted(boolean newsletterAccepted) {
		this.newsletterAccepted = newsletterAccepted;
	}
	
	
}
