package com.Myproject.PGIVirtualCare.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.Myproject.PGIVirtualCare.Model.Appointment;
import com.Myproject.PGIVirtualCare.Model.Appointment.AppointmentStatus;
import com.Myproject.PGIVirtualCare.Model.Prescription;
import com.Myproject.PGIVirtualCare.Model.Users;
import com.Myproject.PGIVirtualCare.Repository.AppointmentRepository;
import com.Myproject.PGIVirtualCare.Repository.PrescriptionRepository;
import com.Myproject.PGIVirtualCare.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Doctor")
public class DoctorContoller {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserRepository UserRepo;
	
	@Autowired
	private AppointmentRepository appointmentRepo;
	
	
	
	@Autowired
	private PrescriptionRepository prescriptionRepo;
	
	@GetMapping("/DoctorDashboard")
	public String ShowDoctorDashboard() {
		if(session.getAttribute("loggedInDoctor")==null) {
			return "redirect:/DoctorLogin";
			
		}
		
		return "Doctor/DoctorDashboard";
		
		
	}
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/ManageMedicalReports")
	public String ShowManageMedicalReports() {
		if(session.getAttribute("loggedInDoctor")==null) {
			return "redirect:/DoctorLogin";
			
		}
		
		return "Doctor/ManageMedicalReports";
		
		
	}

	
	@GetMapping("/ViewProfile")
	public String ShowViewProfile() {
		if(session.getAttribute("loggedInDoctor")==null) {
			return "redirect:/DoctorLogin";
			
		}
		
		return "Doctor/ViewProfile";
		
		
	}
	
	
	
	// Change password
		@GetMapping("/ChangeDoctorPassword")
		public String ShowChangeDoctorPassword() {
			
			if(session.getAttribute("loggedInDoctor")==null) {
				return "redirect:/DoctorLogin";
				
			}
			
			return "Doctor/ChangeDoctorPassword";
		}
		
		
		// post mapping
		@PostMapping("/ChangeDoctorPassword")
		public String ChangePatientPassword(HttpServletRequest request,RedirectAttributes attributes) {
			try {
				
				String oldPass = request.getParameter("oldPassword");
				String newPass = request.getParameter("newPassword");
				String confirmPass =request.getParameter("confirmPassword");
				
				if(!newPass.equals(confirmPass)) {
					attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same");
					return "redirect:/Doctor/ChangeDoctorPassword";
					
				}
				
				Users doctor = (Users) session.getAttribute("loggedInDoctor");
				
				if(oldPass.equals(doctor.getPassword())) {
					doctor.setPassword(confirmPass);
					UserRepo.save(doctor);
					session.removeAttribute("loggedInDoctor");
					attributes.addFlashAttribute("msg", "Password succesfully Change");
					return "redirect:/DoctorLogin";
					
				}
				else {
					attributes.addFlashAttribute("msg", "Invalid Old Password");
					
					
				}
				
				return "redirect:/Doctor/ChangeDoctorPassword";
				
			} catch (Exception e) {
				attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
				return "redirect:/Doctor/ChangeDoctorPassword";
			}
			
			
		}
		
	
	
	
	
	
	@GetMapping("/Logout")
	public String Logout(RedirectAttributes attributes) {
		
		session.removeAttribute("loggedInDoctor");
		attributes.addFlashAttribute("msg", "Logout Successfully");
		return "redirect:/DoctorLogin";   
	 
		
	}
	
	
	@GetMapping("/ViewAppointments")
	public String ShowViewAppointments(Model model) {
		if(session.getAttribute("loggedInDoctor")==null) {
			return "redirect:/DoctorLogin";
			
		}
		
		
		 Users doctor =(Users) session.getAttribute("loggedInDoctor");
		 List<Appointment> appointments = appointmentRepo.findByDoctor(doctor);
		 model.addAttribute("appointments", appointments);
		 
		 
		
		return "Doctor/ViewAppointments";
		
		
	}
	
	
	
	
	@GetMapping("/PatientStatus")
	public String UpdatePatientStatus(@RequestParam("id") long id, RedirectAttributes attributes) {
		
		try {
			Appointment appointment = appointmentRepo.findById(id).get();
			if(appointment.getStatus().equals(AppointmentStatus.PENDING)) {
				appointment.setStatus(AppointmentStatus.APPROVED);
				appointmentRepo.save(appointment);
				

				
			}
			else if(appointment.getStatus().equals(AppointmentStatus.APPROVED)) {
				
				appointment.setStatus(AppointmentStatus.REJECTED);
				appointmentRepo.save(appointment);
				
			}
			else {
				appointment.setStatus(AppointmentStatus.APPROVED);
				appointmentRepo.save(appointment);
			}
			attributes.addFlashAttribute("msg", "User Status successfully Update");
			return "redirect:/Doctor/ViewAppointments";
			
		} catch (Exception e) {
			return "redirect:/Doctor/ViewAppointments";
		}
	}
	
	
	@GetMapping("/Reject")
	public String RejectStatus(@RequestParam("id") long id, RedirectAttributes attributes) {
		
		try {
			Appointment appointment = appointmentRepo.findById(id).get();
			if(appointment.getStatus().equals(AppointmentStatus.REJECTED)) {
				appointment.setStatus(AppointmentStatus.APPROVED);
				appointmentRepo.save(appointment);
				

				
			}
			else if(appointment.getStatus().equals(AppointmentStatus.APPROVED)) {
				
				appointment.setStatus(AppointmentStatus.REJECTED);
				appointmentRepo.save(appointment);
				
			}
			else {
				appointment.setStatus(AppointmentStatus.APPROVED);
				appointmentRepo.save(appointment);
			}
			attributes.addFlashAttribute("msg", "User Status successfully Update");
			return "redirect:/Doctor/ViewAppointments";
			
		} catch (Exception e) {
			return "redirect:/Doctor/ViewAppointments";
		}
	}
	
	
	
	
	@GetMapping("/DeletePatient")
	public String DeletePatient(@RequestParam("id") long id) {
		appointmentRepo.deleteById(id);
		return "redirect:/Doctor/ViewAppointments";
		
	}
	
	
	
	
	@GetMapping("/WritePrescription")
	public String ShowWritePrescription(@RequestParam("id") long id, Model model) {
		if(session.getAttribute("loggedInDoctor")==null) {
			return "redirect:/DoctorLogin";
			
		}
		Appointment appointment = appointmentRepo.findById(id).get();	
		model.addAttribute("appointment", appointment);
		
		Prescription prescription = new Prescription();
		model.addAttribute("prescription", prescription);
		return "Doctor/WritePrescription";
		
		
	}
	
	@PostMapping("/WritePrescription")
	public String WritePrescription(@ModelAttribute Prescription prescription, RedirectAttributes attributes, @RequestParam("appointmentId") long appointmentId) {
		try {
			
			Appointment appointment =appointmentRepo.findById(appointmentId).get();
			prescription.setAppointment(appointment);
			prescription.setDoctor(appointment.getDoctor());
			prescription.setPatient(appointment.getPatient());
			prescription.setPrescriptionDate(LocalDateTime.now());
			prescriptionRepo.save(prescription);
			attributes.addFlashAttribute("msg", "Prescription Succesfully submited");
			return "redirect:/Doctor/ViewAppointments";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Doctor/WritePrescription?id="+appointmentId;
		}
		
	}
	
	
	@GetMapping("/ManagePrescriptions")
	public String ShowManagePrescriptions(Model model) {
		if(session.getAttribute("loggedInDoctor")==null) {
			return "redirect:/DoctorLogin";
			
		}
		
		
		 Users doctor =(Users) session.getAttribute("loggedInDoctor");
		 List<Prescription> prescriptions = prescriptionRepo.findByDoctor(doctor);
		model.addAttribute("prescriptions", prescriptions);
		 
		 
		
		return "Doctor/ManagePrescriptions";
		
		
	}
	
	
	
	
	
	
	

}
