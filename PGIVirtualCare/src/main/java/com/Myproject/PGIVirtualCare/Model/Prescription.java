package com.Myproject.PGIVirtualCare.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Prescription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pid;
	
	@Column(nullable = false)
	private String medicationDetails;
	
	@Column(nullable = false,length =500)
	private String advice;
	
	@Column(nullable = false,length =500)
	private String diagonsis;
	
	private LocalDateTime prescriptionDate;
	
	@ManyToOne
	private Appointment appointment;
	
	@ManyToOne
	private Users patient;
	@ManyToOne
	private Users doctor;
	
	

	public Users getPatient() {
		return patient;
	}

	public void setPatient(Users patient) {
		this.patient = patient;
	}

	public Users getDoctor() {
		return doctor;
	}

	public void setDoctor(Users doctor) {
		this.doctor = doctor;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getMedicationDetails() {
		return medicationDetails;
	}

	public void setMedicationDetails(String medicationDetails) {
		this.medicationDetails = medicationDetails;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getDiagonsis() {
		return diagonsis;
	}

	public void setDiagonsis(String diagonsis) {
		this.diagonsis = diagonsis;
	}

	public LocalDateTime getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(LocalDateTime prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	

	

	
}
