package com.PSVM.dopamin.controller.item;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/item")
public class ItemMainController {

    @GetMapping("/item")
    public String item_main(){
        return "Item/new_itemMain";
    }
    @GetMapping("/cart")
    public String cart_main() {return "Item/new_cart";}
}