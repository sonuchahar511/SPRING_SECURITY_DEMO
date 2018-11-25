package com.neev.moh.web.controller.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.neev.moh.dao.UserDao;
import com.neev.moh.facade.DepartmentFacade;
import com.neev.moh.facade.UserFacade;
import com.neev.moh.facade.impl.UserFacadeImpl;
import com.neev.moh.model.User;
import com.neev.moh.services.UserService;
import com.neev.moh.web.auth.CustomUserDetailsService;
import com.neev.moh.web.controller.UserController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:controller-context.xml")
@SuppressWarnings("unused")
@WebAppConfiguration
@EnableWebMvc
public class UserControllerTest {

	static {
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }

	private UserFacade userFacade;

    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private UserController userController;
    
    private static UserService userService;
    private static UserDao userDao;
	private static User mockUser;
    private static MockHttpServletRequest request;
    private static MockHttpServletResponse response;
    private static MockMvc mockMvc;
	
    @Resource
    private CustomUserDetailsService userDetailsService ;
    
    @Resource
    private DepartmentFacade departmentFacade;
    
    @Resource
    private FilterChainProxy springSecurityFilterChain;
    
	@Before
	public void setup() throws Exception {
		userDao = mock(UserDao.class);
		userService = mock(UserService.class);
		userFacade = new UserFacadeImpl(userService);
		
		request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        //mockMvc = MockMvcBuilders.standaloneSetup(this.wac).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
        
		mockUser = new User("firstname","lastname","test@email.com",new Date(),91, "007", "moh");
		
		when(userFacade.getUserByEmail("test@email.com")).thenReturn(mockUser);
	}
	
	@Test
	public void test() {
		Assert.assertSame("Assertion Test", "test" , "test");
	}
	
	@Test
    public void testHome() throws Exception {
		// root url to login.jsp redirection
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/login.jsp")) ;
    }	
	
    @Test
    public void testValidUserLogin() throws Exception{
    	User validUser = new User("", "", "admin@gmail.com", new Date(), 91, "007", "moh");
    	
    	MvcResult result  = mockMvc.perform(post("/login")
    			.param("loginId", validUser.getEmail())
    			.param("password", validUser.getPassword())
    			).andReturn();
    	
    	MockHttpSession session = (MockHttpSession)result.getRequest().getSession();
    	
    	// "CURRENT_USER" attribute will ALWAYS available in session after successfull login.
    	Assert.assertNotNull( session.getAttribute("CURRENT_USER") );
    }
    
    @Test
    public void testInvalidUserLogin() throws Exception{
    	User invalidUser = new User("", "", "admin@gmail.com111", new Date(), 91, "007", "moh11");
    	
    	MvcResult result  = mockMvc.perform(post("/login")
    			.param("loginId", invalidUser.getEmail())
    			.param("password", invalidUser.getPassword())
    			).andReturn();
    	
    	MockHttpSession session = (MockHttpSession)result.getRequest().getSession();
    	
    	// "CURRENT_USER" attribute will NOT available for INVALID CREDENTIALS.
    	Assert.assertNull( session.getAttribute("CURRENT_USER") );
    }
    
    @Test
    public void testUserHasPermissionOnResource(){
    	 UserDetails ud =  userDetailsService.loadUserByUsername("admin@gmail.com");
       	 Authentication authToken = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword() ,ud.getAuthorities());
	   	 SecurityContextHolder.getContext().setAuthentication(authToken);
	   	 
	   	 departmentFacade.getAllDepartment();
	   	 
	     SecurityContextHolder.clearContext();
    }
    
    @Test(expected = AccessDeniedException.class)
    public void testUserHasNoPermissionOnResource(){
   	 	UserDetails ud =  userDetailsService.loadUserByUsername("patient1@gmail.com");
   	 	Authentication authToken = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword() ,ud.getAuthorities());
	    SecurityContextHolder.getContext().setAuthentication(authToken);
	   	 
	   	departmentFacade.getAllDepartment();
	   	 
	    SecurityContextHolder.clearContext();
    }
    
}
