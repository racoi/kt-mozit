package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.mozit.domain.Notices;
import project.mozit.dto.NoticesDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticesMapper {

    @Mapping(target = "noticeNum", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Notices PostDTOToEntity(NoticesDTO.Post post);

    @Mapping(target = "noticeNum", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void PatchDTOToEntity(NoticesDTO.Patch patch, @MappingTarget Notices notice);

    NoticesDTO.Response entityToResponse(Notices notice);

    List<NoticesDTO.Response> noticesToResponse(List<Notices> notices);
}
