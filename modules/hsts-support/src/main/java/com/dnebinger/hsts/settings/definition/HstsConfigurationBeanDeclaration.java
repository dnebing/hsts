package com.dnebinger.hsts.settings.definition;

import com.dnebinger.hsts.configuration.HstsConfiguration;
import com.liferay.portal.kernel.settings.definition.ConfigurationBeanDeclaration;
import org.osgi.service.component.annotations.Component;

/**
 * class HstsConfigurationBeanDeclaration: Config bean declaration class for the HSTS configuration.
 *
 * @author dnebinger
 */
@Component(
		immediate = true,
		service = ConfigurationBeanDeclaration.class
)
public class HstsConfigurationBeanDeclaration implements ConfigurationBeanDeclaration {
	@Override
	public Class<?> getConfigurationBeanClass() {
		return HstsConfiguration.class;
	}
}
