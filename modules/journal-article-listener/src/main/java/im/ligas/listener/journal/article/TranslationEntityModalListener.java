package im.ligas.listener.journal.article;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.translation.model.TranslationEntry;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(immediate = true, service = ModelListener.class)
public class TranslationEntityModalListener extends BaseModelListener<TranslationEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(TranslationEntityModalListener.class);

    @Override
    public void onAfterAddAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onAfterAddAssociation");
    }

    @Override
    public void onAfterCreate(TranslationEntry model) throws ModelListenerException {
        LOG.error("onAfterCreate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onAfterRemove(TranslationEntry model) throws ModelListenerException {
        LOG.error("onAfterRemove");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onAfterRemoveAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onAfterRemoveAssociation");
    }

    @Override
    public void onAfterUpdate(TranslationEntry model) throws ModelListenerException {
        LOG.error("onAfterUpdate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeAddAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onBeforeAddAssociation");
    }

    @Override
    public void onBeforeCreate(TranslationEntry model) throws ModelListenerException {
        LOG.error("onBeforeCreate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeRemove(TranslationEntry model) throws ModelListenerException {
        LOG.error("onBeforeRemove");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeRemoveAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onBeforeRemoveAssociation");
    }

    @Override
    public void onBeforeUpdate(TranslationEntry model) throws ModelListenerException {
        LOG.error("onBeforeUpdate");
        LOG.info("{}", getString(model));
    }

    private String getString(TranslationEntry model) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(model.getContent());
        stringBuilder.append("  ");
        stringBuilder.append(model.getUuid());
        stringBuilder.append("  ");
        stringBuilder.append(model.getPrimaryKey());
        stringBuilder.append("  ");
        return stringBuilder.toString();
    }
}
