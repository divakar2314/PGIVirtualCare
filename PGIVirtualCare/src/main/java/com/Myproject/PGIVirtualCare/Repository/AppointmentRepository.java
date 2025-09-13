package com.Myproject.PGIVirtualCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Myproject.PGIVirtualCare.Model.Appointment;
import com.Myproject.PGIVirtualCare.Model.Appointment.AppointmentStatus;
import com.Myproject.PGIVirtualCare.Model.Users;

public interface AppointmentRepository  extends JpaRepository<Appointment, Long>{

	List<Appointment> findByPatient(Users patient);

	List<Appointment> findAllByPatient(Users admin);

	List<Appointment> findByDoctor(Users doctor);

	Object countByStatus(AppointmentStatus approved);

	
	
}
