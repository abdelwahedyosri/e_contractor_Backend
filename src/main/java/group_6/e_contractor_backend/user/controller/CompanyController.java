package group_6.e_contractor_backend.user.controller;

import group_6.e_contractor_backend.user.dto.JwtResponse;
import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.service.impl.CompanyService;
import group_6.e_contractor_backend.user.service.impl.RoleService;
import group_6.e_contractor_backend.user.util.JwtUtil;
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
@RequestMapping("/api/register/company")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class CompanyController {

    private final CompanyService companyService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping(consumes = {"multipart/form-data"}, produces = {"application/json"})
    public ResponseEntity<?> registerCompany(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("location") String location,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("website") String website,
            @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogo) {

        // Fetching the role entity for 'company'
        Optional<RoleEntity> roleEntityOptional = roleService.findByRole("company");
        if (!roleEntityOptional.isPresent()) {
            throw new RuntimeException("Role 'company' not found");
        }
        RoleEntity roleEntity = roleEntityOptional.get();

        UserEntity userEntity = new UserEntity();
        userEntity.setRole(roleEntity);
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        userEntity.setPassword(password);

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setUser(userEntity);
        companyEntity.setCompanyLogo(companyLogo != null ? companyLogo.getOriginalFilename() : null);
        companyEntity.setLocation(location);
        companyEntity.setPhoneNumber(phoneNumber);
        companyEntity.setWebsite(website);

        companyService.registerCompany(userEntity, companyEntity, companyLogo);

        // Authenticate and generate token
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
