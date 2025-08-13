package dz.irdcfb.dgdn.mf.gouv.service;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.irdcfb.dgdn.mf.gouv.entities.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	dz.irdcfb.dgdn.mf.gouv.repository.UserRepository userRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
		return UserDetailsImpl.build(user);
	}
}
