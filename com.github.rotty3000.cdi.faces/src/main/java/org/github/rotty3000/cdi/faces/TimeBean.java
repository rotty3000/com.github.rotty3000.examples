package org.github.rotty3000.cdi.faces;

import java.util.Date;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named("time")
@ViewScoped
public class TimeBean {

	public Date getServerTime() {
		return new Date();
	}

}
