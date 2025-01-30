package project.mozit.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoResponse {
    private List<FrameInfo> frames; // 여러 프레임 정보를 저장하는 리스트

    @Getter
    @Setter
    public static class FrameInfo {
        private int frame; // 현재 프레임 번호
        private List<ObjectInfo> detections; // 감지된 객체 정보 리스트
    }

    @Getter
    @Setter
    public static class ObjectInfo {
        private int objectId; // objectId를 int로 변경
        private String className;
        private float x;
        private float y;
        private float width;
        private float height;
        private float confidence; // confidence 추가
    }
}
