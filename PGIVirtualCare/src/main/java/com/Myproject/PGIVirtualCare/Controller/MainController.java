package com.Myproject.PGIVirtualCare.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Myproject.PGIVirtualCare.Model.Enquiry;
import com.Myproject.PGIVirtualCare.Model.Users;
import com.Myproject.PGIVirtualCare.Model.Users.UserRole;
import com.Myproject.PGIVirtualCare.Model.Users.UserStatus;
import com.Myproject.PGIVirtualCare.Repository.EnquiryRepository;
import com.Myproject.PGIVirtualCare.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EnquiryRepository enquiryRepo;

	@GetMapping("/")
	public String ShowHome() {
		return "index";
	}

	@GetMapping("/AboutUs")
	public String ShowAboutUs() {
		return "AboutUs";
	}

	@GetMapping("/Services")
	public String ShowServices() {
		return "Services";
	}

	@GetMapping("/Registration")
	public String ShowRegistration(Model model) {
		Users userDto = new Users();
		model.addAttribute("userDto", userDto);

		return "Registration";
	}

	@PostMapping("/Registration")
	public String Registration(@ModelAttribute("userDto") Users newUser, RedirectAttributes attributes) {
		try {

			newUser.setRole(UserRole.PATIENT);

			newUser.setStatus(UserStatus.PENDING);
			newUser.setRegDate(LocalDateTime.now());
			userRepo.save(newUser);
			attributes.addFlashAttribute("msg", "Registration Successful!");
			return "redirect:/Registration";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " + e.getMessage());
			return "redirect:/Registration";
		}

	}

	@GetMapping("/AdminLogin")
	public String ShowAdminLogin() {
		return "AdminLogin";

	}

	@PostMapping("/AdminLogin")
	public String AdminLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {

		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "Admin doesn't Exists");
				return "redirect:/AdminLogin";
			}
			Users admin = userRepo.findByEmail(email);
			if (password.equals(admin.getPassword()) && admin.getRole().equals(UserRole.ADMIN)) {
			  
			   session.setAttribute("loggedInAdmin", admin);
			   return "redirect:/Admin/AdminDashboard";
			  
			

			} else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
			}
			
			return "redirect:/AdminLogin";

			

		} catch (Exception e) {
			return "redirect:/AdminLogin";
		}

	}
	
	
	// Doctor Login
   
	@GetMapping("/DoctorLogin")
	public String ShowDoctorLogin() {
		return "DoctorLogin";
	}
	
	
	@PostMapping("/DoctorLogin")
	public String DoctorLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "Doctor doesn't Exists");
				return "redirect:/DoctorLogin";

			}
			Users doctor = userRepo.findByEmail(email);

			if (password.equals(doctor.getPassword()) && doctor.getRole().equals(UserRole.DOCTOR)) {
				if (doctor.getStatus().equals(UserStatus.DISABLED)) {
					attributes.addFlashAttribute("msg", "Login Disabled 🚫, Please contact Adminstration");
					return "redirect:/DoctorLogin";

				} else {
					 session.setAttribute("loggedInDoctor", doctor);
					 return "redirect:/Doctor/DoctorDashboard";
				}

			} else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
				return "redirect:/DoctorLogin";
			}
			

			

		} catch (Exception e) {
			return "redirect:/DoctorLogin";
		}
	}

	// Doctor Login process end 
	
	
	

	@GetMapping("/PatientLogin")
	public String ShowPatientLogin() {
		return "PatientLogin";
	}

	@PostMapping("/PatientLogin")
	public String PatientLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "User doesn't Exists");
				return "redirect:/PatientLogin";

			}
			Users patient = userRepo.findByEmail(email);

			if (password.equals(patient.getPassword()) && patient.getRole().equals(UserRole.PATIENT)) {
				if (patient.getStatus().equals(UserStatus.PENDING)) {
					attributes.addFlashAttribute("msg", "Registration Pending, Wait for Admin Approval");
				} else if (patient.getStatus().equals(UserStatus.DISABLED)) {
					attributes.addFlashAttribute("msg", "Login Disabled 🚫, Please contact Adminstration");

				} else {
					  
					  session.setAttribute("loggedInPatient", patient);
					  return "redirect:/Patient/PatientDashboard";
				}

			}
			
			
			else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
				return "redirect:/PatientLogin";
			}

			return "redirect:/PatientLogin";

		} catch (Exception e) {
			return "redirect:/PatientLogin";
		}
	}
	
	
	//Contact Us from process start

	@GetMapping("/ContactUs")
	public String ShowContactUs(Model model) {
		Enquiry enquiry = new Enquiry();
		model.addAttribute("enquiry", enquiry);
		return "ContactUs";
	}

	@PostMapping("/ContactUs")
	public String SubmitEnquiry(@ModelAttribute("enquiry") Enquiry enquiry) {
		try {
			enquiry.setEnquiryDate(LocalDateTime.now());
			enquiryRepo.save(enquiry);
			return "redirect:/ContactUs";

		} catch (Exception e) {
			return "redirect:/ContactUs";
		}
	}
	//Contact Us process end
	
	
	
}
