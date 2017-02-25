package masterSpringMvc.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EntityNotFoundMapper {
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Zasób nie istnieje")
	public void handleNotFound() {
	}
}
