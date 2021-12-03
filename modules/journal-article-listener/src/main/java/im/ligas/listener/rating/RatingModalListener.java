package im.ligas.listener.rating;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.ratings.kernel.model.RatingsStats;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(immediate = true, service = ModelListener.class)
public class RatingModalListener extends BaseModelListener<RatingsStats> {

    private static final Logger LOG = LoggerFactory.getLogger(RatingModalListener.class);

    @Reference
    private JournalArticleLocalService journalArticleLocalService;
    @Reference
    private DLFileEntryLocalService dlFileEntryService;
    @Reference
    private KBArticleLocalService kbArticleLocalService;

    @Override
    public void onAfterAddAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onAfterAddAssociation");
    }

    @Override
    public void onAfterCreate(RatingsStats model) throws ModelListenerException {
        LOG.error("onAfterCreate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onAfterRemove(RatingsStats model) throws ModelListenerException {
        LOG.error("onAfterRemove");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onAfterRemoveAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onAfterRemoveAssociation");
    }

    @Override
    public void onAfterUpdate(RatingsStats model) throws ModelListenerException {
        LOG.error("onAfterUpdate");
        LOG.info("{}", getString(model));
        try {
            if (model.getClassName().equals(DLFileEntry.class.getName())) {
                final DLFileEntry dlFileEntry = dlFileEntryService.getFileEntry(model.getClassPK());
                dlFileEntryService.updateDLFileEntry(dlFileEntry);
            } else if (model.getClassName().equals(JournalArticle.class.getName())) {
                final JournalArticle latestArticle = journalArticleLocalService.getLatestArticle(model.getClassPK());
                journalArticleLocalService.updateJournalArticle(latestArticle);
            } else if (model.getClassName().equals(KBArticle.class.getName())) {
                final KBArticle latestArticle = kbArticleLocalService.getLatestKBArticle(model.getClassPK(), WorkflowConstants.STATUS_APPROVED);
                kbArticleLocalService.updateKBArticle(latestArticle);
            }
        } catch (PortalException e) {
            LOG.error("Something went wrong", e);
        }
    }

    @Override
    public void onBeforeAddAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onBeforeAddAssociation");
    }

    @Override
    public void onBeforeCreate(RatingsStats model) throws ModelListenerException {
        LOG.error("onBeforeCreate");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeRemove(RatingsStats model) throws ModelListenerException {
        LOG.error("onBeforeRemove");
        LOG.info("{}", getString(model));
    }

    @Override
    public void onBeforeRemoveAssociation(Object classPK, String associationClassName, Object associationClassPK) throws ModelListenerException {
        LOG.error("onBeforeRemoveAssociation");
    }

    @Override
    public void onBeforeUpdate(RatingsStats model) throws ModelListenerException {
        LOG.error("onBeforeUpdate");
        LOG.info("{}", getString(model));
    }

    private String getString(RatingsStats model) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(model.getClassName());
        stringBuilder.append("  ");
        stringBuilder.append(model.getClassPK());
        stringBuilder.append("  ");
        stringBuilder.append(model.getTotalScore());
        stringBuilder.append("  ");
        return stringBuilder.toString();
    }
}
