package com.gtalent.tutor;

import com.gtalent.tutor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import com.gtalent.tutor.requests.UpdateUserRequest;
import com.gtalent.tutor.responses.CreateUserResponse;
import com.gtalent.tutor.requests.CreateUserRequest;
import com.gtalent.tutor.responses.GetUserResponse;
import com.gtalent.tutor.responses.UpdateUserResponse;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin("*")
public class UserV1Controller {
    private final JdbcTemplate jdbcTemplate;
    @Autowired  //由建構子注入(可以做邏輯.比較彈性)
    public UserV1Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//  @Autowired  注入 (缺點比較沒彈性)
//private  JdbcTemplate jdbcTemplate;
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        String sql = "insert into users (username, email) values (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, request.getUsername(), request.getEmail());
        if (rowsAffected > 0) {
            CreateUserResponse response = new CreateUserResponse(request.getUsername());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping
    public  ResponseEntity <List<GetUserResponse>> getAllUsers(){
        String sql="SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(User.class));
        return  ResponseEntity.ok(users.stream().map(GetUserResponse::new).toList());
}
    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
            return ResponseEntity.ok(new GetUserResponse(user));
        } catch (Exception e) {
            // 查無資料時回傳 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        String sql = "update users set username=? where id=?";
        int rowsAffected = jdbcTemplate.update(sql, request.getUsername(), id);
        if (rowsAffected > 0 ) {
            UpdateUserResponse response = new UpdateUserResponse(request.getUsername());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id) {
        String sql = "delete from users where id=?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected > 0 ) {

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetUserResponse>> searchUser(@RequestParam String keyword) {
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        try {
            List<User> users = jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<>(User.class),
                    "%" + keyword + "%"
            );
            List<GetUserResponse> responseList = users.stream()
                    .map(GetUserResponse::new)
                    .toList();
            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}