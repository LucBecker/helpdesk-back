package com.lucbecker.orderservice.resources;

import com.lucbecker.orderservice.domain.OS;
import com.lucbecker.orderservice.dto.OSDTO;
import com.lucbecker.orderservice.services.OsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "os")
public class OsResource {

    @Autowired
    private OsService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<OSDTO> findById(@PathVariable Integer id){
        OSDTO obj = new OSDTO(service.findById(id));
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<OSDTO>> findAll(){
        List<OSDTO> listDTO = service.findAll()
                .stream().map(obj -> new OSDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);

    }

    @PostMapping
    public ResponseEntity<OSDTO> create(@Valid @RequestBody OSDTO objDTO){
        objDTO = new OSDTO(service.create(objDTO));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDTO.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @PutMapping
    public ResponseEntity<OSDTO> update(@Valid @RequestBody OSDTO obj){
        obj = new OSDTO(service.update(obj));
        return ResponseEntity.ok().body(obj);
    }
}
