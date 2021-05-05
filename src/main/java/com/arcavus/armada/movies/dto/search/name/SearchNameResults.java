package com.arcavus.armada.movies.dto.search.name;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchNameResults {

    private final String name;
    private final long noOfResults;
    private final List<PersonGenreInfo> personGenreInfos;

}
