package org.github.rotty3000.cdi.faces;

import java.io.Serializable;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("time")
@ViewScoped
public class TimeBean implements Serializable {

	private static final long serialVersionUID = -3767265178485232805L;

	public Date getServerTime() {
		return new Date();
	}

}
