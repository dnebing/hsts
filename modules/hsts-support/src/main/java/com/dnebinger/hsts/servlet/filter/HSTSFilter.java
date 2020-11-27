package com.dnebinger.hsts.servlet.filter;

import com.dnebinger.hsts.configuration.HstsConfiguration;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.Portal;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * class HSTSFilter: An updated HSTS filter implementation for Liferay 7.x
 *
 * @author dnebinger
 * @author olaf kock
 */
@Component(
		immediate = true,
		property = {
				"servlet-context-name=",
				"servlet-filter-name=HSTS Filter",
				"url-pattern=/*"
		},
		service = Filter.class
)
public class HSTSFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		_log.info("HSTS Filter Initialized");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		// only set the header once. response.containsHeader gave false negatives,
		// working around this with an extra request attribute
		if(servletRequest.isSecure() && servletRequest.getAttribute(STRICT_TRANSPORT_SECURITY)==null) {
			String header = getHeader(servletRequest);
			if(header != null) {
				try {
					response.addHeader(STRICT_TRANSPORT_SECURITY, header);
					servletRequest.setAttribute(STRICT_TRANSPORT_SECURITY, header);
				} catch (Exception ignore) {
					_log.error("ignored exception in HSTS Filter. No header added. (" +
							ignore.getClass().getName() + " " + ignore.getMessage() + ")");
				}
			} else {
				if(_log.isDebugEnabled())
					_log.debug("request is insecure or HSTS not configured");
			}
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}

	private static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

	private String getHeader(ServletRequest request) {
		Long companyId = (Long) request.getAttribute("COMPANY_ID");
		if(companyId == null) {
			companyId = _portal.getDefaultCompanyId();
		}

		HstsConfiguration config = null;
		try {
			config = _configurationProvider.getCompanyConfiguration(HstsConfiguration.class, companyId);
		} catch (ConfigurationException e) {
			_log.warn("Failed to load HSTS configuration for company id {}: {}", companyId, e.getMessage(), e);

			return null;
		}

		if (! config.enabled()) {
			// disabled, no header
			return null;
		}

		long timeout = 0;
		String result = null;

		Object user = request.getAttribute("USER");

		if(user == null) {
			timeout = config.anonymousUserTimeoutSeconds();
		} else {
			timeout = config.authenticatedUserTimeoutSeconds();
		}

		if(timeout > 0L) {
			result = "max-age=" + timeout;

			if(config.includeSubdomain()) {
				result += "; includeSubDomains";
			}
		}

		if(_log.isTraceEnabled()) {
			_log.trace(STRICT_TRANSPORT_SECURITY + " " + result);
		}

		return  result;
	}

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;

	private static final Logger _log = LoggerFactory.getLogger(HSTSFilter.class);
}
