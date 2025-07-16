package peaksoft.springsecurityjava17.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserApi {

    @GetMapping
    public String getMain(){
        return "main";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String getUser(){
        return "user";
    }

    @GetMapping("/delete")
    public String deleteUser(){
        return "delete";
    }
}
