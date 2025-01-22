package project.mozit.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
<<<<<<< HEAD
    date = "2025-01-21T10:28:20+0900",
=======
    date = "2025-01-17T16:38:07+0900",
>>>>>>> d39e813934a412cd21484fe569c5905d24bf8fe5
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class FaqsMapperImpl implements FaqsMapper {

    @Override
    public Faqs PostDTOToEntity(Post post) {
        if ( post == null ) {
            return null;
        }

        Faqs faqs = new Faqs();

        faqs.setFaqQuestion( post.getFaqQuestion() );
        faqs.setFaqAnswer( post.getFaqAnswer() );

        return faqs;
    }

    @Override
    public void PatchDTOToEntity(Patch patch, Faqs faq) {
        if ( patch == null ) {
            return;
        }

        faq.setFaqQuestion( patch.getFaqQuestion() );
        faq.setFaqAnswer( patch.getFaqAnswer() );
    }

    @Override
    public Response entityToResponse(Faqs faq) {
        if ( faq == null ) {
            return null;
        }

        Response response = new Response();

        response.setFaqNum( faq.getFaqNum() );
        response.setFaqQuestion( faq.getFaqQuestion() );
        response.setFaqAnswer( faq.getFaqAnswer() );

        return response;
    }

    @Override
    public List<Response> faqsToResponse(List<Faqs> faqs) {
        if ( faqs == null ) {
            return null;
        }

        List<Response> list = new ArrayList<Response>( faqs.size() );
        for ( Faqs faqs1 : faqs ) {
            list.add( entityToResponse( faqs1 ) );
        }

        return list;
    }
}
