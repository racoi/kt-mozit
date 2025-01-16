package project.mozit.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.mozit.domain.Enterprises;
import project.mozit.domain.Users;
import project.mozit.dto.UsersDTO;
import project.mozit.repository.EnterprisesRepository;
import project.mozit.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UsersRepository userRepository;
    private final EnterprisesRepository enterprisesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
                    newEnterprise.setEnterpriseAddr(usersDto.getEnterpriseAddr());
                    newEnterprise.setEnterpriseCall(usersDto.getEnterpriseCall());
                    return enterprisesRepository.save(newEnterprise);
                });


        Users data = new Users();

        data.setUserId(usersDto.getUserId());
        data.setUserPwd(bCryptPasswordEncoder.encode(usersDto.getUserPwd()));
        data.setUserName(usersDto.getUserName());
        data.setUserEmail(usersDto.getUserEmail());
        data.setEnterpriseNum(enterprise);

        userRepository.save(data);
    }

    public boolean checkUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }
}
