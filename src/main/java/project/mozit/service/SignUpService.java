package project.mozit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDto;
import project.mozit.repository.UsersRepository;

@Service
public class SignUpService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UsersDto.Post usersDto){

        boolean isUser = userRepository.existsByUserId(usersDto.getUserId());
        if (isUser) {
            return;
        }

        Users data = new Users();

        data.setUserId(usersDto.getUserId());
        data.setUserPwd(bCryptPasswordEncoder.encode(usersDto.getUserPwd()));
        data.setUserName(usersDto.getUserName());
        data.setUserEmail(usersDto.getUserEmail());
        data.setUserRole("ROLE_USER");

        userRepository.save(data);
    }
}
