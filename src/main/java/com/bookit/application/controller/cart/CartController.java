package com.bookit.application.controller.cart;

import com.bookit.application.dto.cart.ItemDto;
import com.bookit.application.dto.cart.ItemDtoMapper;
import com.bookit.application.entity.Item;
import com.bookit.application.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    private CartService cartService;
    private ItemDtoMapper itemDtoMapper;

    public CartController(CartService cartService, ItemDtoMapper itemDtoMapper) {
        this.cartService = cartService;
        this.itemDtoMapper = itemDtoMapper;
    }

    @GetMapping("/cart")
    public ResponseEntity<List<ItemDto>> getCart(){
        List<Item> items = this.cartService.getCart();
        return new ResponseEntity<>(this.itemDtoMapper.toCartItemsDTO(items),HttpStatus.OK);
    }

    @PatchMapping("/cart/add")
    public ResponseEntity<ItemDto> addItem(@RequestParam String ticketId){
        Item item = this.cartService.addItem(ticketId);
        return new ResponseEntity<>(this.itemDtoMapper.toItemDto(item), HttpStatus.CREATED);
    }

    @PatchMapping("/cart/remove")
    public ResponseEntity<String> remove(@RequestParam Long itemId){
        this.cartService.removeItem(itemId);
        return new ResponseEntity<>("Item removed successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/cart/checkout")
    public ResponseEntity<List<ItemDto>> checkout(){
        List<Item> items = this.cartService.checkout();
        return new ResponseEntity<>(this.itemDtoMapper.toCartItemsDTO(items),HttpStatus.OK);
    }
}
