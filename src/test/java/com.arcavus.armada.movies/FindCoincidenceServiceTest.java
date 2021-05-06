package com.arcavus.armada.movies;

import com.arcavus.armada.movies.domain.Basic;
import com.arcavus.armada.movies.domain.Name;
import com.arcavus.armada.movies.dto.coincidence.FindCoincidenceResult;
import com.arcavus.armada.movies.repository.BasicRepository;
import com.arcavus.armada.movies.repository.NamesRepository;
import com.arcavus.armada.movies.service.coincidence.FindCoincidenceService;
import com.arcavus.armada.movies.service.mapper.TitleMapperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindCoincidenceServiceTest {

    @Mock
    private NamesRepository namesRepositoryMock;
    @Mock
    private BasicRepository basicRepositoryMock;
    @Mock
    private TitleMapperService titleMapperServiceMock;


    @InjectMocks
    private FindCoincidenceService findCoincidenceService;


    @Test
    void testSearch_notEmptyMovie_returnSuccess() {
        FindCoincidenceResult expected = new FindCoincidenceResult();
        List<String> names = new ArrayList<>();
        names.add("Will Smith");
        names.add("Charles Bronson");

        List<Name> nameList = new ArrayList<>();
        Name name = new Name();
        name.setPrimaryName("Will Smith");
        name.setKnownForTitles("Actor");
        name.setNconst("A195236");
        nameList.add(name);
        when(namesRepositoryMock.findByPrimaryName(refEq(names.get(0)))).thenReturn(nameList);

        List<Basic> basicsByPrimaryName = new ArrayList<>();
        Basic basic = new Basic();
        basic.setTconst(name.getNconst());
        basicsByPrimaryName.add(basic);

        List<String> nconsts = nameList
                .stream()
                .map(Name::getNconst)
                .collect(Collectors.toList());

        when(basicRepositoryMock.findTitlesByNconst(refEq(nconsts))).thenReturn(basicsByPrimaryName);
        FindCoincidenceResult findCoincidenceResult = new FindCoincidenceResult(null,nameList,1,null);

        Optional<FindCoincidenceResult> actual = findCoincidenceService.search(names);

        Assertions.assertEquals(actual.get().getNames(),findCoincidenceResult.getNames());

    }
}