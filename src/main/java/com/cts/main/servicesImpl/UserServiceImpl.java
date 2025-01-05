package com.cts.main.servicesImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.main.dtos.UserDTO;
import com.cts.main.entities.User;
import com.cts.main.repository.UserRepository;
import com.cts.main.responses.ApiResponse;
import com.cts.main.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<?> adduser(UserDTO userDTO) {
		try {
			// Check if the username already exists
			if (userRepository.existsByUsername(userDTO.getUsername())) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(new ApiResponse<>("Username already exists! Try different.", null));
			}
			// Proceed with saving the new user if username is unique
			User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()),
					userDTO.getRoles());
			// save in DB
			User createdUser = userRepository.save(user);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse<>("User created successfully", createdUser));
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ApiResponse<>("Username already exists! Try different.", null));
		}
	}

	@Override
	public List<User> getuser() {
		return userRepository.findAll();
	}


	@Override
	public ResponseEntity<ApiResponse<User>> updateuser(UserDTO userDTO) {
		Optional<User> optionalUser = userRepository.findById(userDTO.getUser_id());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			
			String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
			
			boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
					.anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
			
			
			if ("ROLE_ADMIN".equals(userDTO.getRoles()) && !isAdmin) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
						new ApiResponse<>("You do not have permission to assign the role of 'ROLE_ADMIN'.", null));
			}
			if (!isAdmin && !authenticatedUsername.equals(user.getUsername())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(new ApiResponse<>("You do not have permission to update this user.", null));
			}
			if (!user.getUsername().equals(userDTO.getUsername())
					&& userRepository.existsByUsername(userDTO.getUsername())) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(new ApiResponse<>("Username already exists! Try different.", null));
			}
			user.setUsername(userDTO.getUsername());
			user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
			user.setRoles(userDTO.getRoles());
			userRepository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User updated successfully!", user));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("User not found!", null));
		}
	}

	

}
