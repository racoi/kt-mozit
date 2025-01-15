package project.mozit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mozit.domain.Notices;
import project.mozit.dto.NoticesDTO;
import project.mozit.service.NoticesService;

import java.util.List;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticesController {

    public final NoticesService noticesService;

    @GetMapping
    public List<NoticesDTO.Response> getNotices(){
        return noticesService.findNotices();
    }

    @GetMapping("/{noticeId}")
    public NoticesDTO.Response getNotice(@PathVariable("noticeId") Long id){
        return noticesService.findNotice(id);
    }

    @PostMapping
    public ResponseEntity<Notices> insertNotice(@RequestBody NoticesDTO.Post dto){
        Notices savedNotice = noticesService.insertNotice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice);
    }

    @PatchMapping("/{noticeId}")
    public Notices updateNotice(@PathVariable("noticeId") Long id, @RequestBody NoticesDTO.Patch dto){
        return noticesService.updateNotice(id, dto);
    }

    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable("noticeId") Long id){
        noticesService.deleteNotice(id);
    }
}
