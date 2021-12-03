package im.ligas.search.processors;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = {"indexer.class.name=com.liferay.journal.model.JournalArticle"},
        service = IndexerPostProcessor.class
)
public class CustomJournalArticleIndexerPostProcessor extends AbstractCustomIndexerPostProcessor {

    @Override
    protected String getClassName() {
        return JournalArticle.class.getName();
    }

    @Override
    protected long getClassPk(Object object) {
        return ((JournalArticle) object).getResourcePrimKey();
    }
}
