package com.example.demo;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.repository.RankingRepository;
import com.cabapro.development.repository.RefereeRepository;
import com.cabapro.development.service.RankingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
/*
 * /
 * 
 * import static org.junit.jupiter.api.Assertions.*;
 * 
 * @Disabled("Test aislado temporalmente")
 * 
 * @ExtendWith(MockitoExtension.class)
 * class RankingServiceTest {
 * 
 * @Mock
 * private RankingRepository rankingRepository;
 * 
 * @Mock
 * private RefereeRepository refereeRepository;
 * 
 * @InjectMocks
 * private RankingService rankingService;
 * 
 * @Test
 * void testFindByIdNotFoundThrowsException() {
 * Long id = 99L;
 * Mockito.when(rankingRepository.findById(id)).thenReturn(Optional.empty());
 * 
 * IllegalArgumentException exception = assertThrows(
 * IllegalArgumentException.class,
 * () -> rankingService.findById(id)
 * );
 * 
 * assertTrue(exception.getMessage().contains("Ranking not found"));
 * }
 * /
 **/
