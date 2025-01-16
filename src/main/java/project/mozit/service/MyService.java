package project.mozit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;
import project.mozit.repository.UsersRepository;
import project.mozit.util.JWTUtil;

@Service
@RequiredArgsConstructor
public class MyService {
    private final JWTUtil jwtUtil;
    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean verifyPassword(String token, String inputPassword) {
        String userid = jwtUtil.getUsername(token.replace("Bearer ", ""));

        Users user = userRepository.findByUserId(userid);

        return passwordEncoder.matches(inputPassword, user.getUserPwd());
    }

    public UsersDTO.Response getUserInfo(String token){
        String username = jwtUtil.getUsername(token.replace("Bearer ", ""));
        Users user = userRepository.findByUserId(username);

        UsersDTO.Response response = new UsersDTO.Response();
        response.setUserNum(user.getUserNum());
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setUserEmail(user.getUserEmail());

        if (user.getEnterpriseNum() != null) {
            response.setEnterpriseNum(user.getEnterpriseNum().getEnterpriseNum());
            Enterprises enterprise = user.getEnterpriseNum();
            response.setEnterpriseNum(enterprise.getEnterpriseNum());
            response.setEnterpriseName(enterprise.getEnterpriseName());
            response.setEnterpriseAddr(enterprise.getEnterpriseAddr());
            response.setEnterpriseCall(enterprise.getEnterpriseCall());
        } else {
            response.setEnterpriseNum(null);
        }

        return response;
    }

    @Transactional
    public void updateUserInfo(String token, UsersDTO.Patch updateDto) {
        String username = jwtUtil.getUsername(token.replace("Bearer ", ""));
        Users user = userRepository.findByUserId(username);

        if (updateDto.getUserName() != null) {
            user.setUserName(updateDto.getUserName());
        }

        if (updateDto.getUserEmail() != null) {
            user.setUserEmail(updateDto.getUserEmail());
        }

        if (updateDto.getUserPwd() != null) {
            user.setUserPwd(passwordEncoder.encode(updateDto.getUserPwd()));
        }

        userRepository.save(user);
    }
}
