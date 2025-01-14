package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsersMapper {

    @Mapping(target = "userNum", ignore = true)
    Users PostDTOToEntity(UsersDTO.Post post);

    @Mapping(target = "userNum", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "userEmail", ignore = true)
    @Mapping(target = "enterpriseNum", ignore = true)
    void PatchDTOToEntity(UsersDTO.Patch patch, @MappingTarget Users user);

    UsersDTO.Response entityToResponse(Users user);

    List<UsersDTO.Response> usersToResponse(List<Users> users);
}
