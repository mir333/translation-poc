package custom.search.api.internal.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;

@Component(
        configurationPid = {
                SearchConfiguration.PID
        },
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        immediate = true,
        service = SearchConfigurationHelper.class
)
public class SearchConfigurationHelperImpl  implements SearchConfigurationHelper{

    private volatile SearchConfiguration configuration;

    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
        configuration = ConfigurableUtil.createConfigurable(
                SearchConfiguration.class, properties);
    }

    @Override
    public SearchConfiguration getConfiguration() {
        return configuration;
    }
}
