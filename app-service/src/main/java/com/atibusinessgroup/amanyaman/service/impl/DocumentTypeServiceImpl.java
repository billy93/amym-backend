package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.DocumentTypeService;
import com.atibusinessgroup.amanyaman.domain.DocumentType;
import com.atibusinessgroup.amanyaman.repository.DocumentTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DocumentType}.
 */
@Service
@Transactional
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

    private final DocumentTypeRepository DocumentTypeRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository DocumentTypeRepository) {
        this.DocumentTypeRepository = DocumentTypeRepository;
    }

    @Override
    public DocumentType save(DocumentType DocumentType) {
        log.debug("Request to save DocumentType : {}", DocumentType);
        return DocumentTypeRepository.save(DocumentType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentType> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return DocumentTypeRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentType> findOne(Long id) {
        log.debug("Request to get DocumentType : {}", id);
        return DocumentTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentType : {}", id);
        DocumentTypeRepository.deleteById(id);
    }
}
