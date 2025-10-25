package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.DTO.AddProductToCart;
import com.farmplace.digitalmarket.DTO.AddProductToCartResponse;
import com.farmplace.digitalmarket.DTO.CreateAccountDto;
import com.farmplace.digitalmarket.DTO.CustomerRegister;
import com.farmplace.digitalmarket.Model.*;
import com.farmplace.digitalmarket.enums.Roles;
import com.farmplace.digitalmarket.exceptions.FailedToFetchCustomerCart;
import com.farmplace.digitalmarket.exceptions.InsufficientQuantity;
import com.farmplace.digitalmarket.exceptions.ProductDoesntExistException;
import com.farmplace.digitalmarket.repository.*;
import com.farmplace.digitalmarket.service.serviceInterface.CustomerService;
import com.farmplace.digitalmarket.utils.LoggedInCustomer;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private Cart cart;
    private final UserRepository userRepository;


    public CustomerServiceImpl(ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, CustomerRepository customerRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    //To be implemented later
    static double discount(){
        return 0.0000D;
    }

    @Override
    @Transactional
    public AddProductToCartResponse addProductToCart(AddProductToCart productToCart) {

        //Verify the product selected is currently present in the database
        Product product=productRepository.findById(productToCart.getProductId())
                .orElseThrow(()->new ProductDoesntExistException("The selected product does not currently exist in the inventory"));

        if(productToCart.getQuantity()> product.getCurrentStock()){

           throw new InsufficientQuantity("The selected quantity is not available");
        }

        //Get the customer's cart
         cart= cartRepository.findByCustomer_phoneNumber(LoggedInCustomer.getUsername())
                .orElseThrow(() -> new FailedToFetchCustomerCart("Internal server error, please contact admin"));




            CartItem cartItem = getCartItem(productToCart,product);

            cartItemRepository.save(cartItem);

            product.setCurrentStock(product.getCurrentStock()- productToCart.getQuantity());
            productRepository.save(product);

        cartRepository.save(cart);

return AddProductToCartResponse.builder().productName(product.getProductName()).quantity(productToCart.getQuantity()).build();


    }

    @Override
    public CreateAccountDto createAccount(CustomerRegister customerRegister) {

        User user=new User();
        user.setRole(Roles.CUSTOMER);
        userRepository.save(user);

        Customer customer=modelMapper.map(customerRegister, Customer.class);
        customer.setCreatedAt(LocalDateTime.now());

        Cart cart1=new Cart();
        cart1.setCustomer(customer);
        cart1.setCreatedAt(LocalDateTime.now());
        customerRepository.save(customer);
        cartRepository.save(cart1);


        return CreateAccountDto.builder()
                .emailAddress(customerRegister.getEmailAddress())
                .phoneNumber(customer.getPhoneNumber())
                .firstName(customerRegister.getFirstName()).build();
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
            cartItem.setCart(cart);
            cartItem.setDiscountAllowed(discount());
            cartItem.setTotalPrice(productToCart.getQuantity()* product.getUnitPrice());
            cartItem.setQuantity(productToCart.getQuantity());
            cart.getCartItems().add(cartItem);

        }
        return cartItem;
    }


}
