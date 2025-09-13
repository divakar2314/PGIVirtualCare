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

import com.Myproject.PGIVirtualCare.API.SendAutoEmail;
import com.Myproject.PGIVirtualCare.Model.Appointment;
import com.Myproject.PGIVirtualCare.Model.Appointment.AppointmentStatus;
import com.Myproject.PGIVirtualCare.Model.Enquiry;
import com.Myproject.PGIVirtualCare.Model.Prescription;
import com.Myproject.PGIVirtualCare.Model.Users;
import com.Myproject.PGIVirtualCare.Model.Users.UserRole;
import com.Myproject.PGIVirtualCare.Model.Users.UserStatus;
import com.Myproject.PGIVirtualCare.Repository.AppointmentRepository;
import com.Myproject.PGIVirtualCare.Repository.EnquiryRepository;
import com.Myproject.PGIVirtualCare.Repository.PrescriptionRepository;
import com.Myproject.PGIVirtualCare.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class AdminController {
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserRepository UserRepo;
	
	@Autowired
	private AppointmentRepository appointmentRepo;
	
	@Autowired
	private EnquiryRepository enquiryRepository;
	@Autowired
	private PrescriptionRepository prescriptionRepo;
	
	@Autowired
	private SendAutoEmail sendAutoEmail;
	
	
	
	@GetMapping("/AdminDashboard")
	public String ShowAdminDashboard(Model model) {
		
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
			
		}
		model.addAttribute("totalDocotors", UserRepo.countByRole(UserRole.DOCTOR));
		model.addAttribute("totalPatients", UserRepo.countByRole(UserRole.PATIENT));
		model.addAttribute("totalAppointments", appointmentRepo.count());
		model.addAttribute("totalPrescription", prescriptionRepo.count());
		model.addAttribute("totalEnquiry", enquiryRepository.count());
		model.addAttribute("AppAppointment", appointmentRepo.countByStatus(AppointmentStatus.APPROVED));
		model.addAttribute("CancleAppointment", appointmentRepo.countByStatus(AppointmentStatus.REJECTED));
		return "Admin/AdminDashboard";
	}
	
	
	
	@GetMapping("/ManagePatients")
	public String ShowManagePatient(Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
		}
		List<Users> patientList = UserRepo.findAllByRole(UserRole.PATIENT);
		model.addAttribute("patientList", patientList.reversed());
		
		return "Admin/ManagePatient";
		
	}
	
	@GetMapping("/PatientStatus")
	public String UpdatePatientStatus(@RequestParam("id") long id, RedirectAttributes attributes) {
		
		try {
			Users patient = UserRepo.findById(id).get();
			if(patient.getStatus().equals(UserStatus.PENDING)) {
				patient.setStatus(UserStatus.APPROVED);
				UserRepo.save(patient);
				sendAutoEmail.SendConfirmationEmail(patient);

				
			}
			else if(patient.getStatus().equals(UserStatus.APPROVED)) {
				
				patient.setStatus(UserStatus.DISABLED);
				UserRepo.save(patient);
				
			}
			else {
				patient.setStatus(UserStatus.APPROVED);
				UserRepo.save(patient);
			}
			attributes.addFlashAttribute("msg", "User Status successfully Update");
			return "redirect:/Admin/ManagePatients";
			
		} catch (Exception e) {
			return "redirect:/Admin/ManagePatients";
		}
	}
	
	@GetMapping("/DeletePatient")
	public String DeletePatient(@RequestParam("id") long id) {
		UserRepo.deleteById(id);
		return "redirect:/Admin/ManagePatients";
		
	}
	
	
	@GetMapping("/Enquiry")
	public String ShowPatientEnquiry( Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
		}
		List<Enquiry> enquiryList= enquiryRepository.findAll();
		model.addAttribute("enquiryList", enquiryList);
		return "Admin/PatientEnquiry";
		
	}
	
	@GetMapping("/DeleteEnquiry")
	public String DeleteEnquiry(@RequestParam("id") long id) {
		enquiryRepository.deleteById(id);
		return "redirect:/Admin/Enquiry";
		
	}
	
	
	
	
	@GetMapping("/PatientDashboard")
	public String ShowPatientDashboard() {
		
		if(session.getAttribute("loggedInPatient")==null) {
			return "redirect:/PatientLogin";
			
		}
		
		return "Patient/PatientDashboard";
		
	}
	
	// Add doctor --> show page
	
	@GetMapping("/AddDoctors")
	public String ShowAddDoctors( Model model) {
		
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
			
		}
		
		Users doctor = new Users();
		model.addAttribute("doctor", doctor);
		return "Admin/AddDoctor";
	}
	
	@PostMapping("/AddDoctors")
	public String AddDoctor(@ModelAttribute("doctor") Users doctor, RedirectAttributes attributes ) {
		try {
			
			if(UserRepo.existsByEmail(doctor.getEmail())) {
				attributes.addFlashAttribute("msg", "User already exist");
				return "redirect:/Admin/AddDoctors";
			}
			
			doctor.setRole(UserRole.DOCTOR);
			doctor.setStatus(UserStatus.APPROVED);
			doctor.setRegDate(LocalDateTime.now());
			UserRepo.save(doctor);
			attributes.addFlashAttribute("msg", "Doctor Successfully added");
			
			
			return "redirect:/Admin/AddDoctors";
			
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
			return "redirect:/Admin/AddDoctors";
		}
		
	}
	
	// Manage Doctor
	
	@GetMapping("/ViewDoctors")
	public String ShowViewDoctors(Model model) {
		
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
			
		}
		List<Users> doctorList = UserRepo.findAllByRole(UserRole.DOCTOR);
		model.addAttribute("doctorList", doctorList);
		
		return "Admin/ViewDoctors";
	}
	
	
	
	
	
	
	
	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
			
		}
		
		return "Admin/ChangePassword";
	}
	
    
	@PostMapping("/ChangePassword")
	public String changePassword(HttpServletRequest request,RedirectAttributes attributes) {
		try {
			
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass =request.getParameter("confirmPassword");
			
			if(!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same");
				return "redirect:/Admin/ChangePassword";
				
			}
			
			Users admin = (Users) session.getAttribute("loggedInAdmin");
			
			if(oldPass.equals(admin.getPassword())) {
				admin.setPassword(confirmPass);
				UserRepo.save(admin);
				session.removeAttribute("loggedInAdmin");
				attributes.addFlashAttribute("msg", "Password succesfully Change");
				return "redirect:/AdminLogin";
				
			}
			else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");
				
				
			}
			
			return "redirect:/Admin/ChangePassword";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : "+e.getMessage());
			return "redirect:/Admin/ChangePassword";
		}
		
		
	}
	
	//Logout
	
	@GetMapping("/Logout")
	public String Logout(RedirectAttributes attributes) {
		
		session.removeAttribute("loggedInAdmin");
		attributes.addFlashAttribute("msg", "Logout Successfully");
		return "redirect:/AdminLogin";
	
		
	}
	
	
	// View Appointments
	
	@GetMapping("/ViewAppointments")
	public String ShowViewAppointment(Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
			
		}
		List<Appointment> appointementList= appointmentRepo.findAll();
		model.addAttribute("appointementList", appointementList);
		return "Admin/ViewAppointments";
		
		
	}
	
	@GetMapping("/DeleteAppointment")
	public String DeleteAppointment(@RequestParam("id") long id) {
		appointmentRepo.deleteById(id);
		return "redirect:/Admin/ViewAppointments";
		
	}
	
	// see prescription
	
	@GetMapping("/ManagePrescriptions")
	public String ShowManagePrescriptions(Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
		}
		List<Prescription> prescriptiontList = prescriptionRepo.findAll();
		model.addAttribute("prescriptiontList", prescriptiontList.reversed());
		
		

		return "Admin/ManagePrescriptions";
		
	}
	
	
	// Medical Report
	
	@GetMapping("/ManageMedicalReports")
	public String ShowManageMedicalReports() {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
		}

		return "Admin/ManageMedicalReports";
		
	}
	
	
	
	

}
