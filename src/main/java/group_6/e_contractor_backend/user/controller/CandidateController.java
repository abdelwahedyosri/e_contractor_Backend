package group_6.e_contractor_backend.user.controller;

import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.service.impl.CandidateService;
import group_6.e_contractor_backend.user.service.impl.RoleService;
import group_6.e_contractor_backend.user.util.JwtUtil;
import group_6.e_contractor_backend.user.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/register/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping(consumes = {"multipart/form-data"}, produces = {"application/json"})
    public ResponseEntity<?> registerCandidate(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("dob") String dob,
            @RequestParam("region") String region,
            @RequestParam("professionalTitle") String professionalTitle,
            @RequestParam("location") String location,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "video", required = false) String video,
            @RequestParam(value = "minRate", required = false) Double minRate,
            @RequestParam("resumeCategory") String resumeCategory,
            @RequestParam("resumeContent") String resumeContent,
            @RequestParam("skills") String skills,
            @RequestParam("phoneNumber") String phoneNumber) {

        // Fetching the role entity for 'candidate'
        Optional<RoleEntity> roleEntityOptional = roleService.findByRole("candidate");
        if (!roleEntityOptional.isPresent()) {
            throw new RuntimeException("Role 'candidate' not found");
        }
        RoleEntity roleEntity = roleEntityOptional.get();

        UserEntity userEntity = new UserEntity();
        userEntity.setRole(roleEntity);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        userEntity.setPassword(password);

        CandidateEntity candidateEntity = new CandidateEntity();
        candidateEntity.setFirstName(firstName);
        candidateEntity.setLastName(lastName);
        candidateEntity.setDob(dob);
        candidateEntity.setRegion(region);
        candidateEntity.setProfessionalTitle(professionalTitle);
        candidateEntity.setLocation(location);
        candidateEntity.setPhoto(photo != null ? photo.getOriginalFilename() : null);
        candidateEntity.setVideo(video);
        candidateEntity.setMinRate(minRate);
        candidateEntity.setResumeCategory(resumeCategory);
        candidateEntity.setResumeContent(resumeContent);
        candidateEntity.setSkills(skills);
        candidateEntity.setPhoneNumber(phoneNumber);

        // Registering candidate with user entity and candidate entity
        candidateService.registerCandidate(userEntity, candidateEntity);

        // Authenticating the newly created user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Generating JWT token for the authenticated user
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // Returning JWT token as response
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
