package com.breno_barbosa1.sistema_vendas.common;

import com.breno_barbosa1.sistema_vendas.dto.EmployeeDTO;
import com.breno_barbosa1.sistema_vendas.dto.EmployeeUpdateDTO;
import com.breno_barbosa1.sistema_vendas.entity.Employee;

import static com.breno_barbosa1.sistema_vendas.entity.UserRole.ROLE_EMPLOYEE;

public class EmployeeConstants {

    public static Employee getValidEmployee() {
        return new Employee("Maria", "Eduarda", "maria@mail.com", "admin123", "52998224725", "Campina Grande - Brasil",  ROLE_EMPLOYEE);
    }

    public static Employee getValidEmployeeUpdate() {
        return new Employee("Maria", "Eduarda", "mariaeduarda@mail.com", "admin123", "52998224725", "Campina Grande - Brasil", ROLE_EMPLOYEE);
    }

    public static EmployeeDTO getValidEmployeeDTO() {
        return new EmployeeDTO("Maria", "Eduarda", "maria@mail.com", "admin123", "52998224725", "Campina Grande - Brasil");
    }

    public static EmployeeUpdateDTO getValidEmployeeDTOUpdate() {
        return new EmployeeUpdateDTO("Maria", "Eduarda", "mariaeduarda@mail.com", "admin123", "João Pessoa - Brasil");
    }
}