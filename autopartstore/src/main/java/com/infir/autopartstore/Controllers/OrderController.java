package com.infir.autopartstore.Controllers;

import com.infir.autopartstore.Models.*;
import com.infir.autopartstore.Repos.ClientRepos;
import com.infir.autopartstore.Repos.OrderRepos;
import com.infir.autopartstore.Repos.OrderStatusRepos;
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
@RequestMapping("/orderSotr")
public class OrderController {
    @Autowired
    private OrderStatusRepos orderStatusRepos;

    @Autowired
    private ClientRepos clientRepos;

    @Autowired
    private UserRepos userRepos;

    @Autowired
    private OrderRepos orderRepos;

    @GetMapping()
    public String orderList(Model model){
        model.addAttribute("orders", orderRepos.findAll());
        model.addAttribute("orderStatus", orderStatusRepos.findAll());
        model.addAttribute("client", clientRepos.findAll());
        model.addAttribute("user", userRepos.findAll());

        return "Order/main";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    public String orderAdd(Order order, Model model){
        List<Client> clients = clientRepos.findAll();
        model.addAttribute("client", clientRepos.findAll());
        Iterable<OrderStatus> orderStatuses = orderStatusRepos.findAll();
        model.addAttribute("orderStatus", orderStatusRepos.findAll());
        model.addAttribute("user", userRepos.findAll());
        return "Order/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    public String orderAdd(@ModelAttribute("order") @Valid Order order,
                            BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
//            model.addAttribute("orderStatuses", orderStatusRepos.findAll());
//            model.addAttribute("clientes", clientRepos.findAll());
            return "Order/add";
        }
        orderRepos.save(order);
        return "redirect:";
    }

    @GetMapping("/edit/{order}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    public String orderEdit(Order order, Model model) {
        List<Client> clients = clientRepos.findAll();
        model.addAttribute("client", clientRepos.findAll());
        Iterable<OrderStatus> orderStatuses = orderStatusRepos.findAll();
        model.addAttribute("orderStatus", orderStatusRepos.findAll());
        model.addAttribute("user", userRepos.findAll());

        return "Order/edit";
    }

    @PostMapping("/edit/{order}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    public String orderPostEdit(
            @ModelAttribute("order") @Valid Order order,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "Order/edit";
        }
        orderRepos.save(order);
        return "redirect:../";
    }

    @GetMapping("/detail/{order}")
    public String orderShow(Order order, Model model) {
        return "Order/detail";
    }

    @GetMapping("/del/{order}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    public String orderDel(
            Order order) {
        orderRepos.delete(order);
        return "redirect:../";
    }
}
