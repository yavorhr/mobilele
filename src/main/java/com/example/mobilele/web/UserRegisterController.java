package com.example.mobilele.web;

import com.example.mobilele.model.dto.binding.user.UserRegisterBindingModel;
import com.example.mobilele.model.dto.service.user.UserRegisterServiceModel;
import com.example.mobilele.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserRegisterController {
  private final UserService userService;
  private final ModelMapper modelMapper;

  public UserRegisterController(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @ModelAttribute("userModel")
  public UserRegisterBindingModel userModel() {
    return new UserRegisterBindingModel();
  }

  @GetMapping("/users/register")
  public String registerPage() {
    return "auth-register";
  }

  @PostMapping("/users/register")
  public String register(
          @Valid UserRegisterBindingModel userModel,
          BindingResult bindingResult,
          RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors() || !userModel.getPassword().equals(userModel.getConfirmPassword())) {
      redirectAttributes.addFlashAttribute("userModel", userModel);
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userModel", bindingResult);

      return "redirect:/users/register";
    }

    UserRegisterServiceModel serviceModel =
            modelMapper.map(userModel, UserRegisterServiceModel.class);

    userService.registerAndLoginUser(serviceModel);

    return "redirect:/";
  }
}
