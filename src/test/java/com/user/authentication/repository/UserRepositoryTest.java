package com.user.authentication.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.user.authentication.entity.User;
import com.user.authentication.repository.UserRepository;

/**
*
* @author Rafael Tavares
*
*/
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	private static User dbUserRafael;
	
	private static User dbUserGabriel;
	
	@BeforeClass
	public static void setUp() throws Exception {
		dbUserRafael = User
				.builder()
				.id(1L)
				.firstName("Rafael")
				.lastName("Tavares")
				.email("rafaeltavares91@gmail.com")
				.encryptedPassword("rafa123")
				.userId("uuu1")
				.build();
		
		dbUserGabriel = User
				.builder()
				.id(2L)
				.firstName("Joao")
				.lastName("Gabriel")
				.email("joaogabriel92@gmail.com")
				.encryptedPassword("biel123")
				.userId("uuu2")
				.build();
	}
	
	@Test
	public void findUserByEmailTest() {
		User pessoaDB = userRepository.findByEmail("rafaeltavares91@gmail.com");
		assertEquals(dbUserRafael.getId(), pessoaDB.getId());
		assertEquals(dbUserRafael.getFirstName(), pessoaDB.getFirstName());
		assertEquals(dbUserRafael.getLastName(), pessoaDB.getLastName());
		assertEquals(dbUserRafael.getEmail(), pessoaDB.getEmail());
		
		assertNotEquals(dbUserGabriel.getId(), pessoaDB.getId());
	}
	
	@Test
	public void findUserByUserIdTest() {
		User pessoaDB = userRepository.findByUserId("uuu2");
		assertEquals(dbUserGabriel.getId(), pessoaDB.getId());
		assertEquals(dbUserGabriel.getFirstName(), pessoaDB.getFirstName());
		assertEquals(dbUserGabriel.getLastName(), pessoaDB.getLastName());
		assertEquals(dbUserGabriel.getEmail(), pessoaDB.getEmail());
		
		assertNotEquals(dbUserRafael.getId(), pessoaDB.getId());
	}
	
}
