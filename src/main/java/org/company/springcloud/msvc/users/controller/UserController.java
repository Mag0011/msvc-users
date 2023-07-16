package org.company.springcloud.msvc.users.controller;

import org.company.springcloud.msvc.users.entity.User;
import org.company.springcloud.msvc.users.entity.request.ListUsersByCourseRequest;
import org.company.springcloud.msvc.users.service.UserService;
import org.company.springcloud.msvc.users.utils.RequestValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestValidationService requestValidationService;

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") Long id){
        Optional<User> optionalUser = userService.findUserById(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/getUsersByCourse") //fixme: this should be @Getter... but only works by using @Post...
    public ResponseEntity<List<User>> getUsersByCourse(@RequestBody ListUsersByCourseRequest request){
        List<User> listUsers = userService.findAllById(request.getListIds());
        return ResponseEntity.status(HttpStatus.OK).body(listUsers);
    }

    @PostMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult validationResult){
        //TODO: Move this and similar validation to a common validation class
        if(!user.getEmail().isEmpty() && userService.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Message","User with mail " + user.getEmail() + " already exists"));
        }
        if(validationResult.hasErrors()){
            Map<String, String> mapErrors = requestValidationService.validateRequest(validationResult.getFieldErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapErrors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/editUser/{id}")
    public ResponseEntity<?> editUser(@Valid @RequestBody User userRequest , @PathVariable Long id, BindingResult validationResult){
        if(validationResult.hasErrors()){
            Map<String, String> mapErrors = requestValidationService.validateRequest(validationResult.getFieldErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapErrors);
        }

        Optional<User> optionalUserDb = userService.findUserById(id);
        if(optionalUserDb.isPresent()){
            User userUpdated = optionalUserDb.get();
            //TODO: Move this and similar validation to a common validation class
            if(!userRequest.getEmail().equalsIgnoreCase(userUpdated.getEmail()) && userService.findByEmail(userRequest.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Message","User with mail " + userRequest.getEmail() + " already exists"));
            }
            userUpdated.setEmail(userRequest.getEmail());
            userUpdated.setName(userRequest.getName());
            userUpdated.setPassword(userRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(userService.save(userUpdated));
        }
        return ResponseEntity.notFound().build();
     }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        Optional<User> userDB = userService.findUserById(id);
        if(userDB.isPresent()) {
            userService.delete(userDB.get());
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }

}
