package com.lucbecker.orderservice.services;

import com.lucbecker.orderservice.domain.Tecnico;
import com.lucbecker.orderservice.dto.TecnicoDTO;
import com.lucbecker.orderservice.repositories.TecnicoRepository;
import com.lucbecker.orderservice.services.exceptions.DataIntegrityViolationException;
import com.lucbecker.orderservice.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;

    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Tecnico.class.getName()));
    }

    public List<Tecnico> findAll() {
        return repository.findAll();
    }

    public Tecnico create(TecnicoDTO objDTO){
        if (findByCPF(objDTO) != null){
            throw new DataIntegrityViolationException("CPF já cadastrado na base de dados!");
        }
        return repository.save(new Tecnico(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
    }

    private Tecnico findByCPF(TecnicoDTO objDTO){
        Tecnico obj = repository.findByCPF(objDTO.getCpf());
        if(obj != null){
            return obj;
        }
        return null;
    }
}
