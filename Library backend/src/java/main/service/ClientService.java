package main.service;


import main.entity.Client;

import java.util.List;

public interface ClientService {

    List<Client> listClients();

    List<String> listClientFirstNames();

    Client findClientById(long id);

    Client findClientsByFirstLastNameAndPassportNumber(String firstName, String lastName, String passportNum);

    Client addClient(Client client);

    void deleteClient(long id);

    long[] getClientBookIds(long id);

}
