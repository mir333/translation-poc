package custom.search.api.internal.resource.v1_0;

import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.WildcardQuery;
import com.liferay.portal.vulcan.pagination.Page;
import custom.search.api.dto.v1_0.Search;
import custom.search.api.resource.v1_0.SearchResource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ligasm
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/search.properties",
	scope = ServiceScope.PROTOTYPE, service = SearchResource.class
)
public class SearchResourceImpl extends BaseSearchResourceImpl {
	@Reference
	private SearchEngineAdapter searchEngineAdapter;
	@Reference
	private Queries queries;

	@Override
	public Page<Search> getSearch(String variation) throws Exception {

		WildcardQuery wildcardQuery = queries.wildcard("content_en_US", variation);

		SearchSearchRequest searchSearchRequest =
				new SearchSearchRequest();

		searchSearchRequest.setIndexNames("_all");
		searchSearchRequest.setQuery(wildcardQuery);
		searchSearchRequest.setSize(20);

		SearchSearchResponse searchSearchResponse =
				searchEngineAdapter.execute(searchSearchRequest);

		SearchHits searchHits = searchSearchResponse.getSearchHits();
		final List<Search> collect = searchHits.getSearchHits().stream().map(searchHit -> {
			final Search search = new Search();
			search.setResult(searchHit.getDocument().toString());
			return search;
		}).collect(Collectors.toList());

		return Page.of(collect);

	}
}
