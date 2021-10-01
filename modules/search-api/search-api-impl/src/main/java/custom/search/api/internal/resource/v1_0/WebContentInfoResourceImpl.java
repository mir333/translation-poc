package custom.search.api.internal.resource.v1_0;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.util.JournalContent;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.ratings.kernel.service.RatingsEntryLocalService;
import custom.search.api.dto.v1_0.WebContentInfo;
import custom.search.api.resource.v1_0.WebContentInfoResource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import javax.ws.rs.core.Response;

/**
 * @author ligasm
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/web-content-info.properties",
	scope = ServiceScope.PROTOTYPE, service = WebContentInfoResource.class
)
public class WebContentInfoResourceImpl extends BaseWebContentInfoResourceImpl {
	@Reference
	private PermissionCheckerFactory permissionCheckerFactory;

	@Reference
	private UserLocalService userLocalService;

	@Reference
	private JournalArticleService journalArticleService;

	@Reference
	private JournalContent journalContent;

	@Reference
	private RatingsEntryLocalService ratingsEntryService;

	@Override
	public Response postWebcontent(WebContentInfo webContentInfo) throws Exception {

		final User userByEmailAddress = userLocalService.getUserByEmailAddress(contextCompany.getCompanyId(), webContentInfo.getEmail());

		final PermissionChecker originalPermissionChecker = PermissionThreadLocal.getPermissionChecker();

		final PermissionChecker userPermissionChecker = permissionCheckerFactory.create(userByEmailAddress);

		PermissionThreadLocal.setPermissionChecker(userPermissionChecker);


		final JournalArticle latestArticle = journalArticleService.getLatestArticle(webContentInfo.getSiteId(), webContentInfo.getArticleId(), WorkflowConstants.STATUS_APPROVED);
		final String result = journalContent.getContent(latestArticle.getGroupId(), latestArticle.getArticleId(), null, webContentInfo.getLang());


		ratingsEntryService.updateEntry(userByEmailAddress.getUserId(),latestArticle.getClassName(),latestArticle.getClassPK(),0.5,null);

		PermissionThreadLocal.setPermissionChecker(originalPermissionChecker);
		return Response.ok(result).build();
	}
}
