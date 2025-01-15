package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.mozit.domain.Faqs;
import project.mozit.dto.FaqsDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FaqsMapper {

    @Mapping(target = "faqNum", ignore = true)
    Faqs PostDTOToEntity(FaqsDTO.Post post);

    @Mapping(target = "faqNum", ignore = true)
    void PatchDTOToEntity(FaqsDTO.Patch patch, @MappingTarget Faqs faq);

    FaqsDTO.Response entityToResponse(Faqs faq);

    List<FaqsDTO.Response> faqsToResponse(List<Faqs> faqs);
}
