package custom.search.api.internal.jaxrs.application;

import javax.annotation.Generated;

import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;

/**
 * @author ligasm
 * @generated
 */
@Component(
	property = {
		"liferay.jackson=false", "osgi.jaxrs.application.base=/search-api",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.Vulcan)",
		"osgi.jaxrs.name=CustomSearchApi"
	},
	service = Application.class
)
@Generated("")
public class SearchApiApplication extends Application {
}