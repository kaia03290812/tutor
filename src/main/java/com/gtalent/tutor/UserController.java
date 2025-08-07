package com.gtalent.tutor;

import com.gtalent.tutor.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gtalent.tutor.requests.CreateUserRequest;
import com.gtalent.tutor.requests.UpdateUserRequest;
import com.gtalent.tutor.responses.CreateUserResponse;
import com.gtalent.tutor.responses.GetUserResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> mockUser = new HashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger();

    // 建構子:每當User
    public UserController() {
        //    假的資料庫
        mockUser.put(1, new User(1, "admin", "admin@gmail.com"));
        mockUser.put(2, new User(2, "user1", "user1@gmail.com"));
        mockUser.put(3, new User(3, "user2", "user2@gmail.com"));
        atomicInteger.set(4);
    }

    @GetMapping
    public List<GetUserResponse> getAllUser() {
        List<User> userList = new ArrayList<>(mockUser.values());
        List<GetUserResponse> UserResponses = new ArrayList<>();
        for (User user : userList) {
            GetUserResponse response = new GetUserResponse(user.getId(), user.getUsername());
            UserResponses.add(response);
        }
        return UserResponses;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserByid(@PathVariable int id) {
        User user = mockUser.get(id);
        if (user == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        GetUserResponse responses = new GetUserResponse(user.getId(), user.getUsername());
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateUserResponse> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        User user = mockUser.get(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        System.out.println(request.getUsername());
        user.setUsername(request.getUsername());
        CreateUserResponse response = new CreateUserResponse(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        int newId = atomicInteger.getAndIncrement();
        User user = new User(newId, request.getUsername(), request.getEmail());
        mockUser.put(newId, user);
        CreateUserResponse response = new CreateUserResponse(user.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable int id) {
        User user = mockUser.get(id);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        mockUser.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetUserResponse>> searchUser(@RequestParam String keyword) {
        List<GetUserResponse> results = mockUser.values()
                .stream()//lambda起手式
                .filter(user ->//過濾出來符合條件的users
                        user.getUsername().toLowerCase().contains(keyword.toLowerCase()))
                .map(GetUserResponse::new).toList();
        return ResponseEntity.ok(results);
    }
//    @GetMapping("/normal")
//    public  ResponseEntity<List<GetUserResponse>> getNormaluser(){
//        List<GetUserResponse> results = mockUser.values()
//                .stream()//lambda起手式
//                .filter(user ->//過濾出來符合條件的users
//                        !user.getUsername().toLowerCase().contains(toLowerCase()))
//                .map(GetUserResponse::new).toList();
//        return ResponseEntity.ok(results);
//    }
    private GetUserResponse toGetUserResponse(User user) {
        return new GetUserResponse(user.getId(), user.getUsername());
    }


}