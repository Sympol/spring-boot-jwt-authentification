package sb.learn.authjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sb.learn.authjwt.models.ERole;
import sb.learn.authjwt.models.Role;
import sb.learn.authjwt.models.User;
import sb.learn.authjwt.payload.request.LoginRequest;
import sb.learn.authjwt.payload.request.SignupRequest;
import sb.learn.authjwt.payload.response.JwtResponse;
import sb.learn.authjwt.payload.response.MessageResponse;
import sb.learn.authjwt.repositories.RoleRepository;
import sb.learn.authjwt.repositories.UserRepository;
import sb.learn.authjwt.security.jwt.JwtUtils;
import sb.learn.authjwt.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Symplice BONI
 * project auth-jwt
 * date 21/10/2020
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String ROLE_IS_NOT_FOUND = "Error: Role is not found.";
    @Autowired
    private
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    /***
     * Authentication to get json web tokens
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    /**
     * Create a new user account
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No chance! Someone is already using this name. Please choose another..."));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The email entered is already associated with an account!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> requestRole = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (requestRole == null) {
            Role userRole = roleRepository.findByName(ERole.SIMPLE_USER)
                    .orElseThrow(() -> new RuntimeException(""));
            roles.add(userRole);
        } else {
            requestRole.forEach(role -> {
                switch (role) {
                    case "boss":
                        Role supAdmin = roleRepository.findByName(ERole.SUPER_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                        roles.add(supAdmin);

                        break;
                    case "admin":
                        Role admin = roleRepository.findByName(ERole.SIMPLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                        roles.add(admin);

                        break;
                    case "article":
                        Role writer = roleRepository.findByName(ERole.ARTICLE_WRITER)
                                .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                        roles.add(writer);

                        break;
                    default:
                        Role simpleUser = roleRepository.findByName(ERole.SIMPLE_USER)
                                .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                        roles.add(simpleUser);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Awesome! User has been successfully registered!"));
    }
}
