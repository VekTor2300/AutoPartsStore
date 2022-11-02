package com.infir.autopartstore.Controllers;

import com.infir.autopartstore.Models.Client;
import com.infir.autopartstore.Models.User;
import com.infir.autopartstore.Repos.ClientRepos;
import com.infir.autopartstore.Repos.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/userClient")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ClientController {
    @Autowired
    private ClientRepos clientRepository;

    @Autowired
    private UserRepos userRepository;

    @GetMapping()
    public String clientList(Model model){
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("users", userRepository.findActive());
        return "Client/main";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String clientAdd(Client client, Model model){
        List<User> users =  userRepository.findActive();
        model.addAttribute("users", userRepository.findActive());
        return "Client/add";
    }

    @PostMapping("/add")
    public String clientAdd(@ModelAttribute("client") @Valid Client client,
                              BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("users", userRepository.findActive());
            return "Client/add";
        }
        clientRepository.save(client);
        return "redirect:";
    }

    @GetMapping("/edit/{client}")
    @PreAuthorize("isAuthenticated()")
    public String clientEdit(Client client, Model model) {
        model.addAttribute("users", userRepository.findActive());
        return "Client/edit";
    }

    @PostMapping("/edit/{client}")
    @PreAuthorize("isAuthenticated()")
    public String clientPostEdit(
            @ModelAttribute("client") @Valid Client client,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userRepository.findActive());
            return "Client/edit";
        }
        clientRepository.save(client);
        return "redirect:../";
    }

    @GetMapping("/show/{client}")
    public String clientShow(
            Client client) {
        return "Client/detail";
    }

    @GetMapping("/del/{client}")
    @PreAuthorize("isAuthenticated()")
    public String clientDel(
            Client client) {
        clientRepository.delete(client);
        return "redirect:../";
    }
}
