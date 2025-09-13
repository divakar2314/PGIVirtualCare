package com.Myproject.PGIVirtualCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Myproject.PGIVirtualCare.Model.Prescription;
import com.Myproject.PGIVirtualCare.Model.Users;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

	List<Prescription> findByDoctor(Users doctor);

	
	List<Prescription> findByPatient(Users patient);

	

}
