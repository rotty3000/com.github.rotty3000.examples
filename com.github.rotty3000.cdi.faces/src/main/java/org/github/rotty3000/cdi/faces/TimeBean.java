package org.github.rotty3000.cdi.faces;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.osgi.service.log.Logger;

@Named("time")
@ViewScoped
public class TimeBean implements Serializable {

	private static final long serialVersionUID = -3767265178485232805L;

	public Date getServerTime() {
		return new Date();
	}

	@Inject
	private Logger logger;

	@PostConstruct
	void init() {
		logger.info("PostConstruct {}", this);
	}

}
