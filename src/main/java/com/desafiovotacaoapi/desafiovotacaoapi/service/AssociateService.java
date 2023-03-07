package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.GetAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultExcepetion;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.AssociateMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociateService {

    private final AssociateRepository associateRepository;

    @Autowired
    public AssociateService(AssociateRepository repository) {
        this.associateRepository = repository;
    }

    public Associate createAssociate(CreateAssociateDTO associateDTO) {

        return this.associateRepository.save(AssociateMapper.buildAssociate(associateDTO));
    }

    public Associate getAssociateByID(Long associateId) {

        return this.associateRepository.findById(associateId)
                .orElseThrow(() -> new NullQueryResultExcepetion("Associate not found!"));

    }

    public List<GetAssociateDTO> getAllAssociates() {
        return this.associateRepository.findAll().stream().map(GetAssociateDTO::new).toList();
    }

}
