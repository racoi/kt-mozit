package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.mozit.domain.Edits;
import project.mozit.dto.EditsDTO;

@Mapper(componentModel = "spring")
public interface EditsMapper {

    @Mapping(source = "userNum.userNum", target = "userNum")
    @Mapping(target = "timestamp", ignore = true)
    EditsDTO toDto(Edits edits);

    @Mapping(source = "userNum", target = "userNum.userNum")
    Edits toEntity(EditsDTO editsDTO);
}
