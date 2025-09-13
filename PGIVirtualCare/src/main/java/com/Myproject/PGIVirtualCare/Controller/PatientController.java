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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Myproject.PGIVirtualCare.Model.Appointment;
import com.Myproject.PGIVirtualCare.Model.Prescription;
import com.Myproject.PGIVirtualCare.Model.Appointment.AppointmentStatus;
import com.Myproject.PGIVirtualCare.Model.Users;
import com.Myproject.PGIVirtualCare.Model.Users.UserRole;
import com.Myproject.PGIVirtualCare.Repository.AppointmentRepository;
import com.Myproject.PGIVirtualCare.Repository.PrescriptionRepository;
import com.Myproject.PGIVirtualCare.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Patient")
public class PatientController {
	
	@Autowired
	private HttpSession session;
	@Autowired
	private UserRepository UserRepo;
	
	@Autowired
	private AppointmentRepository appointmentRepo;
	
	@Autowired
	private PrescriptionRepository prescriptionRepo;
	
	@GetMapping("/PatientDashboard")
	public String ShowPatientDashboard() {
		if(session.getAttribute("loggedInPatient")==null) {
			return "redirect:/PatientLogin";
			
		}
		
		return "Patient/PatientDashboard";
		
		
	}
	
	@GetMapping("/BookAppoinments")
	public String ShowBookAppointment( Model model) {
		if(session.getAttribute("loggedInPatient")==null) {
			return "redirect:/PatientLogin";
		}
		
		 List<Users> doctorList = UserRepo.findAllByRole(UserRole.DOCTOR);
		 model.addAttribute("doctorList", doctorList);
		 
		 
		Appointment appointment = new Appointment();
		model.addAttribute("appointment", appointment);
		return "Patient/BookAppointement";
	}
	
	// postmapping  start BookAppointment
	
	@PostMapping("/BookAppoinments")
	public String BookAppointment(@ModelAttribute(" appointment") Appointment appointment ,RedirectAttributes attributes) {
		
		try {
			
			Users patient =(Users) session.getAttribute("loggedInPatient");
			
			appointment.setPatient(patient);
			appointment.setDepartment(appointment.getDoctor().getSpecialization());
			appointment.setBookedAt(LocalDateTime.now());
			appointment.setStatus(AppointmentStatus.PENDING);
			
			appointmentRepo.save(appointment);
			attributes.addFlashAttribute("msg", "Appointments successfully");
			
			
			
			
			return "redirect:/Patient/BookAppoinments";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Patient/BookAppoinments";
		}
		
	}
	
	
	
	@GetMapping("/ViewAppointment")
	public String ShowViewAppointment(Model model) {
		if(session.getAttribute("loggedInPatient")==null) {
			return "redirect:/PatientLogin";
			
		}
		
		   Users patient =(Users) session.getAttribute("loggedInPatient");
		   List<Appointment> appointments  = appointmentRepo.findByPatient(patient);
		   model.addAttribute("appointments", appointments);
		 
		
		return "Patient/ViewAppointment";
		
		
	}
	
	
	
	// Postmapiing end Book Appoinments
	
	
	// Change password
	@GetMapping("/ChangePatientPassword")
	public String ShowChangePatientPassword() {
		
		if(session.getAttribute("loggedInPatient")==null) {
			return "redirect:/PatientLogin";
			
		}
		
		return "Patient/ChangePatientPassword";
	}
	
	
	// post mapping
	@PostMapping("/ChangePatientPassword")
	public String ChangePatientPassword(HttpServletRequest request,RedirectAttributes attributes) {
		try {
			
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass =request.getParameter("confirmPassword");
			
			if(!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same");
				return "redirect:/Patient/ChangePatientPassword";
				
			}
			
			Users patient = (Users) session.getAttribute("loggedInPatient");
			
			if(oldPass.equals(patient.getPassword())) {
				patient.setPassword(confirmPass);
				UserRepo.save(patient);
				session.removeAttribute("loggedInPatient");
				attributes.addFlashAttribute("msg", "Password succesfully Change");
				return "redirect:/PatientLogin";
				
			}
			else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");
				
				
			}
			
			return "redirect:/Patient/ChangePatientPassword";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
			return "redirect:/Patient/ChangePatientPassword";
		}
		
		
	}
	
	// Logout
	
	
	@GetMapping("/Logout")
	public String Logout(RedirectAttributes attributes) {
		
		session.removeAttribute("loggedInPatient");
		attributes.addFlashAttribute("msg", "Logout Successfully");
		return "redirect:/PatientLogin";   
	 
		
	}
	
	
	
	@GetMapping("/ViewPrescriptions")
	public String ShowViewPrescriptions(Model model) {
		if(session.getAttribute("loggedInPatient")==null) {
			return "redirect:/PatientLogin";
			
		}
		
		
		 Users patient =(Users) session.getAttribute("loggedInPatient");
		 List<Prescription> prescriptionList = prescriptionRepo.findByPatient(patient);
		 model.addAttribute("prescriptionList", prescriptionList);
		 
		 
		
		return "Patient/ViewPrescriptions";
		
		
	}
	
	@GetMapping("/ViewProfile")
	public String ShowViewProfile() {
		if(session.getAttribute("loggedInPatient")==null) {
			return "redirect:/PatientLogin";
			
		}
		
		return "Patient/ViewProfile";
		
		
	}
	

}
	
	
	

