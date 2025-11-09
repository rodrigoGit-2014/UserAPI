package com.bci.user.mappers;

import com.bci.user.dto.PhoneRequestDto;
import com.bci.user.dto.PhoneResponseDto;
import com.bci.user.dto.UserRequestDto;
import com.bci.user.dto.UserResponseDto;
import com.bci.user.persistence.entity.PhoneEntity;
import com.bci.user.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, LocalDateTime.class})
public interface UserMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modified", expression = "java(LocalDateTime.now())")
    @Mapping(target = "lastLogin", expression = "java(LocalDateTime.now())")
    @Mapping(target = "token", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "phones", ignore = true)
    UserEntity mapBase(UserRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PhoneEntity toPhoneEntity(PhoneRequestDto dto);

    default UserEntity toEntity(UserRequestDto dto) {
        UserEntity user = mapBase(dto);
        if (dto.getPhones() != null) {
            List<PhoneEntity> phones = dto.getPhones().stream()
                    .map(p -> {
                        PhoneEntity entity = toPhoneEntity(p);
                        entity.setUser(user);
                        return entity;
                    })
                    .toList();
            user.setPhones(phones);
        }
        return user;
    }

    @Mapping(target = "phones", source = "phones")
    UserResponseDto toResponse(UserEntity entity);

    PhoneResponseDto toPhoneResponse(PhoneEntity entity);

}
