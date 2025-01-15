package project.mozit.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mozit.domain.Notices;
import project.mozit.dto.NoticesDTO;
import project.mozit.mapper.NoticesMapper;
import project.mozit.repository.NoticesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticesService {

    private final NoticesRepository noticesRepository;
    private final NoticesMapper noticesMapper;

    public Notices insertNotice(NoticesDTO.Post dto){
        Notices notice = noticesMapper.PostDTOToEntity(dto);
        return saveNotice(notice);
    }

    public Notices updateNotice(Long id, NoticesDTO.Patch dto){
        Notices notice = noticesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지입니다. id: " + id));
        noticesMapper.PatchDTOToEntity(dto, notice);
        return saveNotice(notice);
    }

    public NoticesDTO.Response findNotice(Long id){
        Notices notice = noticesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지입니다. id: " + id));
        return noticesMapper.entityToResponse(notice);
    }

    public List<NoticesDTO.Response> findNotices(){
        List<Notices> notices = noticesRepository.findAll();
        return noticesMapper.noticesToResponse(notices);
    }

    public void deleteNotice(Long id){
        Notices notice = noticesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지입니다. id: " + id));
        noticesRepository.delete(notice);
    }

    public Notices saveNotice(Notices notice){
        return noticesRepository.save(notice);
    }
}
