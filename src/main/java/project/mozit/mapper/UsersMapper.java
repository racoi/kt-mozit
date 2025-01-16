package project.mozit.mapper;

import org.mapstruct.*;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "userNum", ignore = true)
    @Mapping(target = "enterpriseNum", source = "enterpriseNum", qualifiedByName = "mapEnterpriseNum")
    Users PostDTOToEntity(UsersDTO.Post post);

    @Mapping(target = "userNum", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "userEmail", ignore = true)
    @Mapping(target = "enterpriseNum", ignore = true)
    void PatchDTOToEntity(UsersDTO.Patch patch, @MappingTarget Users user);

    UsersDTO.Response entityToResponse(Users user);

    List<UsersDTO.Response> usersToResponse(List<Users> users);

    @Named("mapEnterpriseNum")
    default Enterprises mapEnterpriseNum(Long enterpriseNum) {
        Enterprises enterprise = new Enterprises();
        enterprise.setEnterpriseNum(enterpriseNum);
        return enterprise;
    }
}
