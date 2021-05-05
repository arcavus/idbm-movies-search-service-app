package com.arcavus.armada.movies.resource;

import com.arcavus.armada.movies.core.Urls;
import com.arcavus.armada.movies.dto.coincidence.FindCoincidence;
import com.arcavus.armada.movies.dto.coincidence.FindCoincidenceResult;
import com.arcavus.armada.movies.dto.search.name.PersonGenreInfo;
import com.arcavus.armada.movies.dto.search.name.SearchNameResults;
import com.arcavus.armada.movies.service.coincidence.FindCoincidenceService;
import com.arcavus.armada.movies.validator.SearchByNameValidator;
import com.arcavus.armada.movies.service.search.SearchNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("search/names")
public class SearchNameResource {

    @Autowired
    private SearchByNameValidator searchByNameValidator;

    @Autowired
    private SearchNameService searchNameService;

    @Autowired
    private FindCoincidenceService findCoincidenceService;

    @Autowired
    private Urls urls;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<SearchNameResults> search(@RequestParam(name = "name") String name, @RequestParam(name = "full-fetch", defaultValue = "false") boolean fullFetch) {
        name = urls.decoder(name);
        searchByNameValidator.test(name);
        Optional<List<PersonGenreInfo>> searchResults = searchNameService.byName(name, fullFetch);
        List<PersonGenreInfo> personGenreInfos = searchResults
                .filter(l -> !l.isEmpty())
                .orElseGet(ArrayList::new);
        return ResponseEntity.ok(new SearchNameResults(name, personGenreInfos.size(), personGenreInfos));
    }

    @RequestMapping(value = "coincidence", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<FindCoincidenceResult> search(@RequestBody FindCoincidence findCoincidence) {
        List<String> requestedNames = findCoincidence.getNames();
        searchByNameValidator.test(requestedNames);
        Optional<FindCoincidenceResult> searchResult = findCoincidenceService.search(requestedNames);
        return ResponseEntity.ok(searchResult.orElseGet(FindCoincidenceResult::new));
    }

}
