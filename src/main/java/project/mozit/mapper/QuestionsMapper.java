package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.mozit.domain.Answers;
import project.mozit.domain.Questions;
import project.mozit.dto.QuestionsDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionsMapper {

    @Mapping(target = "questionNum", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "questionState", ignore = true)
    Questions PostDTOToEntity(QuestionsDTO.Post post);

    @Mapping(source = "question.questionNum", target = "questionNum")
    @Mapping(source = "question.timestamp", target = "timestamp")
    @Mapping(source = "answer", target = "answerResponse")
    QuestionsDTO.Response entityToResponse(Questions question, Answers answer);

    QuestionsDTO.Response.AnswerResponse answerToResponse(Answers answer);

    List<QuestionsDTO.Response> questionsToResponse(List<Questions> questions);
}
