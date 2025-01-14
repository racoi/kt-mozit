package project.mozit.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mozit.domain.Answers;
import project.mozit.domain.Questions;
import project.mozit.dto.AnswersDTO;
import project.mozit.mapper.AnswersMapper;
import project.mozit.repository.AnswersRepository;
import project.mozit.repository.QuestionsRepository;

@Service
@RequiredArgsConstructor
public class AnswersService {

    private final AnswersRepository answersRepository;
    private final AnswersMapper answersMapper;
    private final QuestionsRepository questionsRepository;

    @Transactional
    public Answers insertAnswer(AnswersDTO.Post dto){
        Questions question = questionsRepository.findById(dto.getQuestionNum())
                .orElseThrow(() -> new EntityNotFoundException("문의가 존재하지 않습니다. ID: " + dto.getQuestionNum()));
        Answers answer = answersMapper.PostDTOToEntity(dto);
        answer.setQuestionNum(question);
        question.setQuestionState(true);
        return saveAnswer(answer);
    }

    public void deleteAnswer(Long id){
        Answers answer = answersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 답변입니다. ID: " + id));
        answersRepository.delete(answer);
    }

    public Answers saveAnswer(Answers answer){
        return answersRepository.save(answer);
    }
}
