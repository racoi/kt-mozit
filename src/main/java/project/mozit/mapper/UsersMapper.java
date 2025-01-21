package project.mozit.mapper;

import org.mapstruct.*;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "userNum", ignore = true)
    @Mapping(target = "userPwd", ignore = true)
    Users PostDTOToEntity(UsersDTO.Post post);


    @Mapping(target = "enterpriseNum", source = "enterpriseNum.enterpriseNum")
    @Mapping(target = "enterpriseName", source = "enterpriseNum.enterpriseName")
<<<<<<< HEAD
=======
    @Mapping(target = "enterpriseAddr", source = "enterpriseNum.enterpriseAddr")
    @Mapping(target = "enterpriseCall", source = "enterpriseNum.enterpriseCall")
>>>>>>> d39e813934a412cd21484fe569c5905d24bf8fe5
    UsersDTO.Response entityToResponse(Users user);

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
