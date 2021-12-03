package im.ligas.search.processors;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BaseIndexerPostProcessor;
import com.liferay.portal.kernel.search.Document;
import com.liferay.ratings.kernel.model.RatingsStats;
import com.liferay.ratings.kernel.service.RatingsStatsLocalService;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCustomIndexerPostProcessor extends BaseIndexerPostProcessor {
    private static Logger LOG = LoggerFactory.getLogger(CustomDLFileEntryIndexerPostProcessor.class);

    @Reference
    protected RatingsStatsLocalService ratingsStatsLocalService;

    @Override
    public void postProcessDocument(Document document, Object object) {
        double score = 0;
        try {
            RatingsStats ratingsStats = ratingsStatsLocalService.getStats(getClassName(), getClassPk(object));
            score = ratingsStats.getTotalScore();
        } catch (PortalException e) {
            LOG.error(e.getMessage());
        }
        document.addNumberSortable("ratings", score);
    }

    protected abstract String getClassName();
    protected abstract long getClassPk(Object object);
}
