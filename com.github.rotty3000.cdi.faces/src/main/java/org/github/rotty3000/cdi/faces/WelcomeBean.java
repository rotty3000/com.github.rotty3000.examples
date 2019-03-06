package org.github.rotty3000.cdi.faces;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "welcome", eager = true)
public class WelcomeBean {

	public WelcomeBean() {
		System.out.println("WelcomeBean instantiated");
	}

	public String getMessage() {
		return "I'm alive!";
	}

}
