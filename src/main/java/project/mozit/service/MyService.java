package project.mozit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;
import project.mozit.mapper.UsersMapper;
import project.mozit.repository.UsersRepository;
import project.mozit.util.JWTUtil;

@Service
@RequiredArgsConstructor
public class MyService {
    private final JWTUtil jwtUtil;
    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsersMapper usersMapper;

    private String extractUsernameFromToken(String token) {
        return jwtUtil.getUsername(token.replace("Bearer ", ""));
    }

    public boolean verifyPassword(String token, String inputPassword) {
        String userid = extractUsernameFromToken(token);
        Users user = userRepository.findByUserId(userid);

        return passwordEncoder.matches(inputPassword, user.getUserPwd());
    }

    public UsersDTO.Response getUserInfo(String token){
        String username = extractUsernameFromToken(token);
        Users user = userRepository.findByUserId(username);

        return usersMapper.entityToResponse(user);
    }

    @Transactional
    public void updateUserInfo(String token, UsersDTO.Patch updateDto) {
        String username = extractUsernameFromToken(token);
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

        user.setUserSub(updateDto.getUserSub());

        userRepository.save(user);
    }
}
