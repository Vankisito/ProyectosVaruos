package com.NewCodeTeam.Comercializadora.repository;

import com.NewCodeTeam.Comercializadora.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    @Query(value="select * from employee where email=?1", nativeQuery = true)
    public abstract Employee findByEmail(String email);
}
