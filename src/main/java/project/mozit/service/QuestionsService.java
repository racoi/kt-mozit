package project.mozit.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mozit.domain.Answers;
import project.mozit.domain.Questions;
import project.mozit.dto.QuestionsDTO;
import project.mozit.mapper.QuestionsMapper;
import project.mozit.repository.AnswersRepository;
import project.mozit.repository.QuestionsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionsService {

    private final QuestionsRepository questionsRepository;
    private final AnswersRepository answersRepository;
    private final QuestionsMapper questionsMapper;

    public Questions insertQuestion(QuestionsDTO.Post dto){
        Questions question = questionsMapper.PostDTOToEntity(dto);
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

    public List<QuestionsDTO.Response> findQuestions(){
        List<Questions> questions = questionsRepository.findAll();
        return questionsMapper.questionsToResponse(questions);
    }

    public List<QuestionsDTO.Response> findQuestionsByUser(String userId){
        List<Questions> questions = questionsRepository.findByUserNum_UserId(userId);
        return questionsMapper.questionsToResponse(questions);
    }

    public Questions saveQuestion(Questions question){
        return questionsRepository.save(question);
    }
}
