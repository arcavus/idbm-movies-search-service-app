package com.arcavus.armada.movies.dto.coincidence;

import com.arcavus.armada.movies.domain.Name;
import com.arcavus.armada.movies.dto.core.Title;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FindCoincidenceResult {

    @Singular
    private List<String> executedForNames;

    @Singular
    private List<Name> names;

    private long noOfSharedTitles;

    @Singular
    private List<Title> sharedTitles;
}

