package com.lucbecker.orderservice.services;

import com.lucbecker.orderservice.domain.Cliente;
import com.lucbecker.orderservice.domain.OS;
import com.lucbecker.orderservice.domain.Tecnico;
import com.lucbecker.orderservice.domain.enums.Prioridade;
import com.lucbecker.orderservice.domain.enums.Status;
import com.lucbecker.orderservice.dto.OSDTO;
import com.lucbecker.orderservice.repositories.OSRepository;
import com.lucbecker.orderservice.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OsService {

    @Autowired
    private OSRepository repository;

    @Autowired
    private TecnicoService tecnicoService;

    @Autowired
    private ClienteService clienteService;

    public OS findById(Integer id){
        Optional<OS> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: "+ OS.class.getName()));
    }

    public List<OS> findAll() {
        return repository.findAll();
    }

    public OS create(OSDTO objDTO) {
        return fromDTO(objDTO);
    }

    public OS update(OSDTO obj) {
        findById(obj.getId());
        return fromDTO(obj);
    }

    private OS fromDTO(OSDTO obj){
        OS newObj = new OS();
        newObj.setId(obj.getId());
        newObj.setObservacoes(obj.getObservacoes());
        newObj.setPrioridade(Prioridade.toEnum(obj.getPrioridade().getCod()));
        newObj.setStatus(Status.toEnum(obj.getStatus().getCod()));

        Tecnico tec = tecnicoService.findById(obj.getTecnico());
        Cliente cli = clienteService.findById(obj.getCliente());

        newObj.setTecnico(tec);
        newObj.setCliente(cli);

        if(newObj.getStatus().getCod().equals(2)){
            newObj.setDataFechamento(LocalDateTime.now());
        }
        return repository.save(newObj);
    }
}
