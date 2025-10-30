package edu.dosw.sirha.sirha_backend;

package com.example.demo.service;

import com.example.demo.model.AcademicPeriod;
import com.example.demo.repository.AcademicPeriodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicPeriodServiceTest {

    @Mock
    private AcademicPeriodRepository academicPeriodRepository;

    @InjectMocks
    private AcademicPeriodService academicPeriodService;

    @Test
    void testFindAll() {
        when(academicPeriodRepository.findAll()).thenReturn(List.of(new AcademicPeriod()));
        assertFalse(academicPeriodService.findAll().isEmpty());
        verify(academicPeriodRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        AcademicPeriod period = new AcademicPeriod();
        when(academicPeriodRepository.findById(1L)).thenReturn(Optional.of(period));
        assertNotNull(academicPeriodService.findById(1L));

    }

    @Test
    void testSave() {
        AcademicPeriod period = new AcademicPeriod();
        when(academicPeriodRepository.save(period)).thenReturn(period);
        assertEquals(period, academicPeriodService.save(period));
    }

    @Test
    void testDelete() {
        doNothing().when(academicPeriodRepository).deleteById(1L);
        academicPeriodService.delete(1L);
        verify(academicPeriodRepository).deleteById(1L);
    }
}

