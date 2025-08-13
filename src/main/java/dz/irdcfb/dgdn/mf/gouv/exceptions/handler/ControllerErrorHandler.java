package dz.irdcfb.dgdn.mf.gouv.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dz.irdcfb.dgdn.mf.gouv.exceptions.CustomFieldError;
import dz.irdcfb.dgdn.mf.gouv.exceptions.FieldErrorResponse;

@ControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

	@Override
	@NonNull
	protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
			@NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
		FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();

		List<CustomFieldError> fieldErrors = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			CustomFieldError fieldError = new CustomFieldError();
			fieldError.setField(((FieldError) error).getField());
			fieldError.setMessage(error.getDefaultMessage());
			fieldErrors.add(fieldError);
		});

		fieldErrorResponse.setFieldErrors(fieldErrors);
		return new ResponseEntity<>(fieldErrorResponse, status);
	}
}