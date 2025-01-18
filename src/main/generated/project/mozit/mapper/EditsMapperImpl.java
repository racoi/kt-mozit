package project.mozit.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.mozit.domain.Edits;
import project.mozit.domain.Users;
import project.mozit.dto.EditsDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-17T16:38:07+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class EditsMapperImpl implements EditsMapper {

    @Override
    public EditsDTO toDto(Edits edits) {
        if ( edits == null ) {
            return null;
        }

        EditsDTO editsDTO = new EditsDTO();

        editsDTO.setUserNum( editsUserNumUserNum( edits ) );
        editsDTO.setEditNum( edits.getEditNum() );
        editsDTO.setThumbnail( edits.getThumbnail() );

        return editsDTO;
    }

    @Override
    public Edits toEntity(EditsDTO editsDTO) {
        if ( editsDTO == null ) {
            return null;
        }

        Edits edits = new Edits();

        edits.setUserNum( editsDTOToUsers( editsDTO ) );
        edits.setEditNum( editsDTO.getEditNum() );
        edits.setTimestamp( editsDTO.getTimestamp() );
        edits.setThumbnail( editsDTO.getThumbnail() );

        return edits;
    }

    private Long editsUserNumUserNum(Edits edits) {
        if ( edits == null ) {
            return null;
        }
        Users userNum = edits.getUserNum();
        if ( userNum == null ) {
            return null;
        }
        Long userNum1 = userNum.getUserNum();
        if ( userNum1 == null ) {
            return null;
        }
        return userNum1;
    }

    protected Users editsDTOToUsers(EditsDTO editsDTO) {
        if ( editsDTO == null ) {
            return null;
        }

        Users users = new Users();

        users.setUserNum( editsDTO.getUserNum() );

        return users;
    }
}
