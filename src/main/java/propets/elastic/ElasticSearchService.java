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
	
	public ArrayList<LostPet> find(FoundPet foundPet) {
		ArrayList<LostPet> resultList = new ArrayList<LostPet>();
		Item[] items = new Item[1];
		Item i = new Item("foundpets","_doc",foundPet.getId());
		items[0]=i;
		String[] fields;
		if(foundPet.getTags().length()>10) {
			fields = new String[1];
			fields[0] = "tags";
		}else {
			fields = new String[8];
			fields[0]="type";
			fields[1]="color";
			fields[2]="location";
			fields[3]="breed";
			fields[4]="tags";
			fields[5]="sex";
			fields[6]="description";
			fields[7]="distinction";
		}
		
		
		
		MoreLikeThisQueryBuilder moreLikeThisQuery = QueryBuilders.moreLikeThisQuery(fields,null,items);
		
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withQuery(moreLikeThisQuery);
		NativeSearchQuery query = nativeSearchQueryBuilder.build();
		//query.setMaxResults(2);
		query.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchHits<LostPet> lostPetNew = elasticsearchTemplate.search(query,LostPet.class);
		for (SearchHit hit : lostPetNew.getSearchHits()) {
			if(((LostPet)hit.getContent()).getType().equals(foundPet.getType())) {
				resultList.add((LostPet)hit.getContent());
			}
		}
		return resultList;

		
	}
	
	public ArrayList<FoundPet> find(LostPet lostPet) {
		ArrayList<FoundPet> resultList = new ArrayList<FoundPet>();
		Item[] items = new Item[1];
		Item i = new Item("lostpets","_doc",lostPet.getId());
		items[0]=i;
		String[] fields;
		if(lostPet.getTags().length()>10) {
			fields = new String[1];
			fields[0] = "tags";
		}else {
			fields = new String[8];
			fields[0]="type";
			fields[1]="color";
			fields[2]="location";
			fields[3]="breed";
			fields[4]="tags";
			fields[5]="sex";
			fields[6]="description";
			fields[7]="distinction";
		}
		
		MoreLikeThisQueryBuilder moreLikeThisQuery = QueryBuilders.moreLikeThisQuery(fields,null,items);
		
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withQuery(moreLikeThisQuery);
		NativeSearchQuery query = nativeSearchQueryBuilder.build();
		query.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchHits<FoundPet> lostPetNew = elasticsearchTemplate.search(query,FoundPet.class);
		for (SearchHit hit : lostPetNew.getSearchHits()) {
			if(((FoundPet)hit.getContent()).getType().equals(lostPet.getType())) {
				resultList.add((FoundPet)hit.getContent());
			}
		}
		return resultList;

		
	}
	
	/*
	 * public void find() { ArrayList<LostPet> resultList = new
	 * ArrayList<LostPet>(); Optional<LostPet> lostPet =
	 * lostPetRepository.findById("609a89daac835e57c5178759"); Item[] items = new
	 * Item[1]; Item i = new Item("lostpets","_doc","609a89daac835e57c5178759");
	 * items[0]=i; String[] fields = new String[5]; fields[0]="type";
	 * fields[1]="color"; fields[2]="location"; fields[3]="breed"; fields[4]="tags";
	 * 
	 * MoreLikeThisQueryBuilder moreLikeThisQuery =
	 * QueryBuilders.moreLikeThisQuery(fields,null,items);
	 * 
	 * NativeSearchQueryBuilder nativeSearchQueryBuilder = new
	 * NativeSearchQueryBuilder();
	 * nativeSearchQueryBuilder.withQuery(moreLikeThisQuery); NativeSearchQuery
	 * query = nativeSearchQueryBuilder.build();
	 * query.setSearchType(SearchType.DFS_QUERY_THEN_FETCH); SearchHits<LostPet>
	 * lostPetNew = elasticsearchTemplate.search(query,LostPet.class); for
	 * (SearchHit hit : lostPetNew.getSearchHits()) {
	 * if(((LostPet)hit.getContent()).getType().equals(lostPet.get().getType())) {
	 * resultList.add((LostPet)hit.getContent()); } } return;
	 * 
	 * 
	 * }
	 */
	
}
