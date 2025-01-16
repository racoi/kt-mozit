package project.mozit.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mozit.domain.Answers;
import project.mozit.domain.Questions;
import project.mozit.domain.Users;
import project.mozit.dto.QuestionsDTO;
import project.mozit.mapper.QuestionsMapper;
import project.mozit.repository.AnswersRepository;
import project.mozit.repository.QuestionsRepository;
import project.mozit.repository.UsersRepository;
import project.mozit.util.JWTUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionsService {

    private final QuestionsRepository questionsRepository;
    private final AnswersRepository answersRepository;
    private final UsersRepository usersRepository;
    private final QuestionsMapper questionsMapper;
    public final JWTUtil jwtUtil;

    public String getUsername(String token){
        return jwtUtil.getUsername(token.replace("Bearer ", ""));
    }

    public Questions insertQuestion(String token, QuestionsDTO.Post dto){
        String userId = getUsername(token);
        Users userNum = usersRepository.findByUserId(userId);
        if (userNum == null) {
            throw new EntityNotFoundException("해당 유저를 찾을 수 없습니다. ID: " + userId);
        }

        Questions question = questionsMapper.PostDTOToEntity(dto);
        question.setUserNum(userNum);
        return saveQuestion(question);
    }

    public void deleteQuestion(Long id){
        Questions question = questionsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 문의입니다. ID: " + id));
        questionsRepository.delete(question);
    }

    public QuestionsDTO.Response findQuestion(Long id){
        Questions question = questionsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 문의입니다. ID: " + id));
        Answers answer = answersRepository.findByQuestionNum(question).orElse(null);
        return questionsMapper.entityToResponse(question, answer);
    }

    public List<QuestionsDTO.Response> findQuestions() {
        List<Questions> questions = questionsRepository.findAll();

        return questions.stream()
                .map(question -> {
                    Answers answer = answersRepository.findByQuestionNum(question).orElse(null);
                    return questionsMapper.entityToResponse(question, answer);
                })
                .collect(Collectors.toList());
    }

    public List<QuestionsDTO.Response> findQuestionsByUser(String userId){
        List<Questions> questions = questionsRepository.findByUserNum_UserId(userId);
        return questionsMapper.questionsToResponse(questions);
    }

    public Questions saveQuestion(Questions question){
        return questionsRepository.save(question);
    }

    public QuestionsDTO.UserDTO convertUserToDTO(Users user) {
        QuestionsDTO.UserDTO userDTO = new QuestionsDTO.UserDTO();
        userDTO.setUserNum(user.getUserNum());
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserEmail(user.getUserEmail());
        return userDTO;
    }

    public QuestionsDTO.Response convertToResponse(Questions question) {
        QuestionsDTO.Response response = new QuestionsDTO.Response();
        response.setQuestionNum(question.getQuestionNum());
        response.setTimestamp(question.getTimestamp());
        response.setQuestionTitle(question.getQuestionTitle());
        response.setQuestionDetail(question.getQuestionDetail());
        response.setQuestionType(question.getQuestionType());
        response.setQuestionState(question.getQuestionState());
        response.setQuestionImage(question.getQuestionImage());
        response.setUserNum(convertUserToDTO(question.getUserNum()));  // UserDTO로 변환
        return response;
    }

}
