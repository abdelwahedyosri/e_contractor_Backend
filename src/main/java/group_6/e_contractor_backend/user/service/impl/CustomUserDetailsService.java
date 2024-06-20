package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.get().getRole().getRole())));
    }

    public void updateLastLogin(String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }
}
