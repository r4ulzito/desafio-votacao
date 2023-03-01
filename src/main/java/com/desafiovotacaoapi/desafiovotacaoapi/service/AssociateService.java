package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.GetAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.AssociateRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.validation.ValidateQueryIsNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssociateService {

    private final AssociateRepository associateRepository;

    @Autowired
    public AssociateService(AssociateRepository repository) {
        this.associateRepository = repository;
    }

    public Associate createAssociate(CreateAssociateDTO newAssociate) {
        return this.associateRepository.save(new Associate(newAssociate));
    }

    public Associate getAssociateByID(Long associateId) {

        Optional<Associate> associate = this.associateRepository.findById(associateId);

        ValidateQueryIsNull.queryIsNull(associate, "Associate not found!");

        return associate.get();
    }


    public List<GetAssociateDTO> getAllAssociates() {
        return this.associateRepository.findAll().stream().map(GetAssociateDTO::new).toList();
    }

}
