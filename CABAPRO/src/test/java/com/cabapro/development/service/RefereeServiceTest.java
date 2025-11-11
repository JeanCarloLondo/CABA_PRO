package com.cabapro.development.service;

import com.cabapro.development.model.Referee;
import com.cabapro.development.repository.RefereeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RefereeServiceTest {

    private final RefereeRepository refereeRepository = Mockito.mock(RefereeRepository.class);
    private final RefereeService refereeService = new RefereeService(refereeRepository);

    @Test
    void testSaveRefereeWithoutRankingThrowsException() {
        Referee referee = new Referee();
        referee.setEmail("ref@example.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> refereeService.save(referee)
        );

        assertEquals("Ranking must be selected", exception.getMessage());
    }
}