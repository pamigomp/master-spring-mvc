package masterSpringMvc.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import masterSpringMvc.user.User;
import masterSpringMvc.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserApiControllerAuthTest {
	@Autowired
	private FilterChainProxy springSecurityFilter;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private UserRepository userRepository;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilter).build();
		userRepository.reset(new User("michael@spring.io"));
	}

	@Test
	public void unauthenticatedCannotListUsers() throws Exception {
		this.mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	public void adminCanListUsers() throws Exception {
		this.mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON).header("Authorization",
				basicAuth("admin", "admin"))).andExpect(status().isOk());
	}

	private String basicAuth(String login, String password) {
		byte[] auth = (login + ":" + password).getBytes();
		return "Basic " + Base64.getEncoder().encodeToString(auth);
	}
}
