package com.arcavus.armada.movies.resource;

import com.arcavus.armada.movies.dto.data.DataLoaderMetricsDto;
import com.arcavus.armada.movies.service.data.DataLoader;
import com.arcavus.armada.movies.service.data.DataLoaderMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("data")
public class DataResource {

    private final DataLoader dataLoader;
    private final ThreadPoolExecutor workers;
    private final DataLoaderMetrics dataLoaderMetrics;

    @Autowired
    public DataResource(DataLoader dataLoader, ThreadPoolExecutor workers, DataLoaderMetrics dataLoaderMetrics) {
        this.dataLoader = dataLoader;
        this.workers = workers;
        this.dataLoaderMetrics = dataLoaderMetrics;
    }

    /*
        Note: 2018-11-04 21:13:28.189  INFO 7086 --- [pool-1-thread-1] c.c.l.movies.service.data.DataLoader     : Total time to load data in ms: 3524434

     */
    @RequestMapping(path = "load", method = RequestMethod.POST)
    public HttpEntity<Void> load() {
        workers.submit(dataLoader::runEtlJob);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "metrics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<DataLoaderMetricsDto> metrics() {
        long successfulRuns = dataLoaderMetrics.successfulRuns();
        return ResponseEntity.ok(new DataLoaderMetricsDto(successfulRuns));
    }

}
