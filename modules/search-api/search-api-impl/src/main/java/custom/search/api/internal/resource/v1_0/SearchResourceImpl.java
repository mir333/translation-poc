package custom.search.api.internal.resource.v1_0;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.journal.model.JournalArticle;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.highlight.FieldConfig;
import com.liferay.portal.search.highlight.Highlight;
import com.liferay.portal.search.highlight.HighlightBuilder;
import com.liferay.portal.search.highlight.HighlightBuilderFactory;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.*;
import com.liferay.portal.search.query.function.score.ScoreFunctions;
import com.liferay.portal.search.query.function.score.ScriptScoreFunction;
import com.liferay.portal.search.script.Script;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.search.sort.SortBuilderFactory;
import com.liferay.portal.vulcan.pagination.Page;
import custom.search.api.dto.v1_0.Search;
import custom.search.api.internal.configuration.SearchConfiguration;
import custom.search.api.internal.configuration.SearchConfigurationHelper;
import custom.search.api.resource.v1_0.SearchResource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger LOG = LoggerFactory.getLogger(SearchResourceImpl.class);

    @Reference
    private SearchConfigurationHelper configurationHelper;
    @Reference
    private SearchEngineAdapter searchEngineAdapter;
    @Reference
    private Queries queries;
    @Reference
    private SortBuilderFactory factory;
    @Reference
    private ScoreFunctions scoreFunctions;
    @Reference
    private Scripts scripts;

    @Override
    public Page<Search> getSearch(String variation) throws Exception {
        final SearchConfiguration configuration = configurationHelper.getConfiguration();


        final BooleanQuery bq = queries.booleanQuery();

        StringQuery query = queries.string(variation);
        query.setEscape(false);
        bq.addMustQueryClauses(query);

        final BooleanQuery fileEntryFilter = queries.booleanQuery();
        fileEntryFilter.addMustQueryClauses(queries.term("entryClassName","com.liferay.document.library.kernel.model.DLFileEntry"));
        fileEntryFilter.addMustQueryClauses(queries.term("hidden","false"));
        fileEntryFilter.addMustQueryClauses(queries.term("status","0"));

        final BooleanQuery articleFilter = queries.booleanQuery();
        articleFilter.addMustQueryClauses(queries.term("entryClassName","com.liferay.journal.model.JournalArticle"));
        articleFilter.addMustQueryClauses(queries.term("head","true"));
        final TermsQuery tq = queries.terms("status");
        tq.addValue("0");
        articleFilter.addMustQueryClauses(tq);
        articleFilter.addShouldQueryClauses(queries.dateRangeTerm("expirationDate",false,false,"20220113161902",null));

        final BooleanQuery kbArticleFilter = queries.booleanQuery();
        kbArticleFilter.addMustQueryClauses(queries.term("entryClassName","com.liferay.knowledge.base.model.KBArticle"));

        final BooleanQuery filterBg = queries.booleanQuery();
        filterBg.addShouldQueryClauses(fileEntryFilter);
        filterBg.addShouldQueryClauses(articleFilter);
        filterBg.addShouldQueryClauses(kbArticleFilter);

        bq.addFilterQueryClauses(queries.booleanQuery().addMustQueryClauses(filterBg));


//		SortBuilder sortBuilder = factory.getSortBuilder();
//		final Sort ratings = sortBuilder.field("ratings").sortOrder(SortOrder.ASC).build();
//		searchSearchRequest.addSorts(ratings);
        final FunctionScoreQuery functionScoreQuery = queries.functionScore(bq);

//        final Script script = scripts.inline("painless", "1 + Math.log(1 + doc['priority_sortable'].value)");
//        final ScriptScoreFunction scoreFunction = scoreFunctions.script(script);
//        functionScoreQuery.addFilterQueryScoreFunctionHolder(null, scoreFunction);

        final Script script2 = scripts.script( configuration.scoringFunction());
        final ScriptScoreFunction scoreFunction2 = scoreFunctions.script(script2);
        functionScoreQuery.addFilterQueryScoreFunctionHolder(null, scoreFunction2);
        functionScoreQuery.setScoreMode(FunctionScoreQuery.ScoreMode.MAX);


        SearchSearchRequest searchSearchRequest =
                new SearchSearchRequest();

        final Query queryToUse = configuration.enableScoringFunction() ? functionScoreQuery: bq;

        searchSearchRequest.setIndexNames("_all");
        searchSearchRequest.setQuery(queryToUse);
        searchSearchRequest.setSize(20);
        searchSearchRequest.setScoreEnabled(true);
        searchSearchRequest.setFetchSourceIncludes(new String[]{"entryClassName", "title_en_US", "entryClassPK", "priority_sortable", "ratings","ratings_Number_sortable"});

        searchSearchRequest.setTrackTotalHits(true);
        searchSearchRequest.setExplain(true);

        SearchSearchResponse searchSearchResponse =
                searchEngineAdapter.execute(searchSearchRequest);

        SearchHits searchHits = searchSearchResponse.getSearchHits();
        final List<Search> collect = searchHits.getSearchHits().stream().map(searchHit -> {
            final Search search = new Search();
            search.setResult(searchHit.getDocument().toString());
            search.setScore(String.valueOf(searchHit.getScore()));
            return search;
        }).collect(Collectors.toList());

        LOG.error("Request: {}",searchSearchResponse.getSearchRequestString());
        LOG.error("Response: {}",searchSearchResponse.getSearchResponseString());

        return Page.of(collect);

    }
}
