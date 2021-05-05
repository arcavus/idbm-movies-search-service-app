package com.arcavus.armada.movies.dto.search.title;

import com.arcavus.armada.movies.dto.core.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchTitleResults {

    private final String query;
    private final long noOfResults;
    private final List<Title> searchResults;
}

