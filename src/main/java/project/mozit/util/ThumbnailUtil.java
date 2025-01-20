package project.mozit.util;

import io.jsonwebtoken.io.IOException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class ThumbnailUtil {
    static {
        // OpenCV 라이브러리 로드
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void extractThumbnail(String videoPath, String thumbnailPath) throws IOException {
        Mat frame = new Mat();
        VideoCapture videoCapture = new VideoCapture(videoPath);

        // 비디오 파일 경로 로그 출력
        System.out.println("비디오 파일 경로: " + videoPath);

        if (videoCapture.isOpened()) {
            videoCapture.read(frame);  // 첫 번째 프레임 읽기
            if (!frame.empty()) {
                System.out.println("프레임 크기: " + frame.size()); // 프레임 크기 출력
                // 썸네일 이미지의 크기를 조정 (예: 100x100 픽셀)
                Imgproc.resize(frame, frame, new Size(100, 100));
                // 썸네일 이미지 저장
                Imgcodecs.imwrite(thumbnailPath, frame);
                System.out.println("썸네일이 저장되었습니다: " + thumbnailPath);
            } else {
                throw new IOException("프레임을 읽을 수 없습니다: " + videoPath);
            }
        } else {
            throw new IOException("비디오 파일을 열 수 없습니다: " + videoPath);
        }

        videoCapture.release(); // 리소스 해제
    }
}
