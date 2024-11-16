package com.joe.lib.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

//    private final EmployeeService employeeService;
//    private final UserService userService;
//    private RoleType roleType;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

//        if (roleType == RoleType.ROLE_USER) {
//            log.info("====>>>> RoleType.ROLE_USER");
//
//            com.proKeys.prokeys111.application.auth.user.entity.User userEntity = userService.findByEmail(email);
//
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userEntity.getRole().getRoleType().value);
//            Collection<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(authority);
//
//            return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
//
//        } else {
//            log.info("====>>>> RoleType.EMPLOYEE");
//            Employee employeeEntity = employeeService.findByEmail(email);
//
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(employeeEntity.getRole().getRoleType().value);
//            Collection<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(authority);
//
//            return new User(employeeEntity.getEmail(), employeeEntity.getPassword(), authorities);
//        }
        return null;
    }
}
