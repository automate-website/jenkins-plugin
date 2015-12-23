package website.automate.plugins.jenkins.service.support;

import website.automate.plugins.jenkins.model.Authentication;

public final class AuthenticationUtils {

	private static Authentication DEFAULT = Authentication.of("test@automate.website", "secr3t");
	
	private AuthenticationUtils(){
		throw new AssertionError();
	}
	
	public static Authentication getIntegrationTestAccountPrincipal(){
		return DEFAULT;
	}
}
