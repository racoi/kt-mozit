package project.mozit.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;
import project.mozit.repository.UsersRepository;

@Service
@AllArgsConstructor
public class SignUpService {

    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UsersDTO.Post usersDto){

        boolean isUser = userRepository.existsByUserId(usersDto.getUserId());
        if (isUser) {
            return;
        }

        Users data = new Users();

        data.setUserId(usersDto.getUserId());
        data.setUserPwd(bCryptPasswordEncoder.encode(usersDto.getUserPwd()));
        data.setUserName(usersDto.getUserName());
        data.setUserEmail(usersDto.getUserEmail());
//        data.setUserEmail(usersDto.getEnterpriseNum());

        userRepository.save(data);
    }

    public boolean checkUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }
}
