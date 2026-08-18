package tech.jhipster.sample.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.sample.domain.MapsIdUserProfileWithDTO;
import tech.jhipster.sample.repository.MapsIdUserProfileWithDTORepository;
import tech.jhipster.sample.repository.UserRepository;
import tech.jhipster.sample.service.MapsIdUserProfileWithDTOService;
import tech.jhipster.sample.service.dto.MapsIdUserProfileWithDTODTO;
import tech.jhipster.sample.service.mapper.MapsIdUserProfileWithDTOMapper;

/**
 * Service Implementation for managing {@link tech.jhipster.sample.domain.MapsIdUserProfileWithDTO}.
 */
@Service
@Transactional
public class MapsIdUserProfileWithDTOServiceImpl implements MapsIdUserProfileWithDTOService {

    private static final Logger LOG = LoggerFactory.getLogger(MapsIdUserProfileWithDTOServiceImpl.class);

    private final MapsIdUserProfileWithDTORepository mapsIdUserProfileWithDTORepository;

    private final MapsIdUserProfileWithDTOMapper mapsIdUserProfileWithDTOMapper;

    private final UserRepository userRepository;

    public MapsIdUserProfileWithDTOServiceImpl(
        MapsIdUserProfileWithDTORepository mapsIdUserProfileWithDTORepository,
        MapsIdUserProfileWithDTOMapper mapsIdUserProfileWithDTOMapper,
        UserRepository userRepository
    ) {
        this.mapsIdUserProfileWithDTORepository = mapsIdUserProfileWithDTORepository;
        this.mapsIdUserProfileWithDTOMapper = mapsIdUserProfileWithDTOMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<MapsIdUserProfileWithDTODTO> save(MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO) {
        LOG.debug("Request to save MapsIdUserProfileWithDTO : {}", mapsIdUserProfileWithDTODTO);
        return mapsIdUserProfileWithDTORepository
            .save(mapsIdUserProfileWithDTOMapper.toEntity(mapsIdUserProfileWithDTODTO))
            .map(mapsIdUserProfileWithDTOMapper::toDto);
    }

    @Override
    public Mono<MapsIdUserProfileWithDTODTO> update(MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO) {
        LOG.debug("Request to update MapsIdUserProfileWithDTO : {}", mapsIdUserProfileWithDTODTO);
        return mapsIdUserProfileWithDTORepository
            .save(mapsIdUserProfileWithDTOMapper.toEntity(mapsIdUserProfileWithDTODTO))
            .map(mapsIdUserProfileWithDTOMapper::toDto);
    }

    @Override
    public Mono<MapsIdUserProfileWithDTODTO> partialUpdate(MapsIdUserProfileWithDTODTO mapsIdUserProfileWithDTODTO) {
        LOG.debug("Request to partially update MapsIdUserProfileWithDTO : {}", mapsIdUserProfileWithDTODTO);

        return mapsIdUserProfileWithDTORepository
            .findById(mapsIdUserProfileWithDTODTO.getId())
            .map(existingMapsIdUserProfileWithDTO -> {
                mapsIdUserProfileWithDTOMapper.partialUpdate(existingMapsIdUserProfileWithDTO, mapsIdUserProfileWithDTODTO);

                return existingMapsIdUserProfileWithDTO;
            })
            .flatMap(mapsIdUserProfileWithDTORepository::save)
            .map(mapsIdUserProfileWithDTOMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MapsIdUserProfileWithDTODTO> findAll() {
        LOG.debug("Request to get all MapsIdUserProfileWithDTOS");
        return mapsIdUserProfileWithDTORepository.findAll().map(mapsIdUserProfileWithDTOMapper::toDto);
    }

    public Flux<MapsIdUserProfileWithDTODTO> findAllWithEagerRelationships(Pageable pageable) {
        return mapsIdUserProfileWithDTORepository.findAllWithEagerRelationships(pageable).map(mapsIdUserProfileWithDTOMapper::toDto);
    }

    public Mono<Long> countAll() {
        return mapsIdUserProfileWithDTORepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MapsIdUserProfileWithDTODTO> findOne(Long id) {
        LOG.debug("Request to get MapsIdUserProfileWithDTO : {}", id);
        return mapsIdUserProfileWithDTORepository.findOneWithEagerRelationships(id).map(mapsIdUserProfileWithDTOMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete MapsIdUserProfileWithDTO : {}", id);
        return mapsIdUserProfileWithDTORepository.deleteById(id);
    }
}
