package project.mozit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Notices;
import project.mozit.domain.Users;
import project.mozit.dto.NoticesDTO;
import project.mozit.dto.UserWorkDownloadDTO;
import project.mozit.dto.UsersDTO;
import project.mozit.mapper.UsersMapper;
import project.mozit.repository.EnterprisesRepository;
import project.mozit.repository.UsersRepository;
import project.mozit.util.RedisUtil;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UsersRepository userRepository;
    private final EnterprisesRepository enterprisesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsersMapper usersMapper;
    private final RedisUtil redisUtil;

    public void joinProcess(UsersDTO.Post usersDto){

        boolean isUser = userRepository.existsByUserId(usersDto.getUserId());
        if (isUser) {
            return;
        }

        Enterprises enterprise = enterprisesRepository
                .findByEnterpriseNum(usersDto.getEnterpriseNum())
                .orElseGet(() -> {
                    Enterprises newEnterprise = new Enterprises();
                    newEnterprise.setEnterpriseNum(usersDto.getEnterpriseNum());
                    newEnterprise.setEnterpriseName(usersDto.getEnterpriseName());
                    return enterprisesRepository.save(newEnterprise);
                });


        Users data = usersMapper.PostDTOToEntity(usersDto);
        data.setUserPwd(bCryptPasswordEncoder.encode(usersDto.getUserPwd()));
        data.setEnterpriseNum(enterprise);

        userRepository.save(data);
    }

    public boolean checkUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public boolean checkUserEmail(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    public Optional<String> findUserId(String userEmail){
        return Optional.ofNullable(userRepository.findUserIdByUserEmail(userEmail));
    }

    public void logout(String username) {
        if (username != null) {
            redisUtil.deleteData(username);
            System.out.println("Redis에서 Refresh Token 삭제 완료");
        }
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        Users user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 사용자를 찾을 수 없습니다."));

        user.setUserPwd(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<UsersDTO.Response> findUsers(){
        List<Users> users = userRepository.findAll();
        return usersMapper.usersToResponse(users);
    }

    public List<UserWorkDownloadDTO.Response> findUserWorkDownload() {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            List<Object[]> results = userRepository.findUserWorkDownloadNative();
            return results.stream()
                    .map(r -> {
                        UserWorkDownloadDTO.Response response = new UserWorkDownloadDTO.Response();
                        // UserNum 처리: Long으로 처리
                        response.setUserNum(r[0] != null ? ((Long) r[0]) : 0L);
                        // Username 처리
                        response.setUsername((String) r[1]);
                        // EnterpriseName 처리
                        response.setEnterpriseName((String) r[2]);
                        // WorkCount 처리: Long으로 처리
                        response.setWorkCount(r[3] != null ? ((Long) r[3]) : 0L);
                        // DownloadCount 처리: Long으로 처리
                        response.setDownloadCount(r[4] != null ? ((Long) r[4]) : 0L);
                        return response;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error while fetching user work download data", e);  // 로그 남기기
            throw new RuntimeException("Error while fetching user work download data", e); // 예외 던지기
        }
    }
}
