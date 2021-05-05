package com.arcavus.armada.movies.dto.links;

import com.arcavus.armada.movies.domain.Name;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FindAcquaintanceLinksResult {

    private String resultStatus;

    private Name sourceName;
    private Name targetName;

    private long degrees;

    @Singular
    private List<Link> links;

}
