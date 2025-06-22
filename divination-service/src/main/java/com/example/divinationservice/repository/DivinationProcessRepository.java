package com.example.divinationservice.repository;

import com.example.divinationservice.model.DivinationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivinationProcessRepository extends JpaRepository<DivinationProcess, Long> {}

