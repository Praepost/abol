package com.abol.abol.app.sevices;

import com.abol.abol.app.contollers.dto.LoginDto;
import com.abol.abol.app.contollers.dto.PersonDto;
import com.abol.abol.app.exception.FingerprintNotFoundException;
import com.abol.abol.app.exception.UserAlreadyExistAuthenticationException;
import com.abol.abol.app.exception.UserNotFoundException;
import com.abol.abol.app.models.Person;
import com.abol.abol.app.models.Role;
import com.abol.abol.app.repositories.PersonRepository;
import com.abol.abol.app.repositories.RoleRepository;
import com.abol.abol.app.security.PersonDetails;
import com.abol.abol.app.security.config.PasswordEncoderImpl;
import com.abol.abol.app.security.dto.AccessTransfer;
import com.abol.abol.app.security.dto.AuthTransfer;
import com.abol.abol.app.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PasswordEncoderImpl passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final PersonRepository personRepository;
    @Transactional
    public AuthTransfer register(PersonDto personDto) throws UserAlreadyExistAuthenticationException {

        Role role = roleRepository.findByName("ROLE_USER").get();
        HashSet roles = new HashSet();
        roles.add(role);

        if(personRepository.findByUsername(personDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistAuthenticationException("User exists");
        }
        Person person = new Person();
        person.setUsername(personDto.getUsername());
        person.setPassword(passwordEncoder.getPasswordEncoder().encode(personDto.getPassword()));
        person.setRoles(roles);

        personRepository.save(person);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(personDto.getUsername(), personDto.getPassword());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        return generateJwtAccessAndRefresh(person);
    }

    private AuthTransfer generateJwtAccessAndRefresh(Person person) {
        String jwtRefresh, jwtAccess;

        jwtAccess = jwtUtil.generateAccessToken(person);
        jwtRefresh = jwtUtil.generateRefreshToken(person.getUsername());

        return new AuthTransfer(jwtAccess, jwtRefresh);
    }

    @Transactional
    public AccessTransfer createAccessToken(String refreshToken) throws UserNotFoundException, FingerprintNotFoundException {
        String jwtAccess;

        Optional<Person> person = personRepository.findByUsername(jwtUtil.extractUsername(refreshToken));
        if(person.isEmpty()){
            throw new FingerprintNotFoundException("User not found");
        }
        PersonDetails user = new PersonDetails(person.get());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        jwtAccess = jwtUtil.generateAccessToken(person.get());

        return new AccessTransfer(jwtAccess);
    }

    @Transactional
    public AuthTransfer authenticate(LoginDto loginDto) throws UserNotFoundException {
        String jwtRefresh, jwtAccess;

        Optional<Person> person = personRepository.findByUsername(loginDto.getUsername());

        if (person.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        Person user = person.get();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        // при создании токена в него кладется username как Subject claim и список authorities как кастомный claim
        jwtAccess = jwtUtil.generateAccessToken(user);
        jwtRefresh = jwtUtil.generateRefreshToken(user.getUsername());

        return new AuthTransfer(jwtAccess, jwtRefresh);
    }
}
