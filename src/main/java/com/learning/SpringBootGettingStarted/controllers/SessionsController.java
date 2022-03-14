package com.learning.SpringBootGettingStarted.controllers;

import com.learning.SpringBootGettingStarted.models.Session;
import com.learning.SpringBootGettingStarted.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/sessions")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public List<Session> list() {
        return sessionRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Session get(@PathVariable Long id) {
        return sessionRepository.getById(id);
    }

    @PostMapping
    public Session create(@RequestBody final Session session){
        return sessionRepository.saveAndFlush(session);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        //quando si fa la delete sarebbe giusto controllare se ci sono record dipendenti da quello che sto cancellando
        //in modo da non rendere inconsistenti i dati
        sessionRepository.deleteById(id);
    }

    //quando si fa una update si può scegliere tra PUT e PATCH:
    // PUT --> modifico tutti i capi dell'entità
    // PATCH --> ne modifico solo alcuni
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update(@PathVariable Long id, @RequestBody Session session){
        Session existingSession = sessionRepository.getById(id);
        //con il copyProperties stiamo dicendo che dobbiamo prendere l'oggetto session, quello nuovo passato nella api
        // e copiare le sue proprietà in quello già esistente, chiaramente tutti tranne l'id che non deve cambiare
        // essendo la chiave dell'oggetto a DB (altrimenti si scatena un'eccezione)
        //TODO sarebbe più corretto prima di fare l'update validare gli attributi che ci arrivano
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }
}
