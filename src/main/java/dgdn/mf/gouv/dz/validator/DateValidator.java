package dgdn.mf.gouv.dz.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<DateValidation, Date> {
	@Override
	public boolean isValid(Date dateDepartExp, ConstraintValidatorContext context) {

		// Date dateArrivee = ;
		return dateDepartExp.compareTo(new Date()) < 0;

	}

}
