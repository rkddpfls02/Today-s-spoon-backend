package com.example.todaySpoon.Service;


import com.example.todaySpoon.entity.User;
import com.example.todaySpoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        return userRepository.findById(userid)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }
    private UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId())
                //signup 완성시 빼기
                .password(user.getPassword())
                .build();
    }
}
