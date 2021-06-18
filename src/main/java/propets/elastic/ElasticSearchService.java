package propets.elastic;

import java.util.ArrayList;
import java.util.Optional;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import propets.model.FoundPet;
import propets.model.LostPet;

@Service
public class ElasticSearchService {

	@Autowired
	ElasticsearchOperations elasticsearchTemplate;

	@Autowired
	RestHighLevelClient highLevelClient;
	
	@Autowired
	FoundPetRepository foundPetRepository;

	public ArrayList<LostPet> find(FoundPet foundPet) {
		ArrayList<LostPet> resultList = new ArrayList<LostPet>();
		StringBuilder builder = new StringBuilder();
		builder.append(foundPet.getTags());
		builder.append(" " + foundPet.getDescription());
		builder.append(" " + foundPet.getType());
		builder.append(" " + foundPet.getLocation());
		builder.append(" " + foundPet.getDistinction());
		builder.append(" " + foundPet.getLocation());
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withQuery(QueryBuilders.simpleQueryStringQuery(builder.toString()));

		NativeSearchQuery query = nativeSearchQueryBuilder.build();
		query.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchHits<LostPet> lostPetNew = elasticsearchTemplate.search(query, LostPet.class);
		for (SearchHit hit : lostPetNew.getSearchHits()) {
			if (((LostPet) hit.getContent()).getType().equals(foundPet.getType())) {
				resultList.add((LostPet) hit.getContent());
			}
		}
		return resultList;

	}

	public ArrayList<FoundPet> find(LostPet lostPet) {
		ArrayList<FoundPet> resultList = new ArrayList<FoundPet>();
		StringBuilder builder = new StringBuilder();
		builder.append(lostPet.getTags());
		builder.append(" " + lostPet.getDescription());
		builder.append(" " + lostPet.getType());
		builder.append(" " + lostPet.getLocation());
		builder.append(" " + lostPet.getDistinction());
		builder.append(" " + lostPet.getColor());

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withQuery(QueryBuilders.simpleQueryStringQuery(builder.toString()));

		NativeSearchQuery query = nativeSearchQueryBuilder.build();
		query.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchHits<FoundPet> foundPetHits = elasticsearchTemplate.search(query, FoundPet.class);
		for (SearchHit hit : foundPetHits.getSearchHits()) {
			if (((FoundPet) hit.getContent()).getType().equals(lostPet.getType())) {
				resultList.add((FoundPet) hit.getContent());
			}
		}
		return resultList;

	}

	// for testing purposes and tunning in debug
	public void find() {
		ArrayList<LostPet> resultList = new ArrayList<LostPet>();
		Optional<FoundPet> foundPet = foundPetRepository.findById("60cce3a5caad731e0a228cb0");	

		StringBuilder builder = new StringBuilder();
		builder.append(foundPet.get().getTags());
		builder.append(" " + foundPet.get().getDescription());
		builder.append(" " + foundPet.get().getType());
		builder.append(" " + foundPet.get().getLocation());
		builder.append(" " + foundPet.get().getDistinction());
		builder.append(" " + foundPet.get().getColor());

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withQuery(QueryBuilders.simpleQueryStringQuery(builder.toString()));

		NativeSearchQuery query = nativeSearchQueryBuilder.build();
		query.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchHits<LostPet> lostPetNew = elasticsearchTemplate.search(query, LostPet.class);
		for (SearchHit hit : lostPetNew.getSearchHits()) {
			if (((LostPet) hit.getContent()).getType().equals(foundPet.get().getType())) {
				resultList.add((LostPet) hit.getContent());
			}
		}
		return;

	}
	 

}
