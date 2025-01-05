//package com.cts.main.services;
//
//import java.util.List;
//
//import org.springframework.http.ResponseEntity;
//
//import com.cts.main.entities.User;
//
//public interface UserService {
//
//	User adduser(User user);
//
//	List<User> getuser();
//	ResponseEntity<String> updateuser(User user);
//
//}

package com.cts.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.cts.main.dtos.UserDTO;
import com.cts.main.entities.User;
import com.cts.main.responses.ApiResponse;

public interface UserService {
	ResponseEntity<?> adduser(UserDTO userDTO);

	List<User> getuser();

	ResponseEntity<ApiResponse<User>> updateuser(UserDTO userDTO);
}
