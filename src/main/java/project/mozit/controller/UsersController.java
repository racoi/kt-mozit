package project.mozit.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.mozit.dto.UsersDto;
import project.mozit.service.JoinService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private JoinService joinService;

    @PostMapping("/signup")
    public Map<String, String> joinProcess(@RequestBody UsersDto usersDto){
        System.out.println("joinProc accessed with data: " + usersDto.getUserName());
        System.out.println("joinProc accessed with data: " + usersDto.getUserPwd());

        joinService.joinProcess(usersDto);

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
