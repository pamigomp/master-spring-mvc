package masterSpringMvc.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@Profile("redis")
@EnableRedisHttpSession
public class RedisConfig {
	@Bean
	@Profile("heroku")
	public RedisConnectionFactory redisConnectionFactory() throws URISyntaxException {
		JedisConnectionFactory redis = new JedisConnectionFactory();
		String redisUrl = System.getenv("REDIS_URL");
		URI redisUri = new URI(redisUrl);
		redis.setHostName(redisUri.getHost());
		redis.setPort(redisUri.getPort());
		redis.setPassword(redisUri.getUserInfo().split(":", 2)[1]);
		return redis;
	}
	
	@Bean
	@Profile({ "cloud", "heroku" })
	public static ConfigureRedisAction configureRedisAction() {
		 return ConfigureRedisAction.NO_OP;
	}
}
