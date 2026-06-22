package com.web.store.order.dto;

import jakarta.validation.constraints.NotBlank;

public class PlaceOrderRequest {

    @NotBlank
    private String deliveryAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String phone;

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    // getters setters
}