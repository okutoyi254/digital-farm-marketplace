package com.farmplace.digitalmarket.security.securityservice;

import com.farmplace.digitalmarket.Model.User;
import com.farmplace.digitalmarket.repository.UserRepository;
import com.farmplace.digitalmarket.security.securitymodel.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username).orElseThrow(
                ()->new UsernameNotFoundException("Invalid username or password"));

        return new UserPrincipal(user);
    }
}
