package project.mozit.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mozit.domain.Faqs;
import project.mozit.dto.FaqsDTO;
import project.mozit.mapper.FaqsMapper;
import project.mozit.repository.FaqsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqsService {

    private final FaqsRepository faqsRepository;
    private final FaqsMapper faqsMapper;

    public Faqs insertFaq(FaqsDTO.Post dto){
        Faqs faq = faqsMapper.PostDTOToEntity(dto);
        return saveFaq(faq);
    }

    public Faqs updateFaq(Long id, FaqsDTO.Patch dto){
        Faqs faq = faqsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 FAQ입니다. id: " + id));
        faqsMapper.PatchDTOToEntity(dto, faq);
        return saveFaq(faq);
    }

    public FaqsDTO.Response findFaq(Long id){
        Faqs faq = faqsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 FAQ입니다. id: " + id));
        return faqsMapper.entityToResponse(faq);
    }

    public List<FaqsDTO.Response> findFaqs(){
        List<Faqs> faqs = faqsRepository.findAll();
        return faqsMapper.faqsToResponse(faqs);
    }

    public void deleteFaq(Long id){
        Faqs faq = faqsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 FAQ입니다. id: " + id));
        faqsRepository.delete(faq);
    }

    public Faqs saveFaq(Faqs faq){
        return faqsRepository.save(faq);
    }
}
