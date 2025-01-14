package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.mozit.domain.Answers;
import project.mozit.dto.AnswersDTO;

@Mapper(componentModel = "spring")
public interface AnswersMapper {

    @Mapping(target = "answerNum", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "questionNum", ignore = true)
    Answers PostDTOToEntity(AnswersDTO.Post post);

    AnswersDTO.Response entityToResponse(Answers answer);
}
