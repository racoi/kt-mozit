package project.mozit.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import project.mozit.dto.UsersDto;
import project.mozit.service.SignUpService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private SignUpService signUpService;

    @PostMapping("/signup")
    public Map<String, String> joinProcess(@RequestBody UsersDto.Post usersDto){
        System.out.println("joinProc accessed with data: " + usersDto.getUserName());
        System.out.println("joinProc accessed with data: " + usersDto.getUserPwd());

        signUpService.joinProcess(usersDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Signup successful");
        return response;
    }

    @GetMapping("logout")
    public Map<String, String> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Logout successful");
        return responseMap;
    }

}
