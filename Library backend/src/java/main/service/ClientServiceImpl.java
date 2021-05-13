package main.service;

import main.entity.Client;
import main.entity.JournalRecord;
import main.exeption.ClientNotFoundExeption;
import main.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> listClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<String> listClientFirstNames() {
        return clientRepository.findAll().stream().map(Client::getFirstName).collect(Collectors.toList());
    }

    @Override
    public Client findClientById(long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        }
        else {
            throw new ClientNotFoundExeption("Client not found");
        }
    }

    @Override
    public Client findClientsByFirstLastNameAndPassportNumber(String firstName, String lastName, String passportNum) {
        Optional<List<Client>> optionalClient =
            clientRepository.findAllByFirstNameAndLastNameAndPassportNum(firstName, lastName, passportNum);

        if (optionalClient.isPresent()) {
            return optionalClient.get().get(0);
        }
        else {
            throw new ClientNotFoundExeption("Client not found");
        }
    }

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            clientRepository.delete(optionalClient.get());
        }
        else {
            throw new ClientNotFoundExeption("Client not found");
        }
    }

    @Override
    public long[] getClientBookIds(long id) {
        return findClientById(id).getJournalRecords().stream().mapToLong(JournalRecord::getBookId).toArray();
    }
}
