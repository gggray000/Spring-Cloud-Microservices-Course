package com.appsdeveloperbolg.photoapp.api.users;

import com.appsdeveloperbolg.photoapp.api.users.data.*;
import com.appsdeveloperbolg.photoapp.api.users.shared.Roles;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Component
public class InitialUsersSetup {

    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UsersRepository usersRepository;

    private static final Logger log = LoggerFactory.getLogger(InitialUsersSetup.class);

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        log.info("From Application Ready Event.");

        AuthorityEntity readAuthority = createAuthority("READ");
        AuthorityEntity writeAuthority = createAuthority("WRITE");
        AuthorityEntity deleteAuthority = createAuthority("DELETE");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("Lisa");
        adminUser.setLastName("Moon");
        adminUser.setEmail("admin@test.com");
        adminUser.setUserId(UUID.randomUUID().toString());
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        if(usersRepository.findByEmail(adminUser.getEmail()) == null){
            usersRepository.save(adminUser);
        }
    }


    @Transactional
    protected AuthorityEntity createAuthority(String name){
        AuthorityEntity authority = authorityRepository.findByName(name);
        if(authority==null){
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    protected RoleEntity createRole(String name, Collection<AuthorityEntity> authorities){
        RoleEntity role = roleRepository.findByName(name);
        if(role == null){
            role = new RoleEntity(name, authorities);
            roleRepository.save(role);
        }
        return role;

    }
}
