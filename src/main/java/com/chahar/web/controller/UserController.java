package com.chahar.web.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.chahar.entity.User;
import com.chahar.service.UserService;
import com.chahar.service.impl.DefaultPasswordEncoderService;
import com.chahar.web.CmsPages;
import com.chahar.web.dto.UserDto;
import com.chahar.web.dto.populator.ReversePopulator;

@Controller
@RequestMapping(value="/")
public class UserController extends AbstractPageController{
	private static final Log LOGGER = LogFactory.getLog(HomeController.class);
	
	@Resource(name="myUserService")
	private UserService userService;
	
	@Resource(name="myUserReversePopulator")
	private ReversePopulator<UserDto, User> populator;
	
	@Resource(name="myPasswordEncoderService")
	private DefaultPasswordEncoderService passwordEncoderService;
	
	@RequestMapping(value=CmsPages.ADMIN_PAGE, method = RequestMethod.GET)
	 public ModelAndView geAdminPage(final ModelMap model) {
		LOGGER.info(getPrincipal()+": admin called");
		
		return new ModelAndView(CmsPages.ADMIN_PAGE);
	}
	
	@RequestMapping(value=CmsPages.USER_PAGE, method = RequestMethod.GET)
	public ModelAndView geUserPage(final ModelMap model) {
		LOGGER.info(getPrincipal()+": user called");
		
	    return new ModelAndView(CmsPages.USER_PAGE);
	}

	@RequestMapping(value=CmsPages.ALL_USER_LIST_PAGE,method=RequestMethod.GET)
	public ModelAndView getAllUsers(final ModelMap model){
		LOGGER.info(getPrincipal()+": allUsers called");
		
		ModelAndView result = new ModelAndView(CmsPages.ALL_USER_LIST_PAGE);
		model.addAttribute("users", userService.findAllUsers());
		return result;
	}
	
	@RequestMapping(value=CmsPages.REGISTRATION_PAGE, method = RequestMethod.GET)
	public ModelAndView getRegistrationPage(final Model model){
		LOGGER.info(getPrincipal()+": registration page called For GET Request");
		
		model.addAttribute("user", new UserDto());
		model.addAttribute("registrationUrl","registration");
		model.addAttribute("roles",userService.findAllRoles());
		
		return new ModelAndView(CmsPages.REGISTRATION_PAGE);
	}
	
	
	@RequestMapping(value=CmsPages.REGISTRATION_PAGE, method = RequestMethod.POST)
	public ModelAndView doRegistration(@ModelAttribute(name="user") UserDto userDto , final BindingResult result, final Model model){
		LOGGER.info(getPrincipal()+": registration with POST called");
		
		ModelAndView resultView = new ModelAndView(CmsPages.REGISTRATION_PAGE);
		
		if(result.hasErrors()){
			//retain on same page with error message
			model.addAttribute("message", "registration failed");
			return resultView;
		}
		
		User user = populator.populate(userDto);
		passwordEncoderService=null;
		String encodedPassword = passwordEncoderService.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		boolean isCustomerAdded = this.userService.saveUser(user);
		model.addAttribute("message", "registration "+ ( isCustomerAdded?" Successful" : "Failed") );
		
		return resultView;
	}
	
	@RequestMapping(value="allUsers1/{username}",method=RequestMethod.GET)
	public ModelAndView getUserProfile(@PathVariable("username") final String username, final Model model){
		LOGGER.info(getPrincipal()+": allUsers1/{username}  called where {username}="+username);
		
		model.addAttribute("user", userService.getUserByUsername(username));
		ModelAndView result = new ModelAndView(CmsPages.USER_PROFILE_PAGE);
		
		return result;
	}
	
	@RequestMapping(value="allUsers1/delete/{username}",method=RequestMethod.GET)
	public String deleteUser(@PathVariable("username") final String username){
		LOGGER.info(getPrincipal()+": allUsers1/delete/{username}  called where {username}="+username);
		
		userService.deleteUserByUsername(username);
		
		return REDIRECT_PREFIX+"allUsers";
	}
	
}
