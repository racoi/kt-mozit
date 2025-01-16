package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.mozit.domain.Answers;
import project.mozit.domain.Questions;
import project.mozit.dto.QuestionsDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionsMapper {
    @Mapping(source = "question.questionNum", target = "questionNum")
    @Mapping(source = "question.timestamp", target = "timestamp")
    @Mapping(source = "question.userNum", target = "userNum")
    @Mapping(source = "answer", target = "answerResponse")
    QuestionsDTO.Response entityToResponse(Questions question, Answers answer);

    Questions PostDTOToEntity(QuestionsDTO.Post post);

    QuestionsDTO.Response.AnswerResponse answerToResponse(Answers answer);

    List<QuestionsDTO.Response> questionsToResponse(List<Questions> questions);
}
