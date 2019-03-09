package com.cdac.in;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cdac.in.model.Login;
import com.cdac.in.model.Users;
import com.cdac.in.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		return "home";
	}
	
	
	
	@RequestMapping(value = "/Register", method = RequestMethod.GET)
	public String reg(Locale locale, Model model) {
		model.addAttribute("user", new Users());
		return "registration";
	}
	
	@RequestMapping(value = "registration-action", method = { RequestMethod.POST , RequestMethod.GET })
	public String login(Locale locale, Model model,@Valid  @ModelAttribute("user")  Users user, BindingResult br, HttpSession session) {
		
		System.out.println(user);
		
		//UserValidation uservalidation= new UserValidation();
		//uservalidation.validate(uservalidation, br);
		if(br.hasErrors())
		{
			return "registration";
		}
		else {
		userService.addUser(user);
		session.setAttribute("user", user);
		return "login";
		}
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String getLogin(Locale locale, Model model)
	{
		model.addAttribute("login", new Login() );
		return "login";
	}
	
	@RequestMapping(value="/login-action",method= {RequestMethod.POST, RequestMethod.GET})
	public String doLogin(Locale locale, Model model, @ModelAttribute("user1") Login login, HttpSession session) 
	{
		if(login.getUsername()!=null && login.getPassword()!=null)
		{
			Users checkrole= userService.isValid(login);
	
		//System.out.println("List is" +checkrole.getAdhar());
		if(checkrole!=null)
		{
			session.setAttribute("user", checkrole.getUsername());
			//req.getSession().setAttribute("user", checkrole.getUsername());
			System.out.println(checkrole.getUsername());
		
			if(checkrole.getroleid().equals(null))
			{
				return "login";
			}
			else if(checkrole.getroleid().equals("1"))
			{
				session.getAttribute("user");
			return "Eprofile";
			}
		
			else if(checkrole.getroleid().equals("2"))
			{
				return "Iprofile";
			}
			else
			{
			return "Aprofile";
			}
	   }
		else
		{
			model.addAttribute(" failed", "login failed");
			return "login";
		}
		}
		else
		{
			return "login";
		}
}
}
