package com.example.managementservice.repository;

import com.example.managementservice.model.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    double sumPaymentsBetweenDates(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}
