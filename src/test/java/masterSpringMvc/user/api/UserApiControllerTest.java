package masterSpringMvc.user.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import masterSpringMvc.user.User;
import masterSpringMvc.user.UserRepository;
import masterSpringMvc.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserApiControllerTest {
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private UserRepository userRepository;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		userRepository.reset(new User("michael@spring.io"));
	}

	@Test
	public void shouldListUsers() throws Exception {
		this.mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].email", is("michael@spring.io")));
	}

	@Test
	public void shouldCreateNewUser() throws Exception {
		User user = new User("adam@spring.io");
		this.mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(user)))
				.andExpect(status().isCreated());

		assertThat(userRepository.findAll()).extracting(User::getEmail).containsOnly("michael@spring.io", "adam@spring.io");
	}

	@Test
	public void shouldDeleteUser() throws Exception {
		this.mockMvc.perform(delete("/api/user/michael@spring.io").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		assertThat(userRepository.findAll()).hasSize(0);
	}

	@Test
	public void shouldReturnNotFoundWhenDeletingUnknownUser() throws Exception {
		this.mockMvc.perform(delete("/api/user/non-existing@mail.com").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void putShouldUpdateExistingUser() throws Exception {
		User user = new User("ignored@spring.io");
		this.mockMvc.perform(
				put("/api/user/michael@spring.io").content(JsonUtil.toJson(user)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		assertThat(userRepository.findAll()).extracting(User::getEmail).containsOnly("michael@spring.io");
	}
}
