package com.narthil.rvcs.security;

import com.narthil.rvcs.dao.user.UserRepository;
import com.narthil.rvcs.pojo.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    // 提供从用户名可以查到用户并返回的方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);

        // System.out.println(user);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("未找到这个用户 '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}
