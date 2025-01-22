package project.mozit.mapper;

import org.mapstruct.*;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Notices;
import project.mozit.domain.Users;
import project.mozit.dto.NoticesDTO;
import project.mozit.dto.UsersDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "userNum", ignore = true)
    @Mapping(target = "userPwd", ignore = true)
    Users PostDTOToEntity(UsersDTO.Post post);


    @Mapping(target = "enterpriseNum", source = "enterpriseNum.enterpriseNum")
    @Mapping(target = "enterpriseName", source = "enterpriseNum.enterpriseName")
    UsersDTO.Response entityToResponse(Users user);

    List<UsersDTO.Response> usersToResponse(List<Users> users);

    default Enterprises mapEnterpriseNum(Long enterpriseNum) {
        if (enterpriseNum == null) return null;
        Enterprises enterprise = new Enterprises();
        enterprise.setEnterpriseNum(enterpriseNum);
        return enterprise;
    }

    default Long mapToLong(Enterprises enterprise) {
        return enterprise != null ? enterprise.getEnterpriseNum() : null;
    }
}
