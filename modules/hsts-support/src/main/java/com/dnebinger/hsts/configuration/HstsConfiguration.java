package com.dnebinger.hsts.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * class HstsConfiguration: Configuration for the HSTS filter.
 *
 * @author dnebinger
 * @author olaf kock
 */
@ExtendedObjectClassDefinition(
		category = "hsts",
		scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
		id = "com.dnebinger.hsts.configuration.HstsConfiguration",
		localization = "content/Language",
		name = "hsts-configuration-name",
		description = "hsts-configuration-description"
)
public interface HstsConfiguration {

	@Meta.AD(
			deflt = "false",
			name = "hsts-enabled",
			description = "hsts-enabled-description",
			required = false
	)
	public boolean enabled();

	@Meta.AD(
			deflt = "31536000",
			name = "hsts-anon-timeout",
			description = "hsts-anon-timeout-description",
			required = false
	)
	public int anonymousUserTimeoutSeconds();

	@Meta.AD(
			deflt = "31536000",
			name = "hsts-user-timeout",
			description = "hsts-user-timeout-description",
			required = false
	)
	public int authenticatedUserTimeoutSeconds();

	@Meta.AD(
			deflt = "true",
			name = "hsts-include-subdomain",
			description = "hsts-include-subdomain-description",
			required = false
	)
	public boolean includeSubdomain();
}
