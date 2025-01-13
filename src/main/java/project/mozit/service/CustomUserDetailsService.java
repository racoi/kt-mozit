package project.mozit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.mozit.domain.Users;
import project.mozit.dto.CustomUserDetails;
import project.mozit.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users userData = usersRepository.findByUserId(username);
        if(userData != null){
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
