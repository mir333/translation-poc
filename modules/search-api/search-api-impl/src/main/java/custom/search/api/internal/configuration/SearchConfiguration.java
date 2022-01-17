package custom.search.api.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(category = "JLR")
@Meta.OCD(
        id = "custom.search.api.internal.configuration.SearchConfiguration",
        localization = "content/Language",
        name = "search-config"
)
public interface SearchConfiguration {
    String PID = "custom.search.api.internal.configuration.SearchConfiguration";

    @Meta.AD(
            deflt = "true",
            required = false,
            description = "enable-scoring-function"
    )
    boolean enableScoringFunction();

    @Meta.AD(
            deflt = "try{ if(doc.containsKey('ratings_Number_sortable')){_score * (1+doc['ratings_Number_sortable'].value)} } catch(Exception e) {}",
            required = false,
            description = "scoring-function"
    )
    String scoringFunction();
}
