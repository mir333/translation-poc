package im.ligas.listener.journal.article;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.search.BaseIndexerPostProcessor;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.ratings.kernel.model.RatingsStats;
import com.liferay.ratings.kernel.service.RatingsStatsLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        immediate = true,
        property = {"indexer.class.name=com.liferay.journal.model.JournalArticle"},
        service = IndexerPostProcessor.class
)
public class CustomJournalArticleIndexerPostProcessor extends BaseIndexerPostProcessor {

    private static Logger LOG = LoggerFactory.getLogger(CustomJournalArticleIndexerPostProcessor.class);

    @Reference
    private RatingsStatsLocalService ratingsStatsLocalService;

    @Override
    public void postProcessDocument(Document document, Object object) throws Exception {

        JournalArticle journalArticle = (JournalArticle) object;
        RatingsStats ratingsStats = null;
        double ratingsPoints = 0;

        ratingsStats = ratingsStatsLocalService.getStats(JournalArticle.class.getName(), journalArticle.getResourcePrimKey());
        ratingsPoints = ratingsStats.getTotalScore();


        if (ratingsPoints > 0) {
            document.addNumberSortable("ratings", ratingsPoints);
        }
    }
}
