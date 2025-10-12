package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.DTO.AddProductToCart;
import com.farmplace.digitalmarket.Model.Cart;
import com.farmplace.digitalmarket.Model.CartItem;
import com.farmplace.digitalmarket.Model.Product;
import com.farmplace.digitalmarket.exceptions.FailedToFetchCustomerCart;
import com.farmplace.digitalmarket.exceptions.ProductDoesntExistException;
import com.farmplace.digitalmarket.repository.CartItemRepository;
import com.farmplace.digitalmarket.repository.CartRepository;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.serviceInterface.CustomerService;
import com.farmplace.digitalmarket.utils.LoggedInCustomer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private Cart cart;


    public CustomerServiceImpl(ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    //To be implemented later
    static double discount(){
        return 0.0000D;
    }

    @Override
    @Transactional
    public CartItem addProductToCart(AddProductToCart productToCart) {

        //Verify the product selected is currently present in the database
        Product product=productRepository.findById(productToCart.getProductId())
                .orElseThrow(()->new ProductDoesntExistException("The selected product does not currently exist in the inventory"));

        //Get the customer's cart
         cart= cartRepository.findByCustomer_phoneNumber(LoggedInCustomer.getUsername())
                .orElseThrow(() -> new FailedToFetchCustomerCart("Internal server error, please contact admin"));




            CartItem cartItem = getCartItem(productToCart,product);

            cartItemRepository.save(cartItem);

        cartRepository.save(cart);



    return cartItem;
    }

    private CartItem getCartItem(AddProductToCart productToCart, Product product) {

        //Check if the selected product is present in the cart,then update its attributes
        Optional<CartItem>existingItem= cart.getCartItems().stream().filter(item->
                item.getProduct().getProductId().equals(product.getProductId())).findFirst();
        CartItem cartItem;
        if(existingItem.isPresent()){

            cartItem= existingItem.get();
            cartItem.setQuantity(productToCart.getQuantity());
            cartItem.setTotalPrice(productToCart.getQuantity()* product.getUnitPrice());
            cartItem.setDiscountAllowed(discount());
        }
        else {
            //The product is not present in the cart yet,add the product to the cart
            cartItem= new CartItem();
            cartItem.setUnitPrice(product.getUnitPrice());
            cartItem.setProduct(product);
            cartItem.setDiscountAllowed(discount());
            cartItem.setTotalPrice(productToCart.getQuantity()* product.getUnitPrice());
            cartItem.setQuantity(productToCart.getQuantity());
            cart.getCartItems().add(cartItem);

        }
        return cartItem;
    }


}
