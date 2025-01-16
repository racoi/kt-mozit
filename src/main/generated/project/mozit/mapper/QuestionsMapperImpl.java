package project.mozit.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.mozit.domain.Answers;
import project.mozit.domain.Questions;
import project.mozit.domain.Users;
import project.mozit.dto.QuestionsDTO.Post;
import project.mozit.dto.QuestionsDTO.Response;
import project.mozit.dto.QuestionsDTO.Response.AnswerResponse;
import project.mozit.dto.QuestionsDTO.UserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-16T16:46:55+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class QuestionsMapperImpl implements QuestionsMapper {

    @Override
    public Response entityToResponse(Questions question, Answers answer) {
        if ( question == null && answer == null ) {
            return null;
        }

        Response response = new Response();

        if ( question != null ) {
            response.setQuestionNum( question.getQuestionNum() );
            response.setTimestamp( question.getTimestamp() );
            response.setUserNum( usersToUserDTO( question.getUserNum() ) );
            response.setQuestionTitle( question.getQuestionTitle() );
            response.setQuestionDetail( question.getQuestionDetail() );
            response.setQuestionType( question.getQuestionType() );
            response.setQuestionState( question.getQuestionState() );
            response.setQuestionImage( question.getQuestionImage() );
        }
        if ( answer != null ) {
            response.setAnswerResponse( answerToResponse( answer ) );
        }

        return response;
    }

    @Override
    public Questions PostDTOToEntity(Post post) {
        if ( post == null ) {
            return null;
        }

        Questions questions = new Questions();

        questions.setQuestionTitle( post.getQuestionTitle() );
        questions.setQuestionDetail( post.getQuestionDetail() );
        questions.setQuestionType( post.getQuestionType() );
        questions.setQuestionImage( post.getQuestionImage() );

        return questions;
    }

    @Override
    public AnswerResponse answerToResponse(Answers answer) {
        if ( answer == null ) {
            return null;
        }

        AnswerResponse answerResponse = new AnswerResponse();

        answerResponse.setAnswerNum( answer.getAnswerNum() );
        answerResponse.setTimestamp( answer.getTimestamp() );
        answerResponse.setAnswerDetail( answer.getAnswerDetail() );

        return answerResponse;
    }

    @Override
    public List<Response> questionsToResponse(List<Questions> questions) {
        if ( questions == null ) {
            return null;
        }

        List<Response> list = new ArrayList<Response>( questions.size() );
        for ( Questions questions1 : questions ) {
            list.add( questionsToResponse1( questions1 ) );
        }

        return list;
    }

    protected UserDTO usersToUserDTO(Users users) {
        if ( users == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserNum( users.getUserNum() );
        userDTO.setUserId( users.getUserId() );
        userDTO.setUserName( users.getUserName() );
        userDTO.setUserEmail( users.getUserEmail() );

        return userDTO;
    }

    protected Response questionsToResponse1(Questions questions) {
        if ( questions == null ) {
            return null;
        }

        Response response = new Response();

        response.setQuestionNum( questions.getQuestionNum() );
        response.setTimestamp( questions.getTimestamp() );
        response.setQuestionTitle( questions.getQuestionTitle() );
        response.setQuestionDetail( questions.getQuestionDetail() );
        response.setQuestionType( questions.getQuestionType() );
        response.setQuestionState( questions.getQuestionState() );
        response.setQuestionImage( questions.getQuestionImage() );
        response.setUserNum( usersToUserDTO( questions.getUserNum() ) );

        return response;
    }
}
