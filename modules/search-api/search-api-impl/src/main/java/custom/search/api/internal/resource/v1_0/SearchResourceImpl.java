package custom.search.api.internal.resource.v1_0;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.journal.model.JournalArticle;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.*;
import com.liferay.portal.search.query.function.score.ScoreFunctions;
import com.liferay.portal.search.query.function.score.ScriptScoreFunction;
import com.liferay.portal.search.script.Script;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.search.sort.SortBuilderFactory;
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
    @Reference
    private SortBuilderFactory factory;
    @Reference
    private ScoreFunctions scoreFunctions;
    @Reference
    private Scripts scripts;

    @Override
    public Page<Search> getSearch(String variation) throws Exception {

        final BooleanQuery bq = queries.booleanQuery();

//      query.addMustQueryClauses(queries.match("title_en_US", variation));
//		MatchAllQuery query = queries.matchAll();
//		WildcardQuery query = queries.wildcard("content_en_US", variation);

        SimpleStringQuery query = queries.simpleString(variation);
        query.addFields("*");
        bq.addMustQueryClauses(query);


        TermsQuery terms= queries.terms("entryClassName");
        terms.addValues(JournalArticle.class.getName());
        terms.addValues(DLFileEntry.class.getName());
        terms.addValues(KBArticle.class.getName());
        bq.addMustQueryClauses(terms);

        bq.addShouldQueryClauses(queries.match("latest", "true"));

//		SortBuilder sortBuilder = factory.getSortBuilder();
//		final Sort ratings = sortBuilder.field("ratings").sortOrder(SortOrder.ASC).build();
//		searchSearchRequest.addSorts(ratings);
        final FunctionScoreQuery functionScoreQuery = queries.functionScore(bq);

//        final Script script = scripts.inline("painless", "1 + Math.log(1 + doc['priority_sortable'].value)");
//        final ScriptScoreFunction scoreFunction = scoreFunctions.script(script);
//        functionScoreQuery.addFilterQueryScoreFunctionHolder(null, scoreFunction);

        final Script script2 = scripts.script( "try{ if(doc.containsKey('ratings_Number_sortable')){1 + Math.log(1 + doc['ratings_Number_sortable'].value)} } catch(Exception e) {}");
        final ScriptScoreFunction scoreFunction2 = scoreFunctions.script(script2);
        functionScoreQuery.addFilterQueryScoreFunctionHolder(null, scoreFunction2);

        functionScoreQuery.setScoreMode(FunctionScoreQuery.ScoreMode.MAX);



        SearchSearchRequest searchSearchRequest =
                new SearchSearchRequest();

        searchSearchRequest.setIndexNames("_all");
        searchSearchRequest.setQuery(functionScoreQuery);
        searchSearchRequest.setSize(200);
        searchSearchRequest.setScoreEnabled(true);
        searchSearchRequest.setFetchSourceIncludes(new String[]{"entryClassName", "title_en_US", "entryClassPK", "priority_sortable", "ratings","ratings_Number_sortable"});


        SearchSearchResponse searchSearchResponse =
                searchEngineAdapter.execute(searchSearchRequest);

        SearchHits searchHits = searchSearchResponse.getSearchHits();
        final List<Search> collect = searchHits.getSearchHits().stream().map(searchHit -> {
            final Search search = new Search();
            search.setResult(searchHit.getDocument().toString());
            search.setScore(String.valueOf(searchHit.getScore()));
            return search;
        }).collect(Collectors.toList());

        return Page.of(collect);

    }
}
