package peaksoft.springsecurityjava17.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.springsecurityjava17.config.JWTService;
import peaksoft.springsecurityjava17.dto.AuthResponse;
import peaksoft.springsecurityjava17.dto.LoginRequest;
import peaksoft.springsecurityjava17.dto.RegisterRequest;
import peaksoft.springsecurityjava17.entities.User;
import peaksoft.springsecurityjava17.repo.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signIn(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.getEmail()));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadCredentialsException(
                    "Invalid Credentials"
            );
        }

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                jwtService.generateToken(user),
                user.getRole()
        );
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new BadCredentialsException(
                    "User with this email Already Exist"
            );
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                jwtService.generateToken(user),
                user.getRole()
        );
    }
}
