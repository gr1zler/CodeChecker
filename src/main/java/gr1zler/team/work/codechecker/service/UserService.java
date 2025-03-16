package gr1zler.team.work.codechecker.service;

import gr1zler.team.work.codechecker.model.User;
import gr1zler.team.work.codechecker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

}
