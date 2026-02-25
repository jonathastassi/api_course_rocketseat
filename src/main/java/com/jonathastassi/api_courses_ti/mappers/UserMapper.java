package com.jonathastassi.api_courses_ti.mappers;

import org.springframework.stereotype.Component;

import com.jonathastassi.api_courses_ti.dtos.CreateUserRequestDto;
import com.jonathastassi.api_courses_ti.dtos.CreateUserResponseDto;
import com.jonathastassi.api_courses_ti.dtos.ListUserResponseDto;
import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.enums.UserRoleEnum;

@Component
public class UserMapper {
    public User toEntity(
            CreateUserRequestDto createUserRequestDto) {
        return User.builder()
                .name(createUserRequestDto.name())
                .email(createUserRequestDto.email())
                .password(createUserRequestDto.password())
                .role(UserRoleEnum.valueOf(createUserRequestDto.role().toUpperCase()))
                .build();
    }

    public CreateUserResponseDto toResponseDto(User user) {
        return new CreateUserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().toString());
    }

    public ListUserResponseDto toResponseListDto(User user) {
        return new ListUserResponseDto(
                user.getId().toString(),
                user.getName(),
                user.getEmail());
    }
}
