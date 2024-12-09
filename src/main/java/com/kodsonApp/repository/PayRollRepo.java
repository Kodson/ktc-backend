package com.kodsonApp.repository;

import com.kodsonApp.domain.PayRoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PayRollRepo extends JpaRepository<PayRoll, String> {

    @Query(value = "SELECT * FROM PayRoll ORDER BY id DESC LIMIT 1000", nativeQuery = true)
    List<PayRoll> findLast1000Records();

    @Query("SELECT p FROM PayRoll p WHERE p.department = ?1 AND p.month LIKE %?2%")
    List<PayRoll> findByDepartmentAndMonth(String department, String month);

    @Query("SELECT p FROM PayRoll p WHERE p.month =?1")
    List<PayRoll> findByMonth(String month);

    @Query("SELECT p FROM PayRoll p WHERE p.companyName = ?1 AND p.month LIKE %?2%")
    List<PayRoll> findByCompanyAndMonth(String companyName, String month);

    @Query("SELECT p FROM PayRoll p WHERE p.employeeId = ?1")
    List<PayRoll> findByEmployeeId(String employeeId);

    /*
    @Query("SELECT p FROM PayRoll p WHERE p.employeeId = ?1 ORDER BY p.payrollDate DESC")
    Optional<PayRoll> findLatestPayrollByEmployeeId(String employeeId);*/
    @Query("SELECT p FROM PayRoll p WHERE p.employeeId = ?1 ORDER BY p.payrollDate DESC")
    List<PayRoll> findPayrollsByEmployeeId(String employeeId);



}
