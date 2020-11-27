package com.dnebinger.hsts.configuration.category;

import com.liferay.configuration.admin.category.ConfigurationCategory;
import org.osgi.service.component.annotations.Component;

/**
 * class HstsConfigurationCategory: Defines the configuration category for the hsts stuff.
 *
 * @author dnebinger
 */
@Component(
		immediate = true,
		service = ConfigurationCategory.class
)
public class HstsConfigurationCategory implements ConfigurationCategory {

	@Override
	public String getBundleSymbolicName() {
		return "com.dnebinger.hsts";
	}

	@Override
	public String getCategoryIcon() {
		return _CATEGORY_ICON;
	}

	@Override
	public String getCategoryKey() {
		return _CATEGORY_KEY;
	}

	@Override
	public String getCategorySection() {
		return _CATEGORY_SECTION;
	}

	private static final String _CATEGORY_ICON = "lock";

	private static final String _CATEGORY_KEY = "hsts";

	private static final String _CATEGORY_SECTION = "security";
}
