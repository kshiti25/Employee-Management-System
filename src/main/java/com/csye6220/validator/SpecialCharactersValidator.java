package com.csye6220.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpecialCharactersValidator implements ConstraintValidator<NoSpecialCharacter,String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return value==null || !value.matches(".*[\\W\\d].*");
	}
	@Override
	public void initialize(NoSpecialCharacter constraintAnnotation) {
    }

}
