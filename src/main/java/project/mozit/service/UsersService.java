package project.mozit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;
import project.mozit.mapper.UsersMapper;
import project.mozit.repository.EnterprisesRepository;
import project.mozit.repository.UsersRepository;
import project.mozit.util.RedisUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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
<<<<<<< HEAD
=======
                    newEnterprise.setEnterpriseAddr(usersDto.getEnterpriseAddr());
                    newEnterprise.setEnterpriseCall(usersDto.getEnterpriseCall());
>>>>>>> d39e813934a412cd21484fe569c5905d24bf8fe5
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

}
