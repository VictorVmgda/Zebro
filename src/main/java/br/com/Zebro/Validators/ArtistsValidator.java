package br.com.Zebro.Validators;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@SuppressWarnings("rawtypes")
@FacesValidator("artistValidator")
public class ArtistsValidator implements Validator{

	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value) throws ValidatorException {
		
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>) value;
		
		if (list.size() > 5) {
			throw new ValidatorException(new FacesMessage("Adicione até cinco artistas"));
		}
		
	}

}
