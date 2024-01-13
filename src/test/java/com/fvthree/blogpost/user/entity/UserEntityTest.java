package com.fvthree.blogpost.user.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(User.class)
public class UserEntityTest {

	@Test
	void shouldMockUserClass() {
		User userMock = mock(User.class);
		
		when(userMock.id()).thenReturn(1L);
		
		assertThat(userMock.id(), equalTo(1L));
	}
}
