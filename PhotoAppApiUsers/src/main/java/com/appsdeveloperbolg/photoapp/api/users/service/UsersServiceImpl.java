package com.appsdeveloperbolg.photoapp.api.users.service;

import com.appsdeveloperbolg.photoapp.api.users.data.UserEntity;
import com.appsdeveloperbolg.photoapp.api.users.data.UsersRepository;
import com.appsdeveloperbolg.photoapp.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService{

    UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = this.convertUserDtoToUserEntity(userDetails);
        usersRepository.save(userEntity);

        return convertUserEntityToUserDto(userEntity);
    }

    private UserEntity convertUserDtoToUserEntity(UserDto userDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword("test");
        return userEntity;
    }

    private UserDto convertUserEntityToUserDto(UserEntity userEntity){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userEntity, UserDto.class);
    }
}
