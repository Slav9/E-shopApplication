package com.example.ShopCart.Service;

import com.example.ShopCart.models.Cart;
import com.example.ShopCart.models.Role;
import com.example.ShopCart.models.Users;
import com.example.ShopCart.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usersRepository.findByUsername(username);
    }

    public boolean addUser(Users user){

        Users userFromDb = usersRepository.findByUsername(user.getUsername());
        if(userFromDb !=null){
            return false;
        }

        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        sendMessage(user);
        return true;
    }
    // method for sending verification code to users
    private void sendMessage(Users user) {

        if(!ObjectUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n"+
                            "Welcome to my E-shop. Please, activate your account by this link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code",message);
        }
    }

    //when users clicks verification code he become active and can enter site
    public boolean activateUser(String code) {
        Users users = usersRepository.findByActivationCode(code);

        if(users == null){
            return false;
        }

        users.setActivationCode(null);
        users.setActive(true);
        usersRepository.save(users);
        return true;
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
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

        usersRepository.save(users);
    }


    public void editProfile(Users users, String password, String email) {

        String userEmail = users.getEmail();
        String userPassword = users.getPassword();

        boolean EmailChanged = (email!=null&& !email.equals(userEmail)) ||
                (userEmail!=null && !userEmail.equals(email));

        boolean PasswordChanged = (password!=null && !password.equals(userPassword)) ||
                (userPassword!=null&& !userPassword.equals(password));


        if (EmailChanged && !PasswordChanged){

           users.setEmail(email);

           if(!ObjectUtils.isEmpty(email)){

               users.setActivationCode(UUID.randomUUID().toString());
           }
        }

        if(PasswordChanged && !EmailChanged){

            if(!ObjectUtils.isEmpty(password)){

                users.setPassword(passwordEncoder.encode(password));
            }
        }

        if(PasswordChanged && EmailChanged){

            users.setEmail(email);

            if(!ObjectUtils.isEmpty(email)){

                users.setActivationCode(UUID.randomUUID().toString());
            }

            if(!ObjectUtils.isEmpty(password)){

                users.setPassword(passwordEncoder.encode(password));
            }
        }

        usersRepository.save(users);

        if(EmailChanged) {

            sendMessage(users);
        }

    }
     public int topUpBalance(Users users, int amount) {

        users.setBalance(users.getBalance()+amount);
        usersRepository.save(users);
        return users.getBalance();
    }

    public Users updateUserBalanceOnCheckout(Users users, Cart cart) {

        users.setBalance(users.getBalance()-cart.getTotalPrice());
        return usersRepository.save(users);

    }

}
