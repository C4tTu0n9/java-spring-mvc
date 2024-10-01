package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.service.UserService;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        // List<User> arrUsers = this.userService.getAllUser();
        // System.out.println(arrUsers);
        model.addAttribute("eric", "test");
        model.addAttribute("catuong", "Cat Tuong dep trai");
        return "hello";
    }

    @RequestMapping("/admin/user/create") // method GET
    public String getCreatePage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST) // method GET
    public String getCreatePage(Model model, @ModelAttribute("newUser") User cattuong) {
        System.out.println("run here" + cattuong);
        this.userService.handleSaveUser(cattuong);
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/admin/user")
    public String getUserPage(Model model) {
        List<User> arrUsers = this.userService.getAllUser();
        System.out.println(arrUsers);
        model.addAttribute("userList", arrUsers);
        return "admin/user/table-user";
    }

    @RequestMapping(value = "/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        System.out.println("check path: " + id);
        model.addAttribute("id", id);
        return "admin/user/show";
    }
}

// @RestController
// public class UserController {

// //DI: dependency injection
// private UserService userService;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping("/")
// public String getHomePage() {
// return this.userService.handleHello();
// }
// }
