package recipebox.com;

import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
@Controller
@Slf4j
public class UserController {
    @PersistenceContext
    private EntityManager entityManager;
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

@Autowired
    private UserServiceImpl userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;


    @Autowired
    public UserController(UserServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/")
    private String redirectToLogin(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null || !referer.contains("/login")) {
            return "redirect:/login";
        }
        return "home";
    }


    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("userDto", new UserDTO());
        return "sign-up";
    }

    @PostMapping("/signup-process")
    public String signupProcess(@Valid @ModelAttribute("userDto") UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Wrong attempt");
            return "sign-up";
        }
        userDetailsService.create(userDTO);
        return "confirmation";
    }


    @RequestMapping("/home")
    public String getHome() {
        log.info("home page displayed");
        return "home";
    }
    @GetMapping("/login")
    public String login() {

        log.info("Login page displayed");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Is authenticated: " + authentication.isAuthenticated());
        log.info("Principal: " + authentication.getPrincipal());
        log.info("Authorities: " + authentication.getAuthorities());
        return "login";
    }
    @PostMapping("/login")
    public String loginPost(@PathVariable Long id, HttpServletRequest request) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                request.login(request.getParameter("email"), request.getParameter("password"));
            } catch (ServletException e) {
                log.error("Authentication failed: " + e.getMessage());

                return "redirect:/login?error";
            }


            return "redirect:/recipe-upload/" + id;
        } else {

            return "redirect:/login?error=user_not_found";
        }
    }
    @GetMapping("/recipe-upload")
    public String showUploadRecipeForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findUserByEmail(email);


            model.addAttribute("user", user);


            Recipe recipe = new Recipe();
            model.addAttribute("recipe", recipe);

            return "/recipe-upload";
        } else {

            return "redirect:/login";
        }
    }
    @GetMapping("/your-mapping")
    public String yourMapping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();


            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();



            return "your-view";
        }


        return "redirect:/login";
    }

    @PostMapping("/uploadRecipe")
    public String uploadRecipe(
            @Validated @ModelAttribute("recipeDto") Recipe recipe, BindingResult bindingResult,
            @RequestParam("imgFile") MultipartFile imgFile) {


        if (bindingResult.hasErrors()) {
            log.warn("Validation errors occurred:");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.warn(error.getDefaultMessage());
            }

            return "recipe-upload";
        }

        try {

            if (!imgFile.isEmpty()) {

                byte[] imgData = imgFile.getBytes();

                recipe.setImg(imgData);
            }


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                User user = userRepository.findUserByEmail(email);
                if (user != null) {

                    recipe.setUser(user);
                }
            }


            recipeService.save(recipe);

            return "upload-success";
        } catch (IOException e) {

            log.error("Error while handling file upload: " + e.getMessage());

            return "error";
        }

    }
    @GetMapping("/user-profile/{id}")
    public String viewUserProfile(@PathVariable Long id, Model model) {
        Optional<User> userId = userService.findById(id);
        if (userId.isPresent()) {
            List<Recipe> userRecipes = recipeService.getRecipesByUserId(userId.get().getId());
            model.addAttribute("userRecipes", userRecipes);


            List<Boolean> isJPEGList = new ArrayList<>();
            List<Boolean> isPNGList = new ArrayList<>();

            for (Recipe recipe : userRecipes) {
                byte[] imgData = recipe.getImg();
                boolean isJPEG = imgData != null && isJPEGImage(imgData);
                boolean isPNG = imgData != null && isPNGImage(imgData);
                isJPEGList.add(isJPEG);
                isPNGList.add(isPNG);
            }

            model.addAttribute("isJPEGList", isJPEGList);
            model.addAttribute("isPNGList", isPNGList);

            return "user-profile";
        } else {

            return "error-page";
        }
    }


    private boolean isJPEGImage(byte[] imgData) {
        return imgData.length >= 2 && imgData[0] == (byte) 0xFF && imgData[1] == (byte) 0xD8;
    }


    private boolean isPNGImage(byte[] imgData) {
        return imgData.length >= 8 &&
                imgData[0] == (byte) 0x89 &&
                imgData[1] == 'P' &&
                imgData[2] == 'N' &&
                imgData[3] == 'G' &&
                imgData[4] == (byte) 0x0D &&
                imgData[5] == (byte) 0x0A &&
                imgData[6] == (byte) 0x1A &&
                imgData[7] == (byte) 0x0A;
    }
}


