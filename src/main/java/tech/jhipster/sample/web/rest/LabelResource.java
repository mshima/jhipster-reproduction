package tech.jhipster.sample.web.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.Label;
import tech.jhipster.sample.repository.LabelRepository;
import tech.jhipster.sample.service.LabelService;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;
import tech.jhipster.sample.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Label}.
 */
@RestController
@RequestMapping("/api/labels")
public class LabelResource {

    private static final Logger LOG = LoggerFactory.getLogger(LabelResource.class);

    private final LabelService labelService;

    public LabelResource(LabelService labelService) {
        this.labelService = labelService;
    }

    /**
     * {@code GET  /labels} : get all the Labels.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Labels in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Label>>> getAllLabels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Labels");
        return labelService
            .countAll()
            .zipWith(labelService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /labels/:id} : get the "id" label.
     *
     * @param id the id of the label to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the label, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Label>> getLabel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Label : {}", id);
        Mono<Label> label = labelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(label);
    }

    /**
     * {@code SEARCH  /labels/_search?query=:query} : search for the label corresponding
     * to the query.
     *
     * @param query the query of the label search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<Label>>> searchLabels(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of Labels for query {}", query);
        return labelService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(labelService.search(query, pageable)));
    }
}
