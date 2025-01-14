package project.mozit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mozit.domain.Answers;
import project.mozit.dto.AnswersDTO;
import project.mozit.service.AnswersService;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswersController {

    public final AnswersService answersService;

    @PostMapping
    public ResponseEntity<Answers> insertAnswer(@RequestBody AnswersDTO.Post dto){
        Answers savedAnswer = answersService.insertAnswer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnswer);
    }

    @DeleteMapping("/{answerId}")
    public void deleteAnswer(@PathVariable("answerId") Long id){
        answersService.deleteAnswer(id);
    }
}
