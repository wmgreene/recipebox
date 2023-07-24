package recipebox.com;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private Object id;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(userName);
        log.debug(userName);
        if (user == null) {
            log.warn("Invalid username or password {}", userName);
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserPrincipal(user);
    }

    @Override
    @Transactional
    public void create(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = modelMapper.map(userDTO, User.class);


        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }
    public User findUserByName(String name) {
        return userRepository.findUserByUserName(name);
    }
    @Override
    public List<Recipe> getRecipesByUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return user.getRecipes();
}




    public Optional<User> findById(Long id) {

        Optional<User> user = userRepository.findById(id);
        return user;
    }

}