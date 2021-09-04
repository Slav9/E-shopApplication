package com.example.ShopCart.Service;

import com.example.ShopCart.models.Role;
import com.example.ShopCart.models.Users;
import com.example.ShopCart.repo.UsersPerository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UsersPerository usersPerository;
    @Autowired
    MailSender mailSender;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersPerository.findByUsername(username);
    }

    public boolean addUser(Users user){

        Users userFromDb = usersPerository.findByUsername(user.getUsername());

        if(userFromDb !=null){
            return false;
        }

        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        usersPerository.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(Users user) {
        if(!ObjectUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n"+
                            "Welcome to my Shopcart. Please, activate your account by this link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code",message);
        }
    }

    public boolean activateUser(String code) {
        Users users = usersPerository.findByActivationCode(code);

        if(users == null){
            return false;
        }

        users.setActivationCode(null);
        users.setActive(true);
        usersPerository.save(users);

        return true;
    }

    public List<Users> findAll() {
        return usersPerository.findAll();
    }

    public void saveUser(Users users, String username, Map<String, String> form) {
        users.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        users.getRoles().clear();

        for(String key: form.keySet()){
            if(roles.contains(key)){
                users.getRoles().add(Role.valueOf(key));
            }
        }

        usersPerository.save(users);
    }


    public void editProfile(Users users, String password, String email) {

        String userEmail = users.getEmail();

        boolean isEmailChanged = (email!=null&& !email.equals(userEmail)) ||
                (userEmail!=null && !userEmail.equals(email));

        if (isEmailChanged){
           users.setEmail(email);

           if(!ObjectUtils.isEmpty(email)){
               users.setActivationCode(UUID.randomUUID().toString());
           }
        }

        if(!ObjectUtils.isEmpty(password)){
            users.setPassword(password);
        }

        usersPerository.save(users);

        if(isEmailChanged) {
            sendMessage(users);
        }

    }
}
