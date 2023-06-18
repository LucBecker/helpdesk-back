package com.lucbecker.orderservice.services;

import com.lucbecker.orderservice.domain.Cliente;
import com.lucbecker.orderservice.domain.Pessoa;
import com.lucbecker.orderservice.dto.ClienteDTO;
import com.lucbecker.orderservice.repositories.ClienteRepository;
import com.lucbecker.orderservice.repositories.PessoaRepository;
import com.lucbecker.orderservice.services.exceptions.DataIntegrityViolationException;
import com.lucbecker.orderservice.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;

    public Cliente findById(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente create(ClienteDTO objDTO){
        if (findByCPF(objDTO) != null){
            throw new DataIntegrityViolationException("CPF já cadastrado na base de dados!");
        }
        return repository.save(new Cliente(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
    }

    public Cliente update(Integer id, ClienteDTO objDTO) {
        Cliente oldObj = findById(id);

        if(findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) {
            throw new DataIntegrityViolationException("CPF já cadastrado na base de dados!");
        }

        oldObj.setNome(objDTO.getNome());
        oldObj.setCpf(objDTO.getCpf());
        oldObj.setTelefone(objDTO.getTelefone());
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        Cliente obj = findById(id);

        if(obj.getList().size() > 0){
            throw new DataIntegrityViolationException("Pessoa possui Ordens de Serviço, não pode ser deletado!");
        }
        repository.deleteById(id);
    }

    private Pessoa findByCPF(ClienteDTO objDTO){
        Pessoa obj = pessoaRepository.findByCPF(objDTO.getCpf());
        if(obj != null){
            return obj;
        }
        return null;
    }
}
