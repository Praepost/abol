package com.abol.abol.app.sevices;

import com.abol.abol.app.models.Person;
import com.abol.abol.app.repositories.PersonRepository;
import com.abol.abol.app.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService{
    private final PersonRepository personRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if(person.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return new PersonDetails(person.get());
    }



}
