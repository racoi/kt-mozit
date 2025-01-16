package project.mozit.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO.Post;
import project.mozit.dto.UsersDTO.Response;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-16T15:56:07+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class UsersMapperImpl implements UsersMapper {

    @Override
    public Users PostDTOToEntity(Post post) {
        if ( post == null ) {
            return null;
        }

        Users users = new Users();

        users.setUserId( post.getUserId() );
        users.setUserName( post.getUserName() );
        users.setUserEmail( post.getUserEmail() );
        users.setEnterpriseNum( mapEnterpriseNum( post.getEnterpriseNum() ) );

        return users;
    }

    @Override
    public Response entityToResponse(Users user) {
        if ( user == null ) {
            return null;
        }

        Response response = new Response();

        response.setEnterpriseNum( userEnterpriseNumEnterpriseNum( user ) );
        response.setEnterpriseName( userEnterpriseNumEnterpriseName( user ) );
        response.setEnterpriseAddr( userEnterpriseNumEnterpriseAddr( user ) );
        response.setEnterpriseCall( userEnterpriseNumEnterpriseCall( user ) );
        response.setUserNum( user.getUserNum() );
        response.setUserId( user.getUserId() );
        response.setUserName( user.getUserName() );
        response.setUserEmail( user.getUserEmail() );

        return response;
    }

    private Long userEnterpriseNumEnterpriseNum(Users users) {
        if ( users == null ) {
            return null;
        }
        Enterprises enterpriseNum = users.getEnterpriseNum();
        if ( enterpriseNum == null ) {
            return null;
        }
        Long enterpriseNum1 = enterpriseNum.getEnterpriseNum();
        if ( enterpriseNum1 == null ) {
            return null;
        }
        return enterpriseNum1;
    }

    private String userEnterpriseNumEnterpriseName(Users users) {
        if ( users == null ) {
            return null;
        }
        Enterprises enterpriseNum = users.getEnterpriseNum();
        if ( enterpriseNum == null ) {
            return null;
        }
        String enterpriseName = enterpriseNum.getEnterpriseName();
        if ( enterpriseName == null ) {
            return null;
        }
        return enterpriseName;
    }

    private String userEnterpriseNumEnterpriseAddr(Users users) {
        if ( users == null ) {
            return null;
        }
        Enterprises enterpriseNum = users.getEnterpriseNum();
        if ( enterpriseNum == null ) {
            return null;
        }
        String enterpriseAddr = enterpriseNum.getEnterpriseAddr();
        if ( enterpriseAddr == null ) {
            return null;
        }
        return enterpriseAddr;
    }

    private String userEnterpriseNumEnterpriseCall(Users users) {
        if ( users == null ) {
            return null;
        }
        Enterprises enterpriseNum = users.getEnterpriseNum();
        if ( enterpriseNum == null ) {
            return null;
        }
        String enterpriseCall = enterpriseNum.getEnterpriseCall();
        if ( enterpriseCall == null ) {
            return null;
        }
        return enterpriseCall;
    }
}
