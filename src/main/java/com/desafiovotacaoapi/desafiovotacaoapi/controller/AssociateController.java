package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.GetAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.service.AssociateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associates")
public class AssociateController {

    private final AssociateService service;

    @Autowired
    public AssociateController(AssociateService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GetAssociateDTO>> getAllAsssociates() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllAssociates());
    }

    @PostMapping
    public ResponseEntity<Associate> createAssociate(@Valid @RequestBody CreateAssociateDTO data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAssociate(data));
    }

}
