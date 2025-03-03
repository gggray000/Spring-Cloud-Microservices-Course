package com.appsdeveloperbolg.photoapp.api.users.ui.controllers;

import com.appsdeveloperbolg.photoapp.api.users.service.UsersService;
import com.appsdeveloperbolg.photoapp.api.users.shared.UserDto;
import com.appsdeveloperbolg.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperbolg.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.appsdeveloperbolg.photoapp.api.users.ui.model.UserResponseModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment env;

    @Autowired
    UsersService usersService;

    @GetMapping("/status/check")
    public String status(){

        return "Users Service is Working on port "
                + env.getProperty("local.server.port")
                + ", with token = "
                + env.getProperty("token.secret");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetail){
        UserDto userDto = convertUserRequestModelToUserDto(userDetail);
        UserDto createdUser = usersService.createUser(userDto);
        CreateUserResponseModel responseModel = convertUserDtoToResponseModel(createdUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseModel);
    }

    @GetMapping(
            value="/{userId}",
            produces={
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<UserResponseModel> getUsers(@PathVariable("userId") String userId){
        UserDto userDto = usersService.getUserByUserId(userId);
        UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    private UserDto convertUserRequestModelToUserDto(CreateUserRequestModel userDetails){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userDetails, UserDto.class);
    }

    private CreateUserResponseModel convertUserDtoToResponseModel(UserDto userDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userDto, CreateUserResponseModel.class);
    }

}

