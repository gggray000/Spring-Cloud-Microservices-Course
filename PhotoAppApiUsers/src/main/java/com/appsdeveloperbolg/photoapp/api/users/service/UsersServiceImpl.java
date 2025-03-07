package com.appsdeveloperbolg.photoapp.api.users.service;

import com.appsdeveloperbolg.photoapp.api.users.data.*;
import com.appsdeveloperbolg.photoapp.api.users.shared.UserDto;
import com.appsdeveloperbolg.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService{

    UsersRepository usersRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    //RestTemplate restTemplate;
    Environment environment;
    AlbumsServiceClient albumsServiceClient;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
                            Environment environment,
                            //RestTemplate restTemplate,
                            AlbumsServiceClient albumsServiceClient){
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        //this.restTemplate = restTemplate;
        this.albumsServiceClient = albumsServiceClient;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        UserEntity userEntity = this.convertUserDtoToUserEntity(userDetails);
        usersRepository.save(userEntity);

        return convertUserEntityToUserDto(userEntity);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return convertUserEntityToUserDto(userEntity);
    }

    @Override
    public UserDto getUserByUserId(String userId, String authorization) {
        UserEntity userEntity = usersRepository.findByUserId(userId);
        if(userEntity == null) throw new UsernameNotFoundException("User not found");
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

/*  Rest Template approach
        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(
                albumsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AlbumResponseModel>>() {}
        );
        List<AlbumResponseModel> albums =  albumsListResponse.getBody();*/

/*      Try catch block approach (comparing using FeignErrorDecoder)
        try {
            List<AlbumResponseModel> albums = albumsServiceClient.getAlbums(userId);
        } catch (FeignException e) {
            logger.error(e.getLocalizedMessage());
            albums = new ArrayList<>();
        }*/
        logger.debug("Before calling albums Microservice");
        List<AlbumResponseModel> albums = albumsServiceClient.getAlbums(userId, authorization);
        logger.debug("After calling albums Microservice");

        userDto.setAlbums(albums);
        return userDto;
    }

    private UserEntity convertUserDtoToUserEntity(UserDto userDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userDto, UserEntity.class);
    }

    private UserDto convertUserEntityToUserDto(UserEntity userEntity){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userEntity, UserDto.class);
    }

    // Invoked by Spring Framework to find username and password when users try to log in.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if(userEntity == null) throw new UsernameNotFoundException(username);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Collection<RoleEntity> roles = userEntity.getRoles();

        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            Collection<AuthorityEntity> authorityEntities = role.getAuthorities();
            authorityEntities.forEach((authorityEntity) -> {
                authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
            });
        });

        return new User(userEntity.getEmail(),
                        userEntity.getEncryptedPassword(),
                        true,
                        true,
                        true,
                        true,
                        authorities
        ) ;
    }
}
