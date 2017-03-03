package masterSpringMvc.search.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import masterSpringMvc.search.LightTweet;
import masterSpringMvc.search.TwitterSearch;

@RestController
@RequestMapping("/api/search")
public class SearchApiController {
	private TwitterSearch twitterSearch;

	@Autowired
	public SearchApiController(TwitterSearch twitterSearch) {
		this.twitterSearch = twitterSearch;
	}

	@RequestMapping(value = "/{searchType}", method = RequestMethod.GET)
	public List<LightTweet> search(@PathVariable String searchType, @MatrixVariable List<String> keywords) {
		return twitterSearch.search(searchType, keywords);
	}
}
