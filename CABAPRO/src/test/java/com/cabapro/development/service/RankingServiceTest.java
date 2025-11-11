package com.cabapro.development.service;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.repository.RankingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RankingServiceTest {

    private final RankingRepository rankingRepository = Mockito.mock(RankingRepository.class);
    private final RankingService rankingService = new RankingService(rankingRepository);

    @Test
    void testFindByIdNotFoundThrowsException() {
        Long id = 99L;
        Mockito.when(rankingRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rankingService.findById(id)
        );

        assertTrue(exception.getMessage().contains("Ranking not found"));
    }
}