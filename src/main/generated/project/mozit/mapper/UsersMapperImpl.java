package project.mozit.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO.Patch;
import project.mozit.dto.UsersDTO.Post;
import project.mozit.dto.UsersDTO.Response;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-15T17:33:21+0900",
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
        users.setUserPwd( post.getUserPwd() );
        users.setUserName( post.getUserName() );
        users.setUserEmail( post.getUserEmail() );
        users.setEnterpriseNum( post.getEnterpriseNum() );

        return users;
    }

    @Override
    public void PatchDTOToEntity(Patch patch, Users user) {
        if ( patch == null ) {
            return;
        }

        user.setUserPwd( patch.getUserPwd() );
    }

    @Override
    public Response entityToResponse(Users user) {
        if ( user == null ) {
            return null;
        }

        Response response = new Response();

        response.setUserNum( user.getUserNum() );
        response.setUserId( user.getUserId() );
        response.setUserName( user.getUserName() );
        response.setUserEmail( user.getUserEmail() );
        response.setEnterpriseNum( user.getEnterpriseNum() );

        return response;
    }

    @Override
    public List<Response> usersToResponse(List<Users> users) {
        if ( users == null ) {
            return null;
        }

        List<Response> list = new ArrayList<Response>( users.size() );
        for ( Users users1 : users ) {
            list.add( entityToResponse( users1 ) );
        }

        return list;
    }
}
