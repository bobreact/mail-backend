package dz.irdcfb.dgdn.mf.gouv.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.irdcfb.dgdn.mf.gouv.entities.ERole;
import dz.irdcfb.dgdn.mf.gouv.entities.RefreshToken;
import dz.irdcfb.dgdn.mf.gouv.entities.Role;
import dz.irdcfb.dgdn.mf.gouv.entities.Structure;
import dz.irdcfb.dgdn.mf.gouv.entities.User;
import dz.irdcfb.dgdn.mf.gouv.exceptions.TokenRefreshException;
import dz.irdcfb.dgdn.mf.gouv.repository.RoleRepository;
import dz.irdcfb.dgdn.mf.gouv.repository.StructureRepository;
import dz.irdcfb.dgdn.mf.gouv.repository.UserRepository;
import dz.irdcfb.dgdn.mf.gouv.security.payload.request.LogOutRequest;
import dz.irdcfb.dgdn.mf.gouv.security.payload.request.LoginRequest;
import dz.irdcfb.dgdn.mf.gouv.security.payload.request.SignupRequest;
import dz.irdcfb.dgdn.mf.gouv.security.payload.request.TokenRefreshRequest;
import dz.irdcfb.dgdn.mf.gouv.security.payload.response.JwtResponse;
import dz.irdcfb.dgdn.mf.gouv.security.payload.response.MessageResponse;
import dz.irdcfb.dgdn.mf.gouv.security.payload.response.TokenRefreshResponse;
import dz.irdcfb.dgdn.mf.gouv.security.security.jwt.JwtUtils;
import dz.irdcfb.dgdn.mf.gouv.service.RefreshTokenService;
import dz.irdcfb.dgdn.mf.gouv.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
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
	StructureRepository structureRepository;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@GetMapping("/structures")
	public ResponseEntity<List<Structure>> getAllStructure() {
		return ResponseEntity.ok(structureRepository.findAll());
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String token = jwtUtils.generateJwtToken(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), userDetails.getId(),
				userDetails.getUsername(), userDetails.getEmail(), roles, user.getStructure().getId()));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getStructure());

		Set<String> strRoles = signUpRequest.getRole();
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
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		user.setStructure(structureRepository.getById(signUpRequest.getStructure()));
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String token = jwtUtils.generateTokenFromUsername(user.getUsername());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@RequestBody LogOutRequest logOutRequest) {
		refreshTokenService.deleteByUserId(logOutRequest.getUserId());
		return ResponseEntity.ok(new MessageResponse("Log out successful!"));
	}

}