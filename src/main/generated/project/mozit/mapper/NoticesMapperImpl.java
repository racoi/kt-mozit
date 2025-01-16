package project.mozit.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.mozit.domain.Notices;
import project.mozit.dto.NoticesDTO.Patch;
import project.mozit.dto.NoticesDTO.Post;
import project.mozit.dto.NoticesDTO.Response;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-16T16:39:59+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class NoticesMapperImpl implements NoticesMapper {

    @Override
    public Notices PostDTOToEntity(Post post) {
        if ( post == null ) {
            return null;
        }

        Notices notices = new Notices();

        notices.setNoticeTitle( post.getNoticeTitle() );
        notices.setNoticeDetail( post.getNoticeDetail() );

        return notices;
    }

    @Override
    public void PatchDTOToEntity(Patch patch, Notices notice) {
        if ( patch == null ) {
            return;
        }

        notice.setNoticeTitle( patch.getNoticeTitle() );
        notice.setNoticeDetail( patch.getNoticeDetail() );
    }

    @Override
    public Response entityToResponse(Notices notice) {
        if ( notice == null ) {
            return null;
        }

        Response response = new Response();

        response.setNoticeNum( notice.getNoticeNum() );
        response.setCreatedAt( notice.getCreatedAt() );
        response.setUpdatedAt( notice.getUpdatedAt() );
        response.setNoticeTitle( notice.getNoticeTitle() );
        response.setNoticeDetail( notice.getNoticeDetail() );

        return response;
    }

    @Override
    public List<Response> noticesToResponse(List<Notices> notices) {
        if ( notices == null ) {
            return null;
        }

        List<Response> list = new ArrayList<Response>( notices.size() );
        for ( Notices notices1 : notices ) {
            list.add( entityToResponse( notices1 ) );
        }

        return list;
    }
}
