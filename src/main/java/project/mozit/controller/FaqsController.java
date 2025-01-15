package project.mozit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mozit.domain.Faqs;
import project.mozit.dto.FaqsDTO;
import project.mozit.service.FaqsService;

import java.util.List;

@RestController
@RequestMapping("/faqs")
@RequiredArgsConstructor
public class FaqsController {

    public final FaqsService faqsService;

    @GetMapping
    public List<FaqsDTO.Response> getFaqs(){
        return faqsService.findFaqs();
    }

    @GetMapping("/{faqId}")
    public FaqsDTO.Response getFaq(@PathVariable("faqId") Long id){
        return faqsService.findFaq(id);
    }

    @PostMapping
    public ResponseEntity<Faqs> insertFaq(@RequestBody FaqsDTO.Post dto){
        Faqs savedFaq = faqsService.insertFaq(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFaq);
    }

    @PatchMapping("/{faqId}")
    public Faqs updateFaq(@PathVariable("faqId") Long id, @RequestBody FaqsDTO.Patch dto){
        return faqsService.updateFaq(id, dto);
    }

    @DeleteMapping("/{faqId}")
    public void deleteFaq(@PathVariable("faqId") Long id){
        faqsService.deleteFaq(id);
    }
}
