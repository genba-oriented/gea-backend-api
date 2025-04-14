package com.example.fleamarket.api.user.service;

import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.input.UserInput;
import com.example.fleamarket.api.user.repository.UserRepository;
import com.example.fleamarket.api.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final IdGenerator idGenerator;

    public User getById(String id) {
        return this.userRepository.getById(id);
    }

    public User getByIdpUserId(String idpUserId) {
        return this.userRepository.getByIdpUserId(idpUserId);
    }

    public User registerNotActivated(String idpUserId, String email) {
        User user = new User();
        user.setId(idGenerator.generateId());
        user.setIdpUserId(idpUserId);
        user.setEmail(email);
        this.userRepository.save(user);
        return user;
    }

    public void activate(String userId, UserInput userInput) {
        User user = this.getById(userId);
        user.setName(userInput.getName());
        user.setBalance(userInput.getDeposit());
        user.setShippingAddress(userInput.getShippingAddress());
        user.setActivated(true);
        this.userRepository.update(user);
    }
}
