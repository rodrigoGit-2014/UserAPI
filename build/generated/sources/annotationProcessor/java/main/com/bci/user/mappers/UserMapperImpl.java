package com.bci.user.mappers;

import com.bci.user.dto.PhoneRequestDto;
import com.bci.user.dto.PhoneResponseDto;
import com.bci.user.dto.UserRequestDto;
import com.bci.user.dto.UserResponseDto;
import com.bci.user.persistence.entity.PhoneEntity;
import com.bci.user.persistence.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T14:00:41-0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.8 (BellSoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity mapBase(UserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.name( dto.getName() );
        userEntity.email( dto.getEmail() );
        userEntity.password( dto.getPassword() );

        userEntity.id( UUID.randomUUID() );
        userEntity.created( LocalDateTime.now() );
        userEntity.modified( LocalDateTime.now() );
        userEntity.lastLogin( LocalDateTime.now() );
        userEntity.token( UUID.randomUUID().toString() );
        userEntity.active( true );

        return userEntity.build();
    }

    @Override
    public PhoneEntity toPhoneEntity(PhoneRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        PhoneEntity.PhoneEntityBuilder phoneEntity = PhoneEntity.builder();

        phoneEntity.number( dto.getNumber() );
        phoneEntity.citycode( dto.getCitycode() );
        phoneEntity.contrycode( dto.getContrycode() );

        return phoneEntity.build();
    }

    @Override
    public UserResponseDto toResponse(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponseDto.UserResponseDtoBuilder userResponseDto = UserResponseDto.builder();

        userResponseDto.phones( phoneEntityListToPhoneResponseDtoList( entity.getPhones() ) );
        userResponseDto.id( entity.getId() );
        userResponseDto.name( entity.getName() );
        userResponseDto.email( entity.getEmail() );
        userResponseDto.created( entity.getCreated() );
        userResponseDto.modified( entity.getModified() );
        userResponseDto.lastLogin( entity.getLastLogin() );
        userResponseDto.token( entity.getToken() );
        userResponseDto.active( entity.isActive() );

        return userResponseDto.build();
    }

    @Override
    public PhoneResponseDto toPhoneResponse(PhoneEntity entity) {
        if ( entity == null ) {
            return null;
        }

        PhoneResponseDto.PhoneResponseDtoBuilder phoneResponseDto = PhoneResponseDto.builder();

        phoneResponseDto.number( entity.getNumber() );
        phoneResponseDto.citycode( entity.getCitycode() );
        phoneResponseDto.contrycode( entity.getContrycode() );

        return phoneResponseDto.build();
    }

    protected List<PhoneResponseDto> phoneEntityListToPhoneResponseDtoList(List<PhoneEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<PhoneResponseDto> list1 = new ArrayList<PhoneResponseDto>( list.size() );
        for ( PhoneEntity phoneEntity : list ) {
            list1.add( toPhoneResponse( phoneEntity ) );
        }

        return list1;
    }
}
