package com.arcavus.armada.movies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.arcavus.armada.movies.domain.Basic;
import com.arcavus.armada.movies.dto.core.Title;
import com.arcavus.armada.movies.repository.BasicRepository;
import com.arcavus.armada.movies.service.mapper.TitleMapperService;
import com.arcavus.armada.movies.service.search.SearchTitleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchTitleServiceTest {

    @Mock
    private BasicRepository basicRepositoryMock;

    @Mock
    private TitleMapperService titleMapperServiceMock;

    @InjectMocks
    private SearchTitleService searchTitleService ;

    @Test
    void testByGenre_NotEmptyPerson_returnNotEmptyList() {
        List<Basic> basicsByGenre = new ArrayList<>();
        Basic basic = new Basic();
        basic.setGenres("Action");
        basic.setOriginalTitle("Dead Pool");
        basicsByGenre.add(basic);
        Title record = new Title("1",null,null,"Dead Pool",false,"","",null,"Action",null,null,null,null,null,null,null);
        when(basicRepositoryMock.findXTopRatedTitlesByGenre(refEq("Action"))).thenReturn(basicsByGenre);
        when(titleMapperServiceMock.map(basic,false)).thenReturn(record);
        Optional<List<Title>> list =  searchTitleService.byGenre("Action",false);
        Assertions.assertTrue(list.isPresent());
        Assertions.assertEquals(list.get().get(0).getGenres(),basic.getGenres());
        Assertions.assertEquals(list.get().get(0).getOriginalTitle(),basic.getOriginalTitle());
    }
}

