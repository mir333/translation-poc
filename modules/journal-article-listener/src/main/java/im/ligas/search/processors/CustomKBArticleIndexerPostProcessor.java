package im.ligas.search.processors;

import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = {"indexer.class.name=com.liferay.knowledge.base.model.KBArticle"},
        service = IndexerPostProcessor.class
)
public class CustomKBArticleIndexerPostProcessor extends AbstractCustomIndexerPostProcessor {

    @Override
    protected String getClassName() {
        return KBArticle.class.getName();
    }

    @Override
    protected long getClassPk(Object object) {
        return ((KBArticle)object).getClassPK();
    }
}
