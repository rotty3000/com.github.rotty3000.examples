package org.apache.portals.samples;

import javax.inject.Inject;
import javax.inject.Named;

import org.osgi.service.cdi.annotations.Reference;

import com.liferay.portal.kernel.service.UserLocalService;

@Named
public class Users {

	@Inject
	@Reference
	private UserLocalService userLocalService;

	public int getUsersCount() {
		return userLocalService.getUsersCount();
	}

}
