package im.ligas.search.processors;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = {"indexer.class.name=com.liferay.document.library.kernel.model.DLFileEntry"},
        service = IndexerPostProcessor.class
)
public class CustomDLFileEntryIndexerPostProcessor extends AbstractCustomIndexerPostProcessor {


    @Override
    protected String getClassName() {
        return DLFileEntry.class.getName();
    }

    @Override
    protected long getClassPk(Object object) {
        return ((DLFileEntry) object).getPrimaryKey();
    }
}
