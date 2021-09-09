package im.ligas.listener.journal.article;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(immediate = true, service = ModelListener.class)
public class TranslationJournalArticleModalListener extends BaseModelListener<JournalArticle> {

    @Reference
    private JournalArticleLocalService journalArticleLocalService;

    private static final Logger LOG = LoggerFactory.getLogger(TranslationJournalArticleModalListener.class);

    @Override
    public void onAfterAddAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onAfterAddAssociation");
    }

    @Override
    public void onAfterCreate(JournalArticle model) throws ModelListenerException {
        LOG.error("onAfterCreate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onAfterRemove(JournalArticle model) throws ModelListenerException {
        LOG.error("onAfterRemove");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onAfterRemoveAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onAfterRemoveAssociation");
    }

    @Override
    public void onAfterUpdate(JournalArticle model) throws ModelListenerException {
        LOG.error("onAfterUpdate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeAddAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onBeforeAddAssociation");
    }

    @Override
    public void onBeforeCreate(JournalArticle model) throws ModelListenerException {
        LOG.error("onBeforeCreate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeRemove(JournalArticle model) throws ModelListenerException {
        LOG.error("onBeforeRemove");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeRemoveAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onBeforeRemoveAssociation");
    }

    @Override
    public void onBeforeUpdate(JournalArticle model) throws ModelListenerException {
        LOG.error("onBeforeUpdate");
        LOG.info("{}", getString(model));
    }

    private String getString(JournalArticle model) {
        try {
            final JournalArticle article = journalArticleLocalService.getArticle(model.getGroupId(), model.getArticleId());
            LOG.info("old and new compartment {}", article.getTitleMapAsXML().equals(model.getTitleMapAsXML()));
            LOG.info(article.getTitleMapAsXML());
            LOG.info(model.getTitleMapAsXML());
        } catch (PortalException e) {
            e.printStackTrace();
        }

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(model.getArticleId());
        stringBuilder.append("  ");
        stringBuilder.append(model.getUuid());
        stringBuilder.append("  ");
        stringBuilder.append(model.getPrimaryKey());
        stringBuilder.append("  ");
        return stringBuilder.toString();
    }
}
