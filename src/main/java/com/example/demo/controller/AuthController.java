package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.DTO.*;
import com.example.demo.repository.OtpTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerifiedTokenRepository;
import com.example.demo.response.JwtResponse;
import com.example.demo.response.MessageResponse;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.EmailSenderService;
import com.example.demo.service.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(tags = "User Rest Controller")
public class AuthController {
	@Autowired
    AuthenticationManager authenticationManager;

	@Autowired
    UserRepository userRepository;

	@Autowired
    RoleRepository roleRepository;

	@Autowired
    PasswordEncoder encoder;

	@Autowired
    JwtUtils jwtUtils;

	@Autowired
	private OtpTokenRepository otpTokenRepository;

	@Autowired
	private VerifiedTokenRepository verifiedTokenRepository;

	@Autowired
	private EmailSenderService emailSenderService;

	@PostMapping("/signin")
	@ApiOperation(value = "Sign in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninDTO signinDTO) {
		User user = userRepository.checkEmail(signinDTO.getEmail());
		if(user == null){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Your email doesn't exist"));
		}


		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signinDTO.getEmail(), signinDTO.getPassword()));


		if(user.isEnabled() == false){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Your email hasn't verified!"));
		}else{
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(jwt,
					userDetails.getId(),
					userDetails.getUsername(),
					roles,
					userDetails.getEmail()));
		}
	}

	@PostMapping("/signinv2")
	@ApiOperation(value = "Sign in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ApiResDTO authenticateUserv2(@Valid @RequestBody SigninDTO signinDTO) {
		User user = userRepository.checkEmail(signinDTO.getEmail());


		if(user == null){
			return ApiResDTO.fail(null,"email doesn't exist");
		}

		boolean result = encoder.matches(signinDTO.getPassword(), user.getPassword());
		if(result == false){
			return ApiResDTO.fail(null, "password doesn't match");
		}else{

			String jwt = jwtUtils.generateJwtToken(user);
			List<String> roles = new ArrayList<>();
			user.getRoles().forEach(role -> {
				roles.add(role.getName().name());
			});
			return ApiResDTO.success(new UserLoginResDTO(jwt,roles.get(0).toString()), ApiResponseDTO.SUCCESSS_MESSAGE);
		}
	}



	@PostMapping("/signup")
	@ApiOperation(value = "Sign up user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDTO signupDTO) {
		if (userRepository.existsByEmail(signupDTO.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Email is already taken!"));
		}

		if (userRepository.existsByUsername(signupDTO.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Username is already taken!"));
		}

		// Create new user's account
		User user = new User(signupDTO.getEmail(),
				encoder.encode(signupDTO.getPassword()), signupDTO.getUsername(), signupDTO.getAvatar());

		Set<String> strRoles = signupDTO.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		VerifiedToken verifiedTokentoken = new VerifiedToken(user);

		verifiedTokenRepository.save(verifiedTokentoken);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Registration Confirmation");
		mailMessage.setFrom("EasyKanjiFPT@gmail.com");
		mailMessage.setText("This is your confirmation link : " + "http://104.155.187.140:8081/api/auth/registrationConfirm?token=" + verifiedTokentoken.getVerifiedToken());

		emailSenderService.sendEmail(mailMessage);

		return ResponseEntity.ok(new MessageResponse("User registered successfully. A verified link have been sent to your email!"));

	}

	@RequestMapping(value="/registrationConfirm", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {
		VerifiedToken verifiedToken = verifiedTokenRepository.findByVerifiedToken(token);

		Calendar cal = Calendar.getInstance();
		if ((verifiedToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			modelAndView.addObject("message","The link is invalid or has expired!");
			modelAndView.setViewName("error");
		}
		User user = userRepository.checkEmail(verifiedToken.getUser().getEmail());
		user.setEnabled(true);
		userRepository.save(user);
		modelAndView.setViewName("verifiedAccount");
		return modelAndView;
	}

	@GetMapping("/users")
	@ApiOperation(value = "Get all users")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ApiResDTO getAllUser(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
		page = page -1;
		if(page < 0) {
			page = 0;
		}
		if(size > 10) {
			size = 10;
		}
		Pageable pageable = PageRequest.of(page,size);
		Page<User> users = userRepository.findUserPaging(pageable);
		if (users.isEmpty()) {
			return ApiResDTO.fail(null, "Don't have users");
		}
		return ApiResDTO.success(users, "Get list user successfully!");
	}

	@GetMapping("/users/{id}")
	@ApiOperation(value = "Get user by ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ApiResDTO getUserById(@PathVariable(value = "id") Long userID) {

		User user = userRepository.findById(userID).orElse(null);
		if(user == null){
			return ApiResDTO.fail(null,"User doesn't exist: " + userID);
		}
		return ApiResDTO.success(user,"Get user by ID successfully!");
	}

	@PutMapping("/users/{id}")
	@ApiOperation(value = "Update user by ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ApiResDTO updateUser(@PathVariable(value = "id") Long userID,
                                @Valid @RequestBody User userDetails){
			User user = userRepository.findById(userID).orElse(null);

			if(user == null){
				return ApiResDTO.fail(null,"User doesn't exist: " + userID);
			}
			user.setEmail(userDetails.getEmail());
			user.setPassword(encoder.encode(userDetails.getPassword()));
			user.setUsername(userDetails.getUsername());
			user.setAvatar(userDetails.getAvatar());
			user.setEnabled(userDetails.isEnabled());
			user.setRoles(userDetails.getRoles());
			userRepository.save(user);

			return ApiResDTO.success(user,"Update user successfully!");
	}

	@DeleteMapping("/users/{id}")
	@ApiOperation(value = "Delete user by ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ApiResDTO deleteLesson(@PathVariable(value = "id") Long userID) {

		User user = userRepository.findById(userID).orElse(null);
		if(user == null){
			return ApiResDTO.success(null,"User doesn't exist: " + userID);
		}

		userRepository.delete(user);
		return ApiResDTO.success(null, "Delete user successfullly!");
	}

	@PostMapping("/users/change-password")
	@ApiOperation(value = "Change password of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordModelDTO changePasswordModelDTO){
		String currentPassword = changePasswordModelDTO.getCurrentPassword();
		String newPassword = changePasswordModelDTO.getNewPassword();

		User user = userRepository.checkEmail(changePasswordModelDTO.getEmail());

		if(user == null){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Incorrect email!"));
		}else if(user.isEnabled() == false) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Email isn't activate!"));
		}
		else{
			String currentEncryptedPassword = user.getPassword();

			if (!encoder.matches(currentPassword, currentEncryptedPassword)) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Current password doesn't match. Change password failed!"));
			}
			String encryptedPassword = encoder.encode(newPassword);
			user.setPassword(encryptedPassword);

			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse("Change password successfully!"));
		}

	}

	@PostMapping("/users/forgot-password")
	@ApiOperation(value = "Forgot password model check email exist")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO){

		User user = userRepository.checkEmail(forgotPasswordDTO.getEmail());

		if(user == null){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Email isn't registered"));
		}

		OtpToken otpToken = new OtpToken(user);

		otpTokenRepository.save(otpToken);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("OTP code =!");
		mailMessage.setFrom("EasyKanjiFPT@gmail.com");
		mailMessage.setText("This is your OTP code: " +otpToken.getOtpToken());

		emailSenderService.sendEmail(mailMessage);

		return ResponseEntity.ok(new MessageResponse("An OTP code have been sent to your email! "));
	}

	@PostMapping("/users/reset-password")
	@ApiOperation(value = "Reset password of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordModel){
		User user = userRepository.checkEmail(resetPasswordModel.getEmail());
		String otpCode = resetPasswordModel.getOtpCode();
		OtpToken token = otpTokenRepository.findByOtpToken(otpCode);
//		Optional user = us
//		User user = userRepository.checkUsername(resetPasswordModel.getUsername());

		if(token != null){
//			User user = new User();
			String newPassword = resetPasswordModel.getNewPassword();
			String encryptedPassword = encoder.encode(newPassword);
			user.setPassword(encryptedPassword);
			userRepository.save(user);
		}else {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("OTP code is wrong!"));
		}


		return ResponseEntity.ok(new MessageResponse("Your password has been reset successfully! "));
	}
}
