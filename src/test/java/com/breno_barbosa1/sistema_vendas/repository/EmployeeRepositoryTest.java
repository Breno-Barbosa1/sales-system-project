package com.breno_barbosa1.sistema_vendas.repository;

import com.breno_barbosa1.sistema_vendas.entity.Employee;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static com.breno_barbosa1.sistema_vendas.common.EmployeeConstants.getValidEmployee;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void getEmployee_withValidId_ReturnsEmployee() {
        Employee savedEmployee = testEntityManager.persistFlushFind(getValidEmployee());

        testEntityManager.clear();

        Optional<Employee> sut = employeeRepository.findById(savedEmployee.getId());

        assertThat(sut).isPresent();
        assertThat(sut.get())
            .usingRecursiveComparison()
            .isEqualTo(savedEmployee);
    }

    @Test
    public void getEmployee_withNonExistingId_ReturnsEmpty() {
        Optional<Employee> sut = employeeRepository.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getEmployee_withValidCpf_ReturnsEmployee() {
        Employee savedEmployee = testEntityManager.persistFlushFind(getValidEmployee());

        testEntityManager.clear();

        Optional<Employee> sut = employeeRepository.findByCpf(savedEmployee.getCpf());

        assertThat(sut).isPresent();
        assertThat(sut.get())
            .usingRecursiveComparison()
            .isEqualTo(savedEmployee);
    }

    @Test
    public void getEmployee_withNonExistingCpf_ReturnsEmpty() {
        Optional<Employee> sut = employeeRepository.findByCpf("99999999999");

        assertThat(sut).isEmpty();
    }

    @Test
    public void getEmployee_withValidEmail_ReturnsEmployee() {
        Employee savedEmployee = testEntityManager.persistFlushFind(getValidEmployee());

        testEntityManager.clear();

        Optional<Employee> sut = employeeRepository.findByEmail(savedEmployee.getEmail());

        assertThat(sut).isPresent();
        assertThat(sut.get())
            .usingRecursiveComparison()
            .isEqualTo(savedEmployee);
    }

    @Test
    public void getEmployee_withNonExistingEmail_ReturnsEmpty() {
        Optional<Employee> sut = employeeRepository.findByEmail("john@mail.com");

        assertThat(sut).isEmpty();
    }

    @Test
    public void createEmployee_withValidData_ReturnsEmployee() {
        Employee employee = getValidEmployee();

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);

        testEntityManager.clear();

        Employee sut = testEntityManager.find(Employee.class, savedEmployee.getId());

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(savedEmployee);
    }

    @Test
    public void createEmployee_withInvalidData_ThrowsException() {
        Employee employee1 = getValidEmployee();
        employee1.setCpf("");
        Employee employee2 = getValidEmployee();
        employee2.setFirstName("");

        assertThatThrownBy(() -> employeeRepository.saveAndFlush(employee1)).isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> employeeRepository.saveAndFlush(employee2)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void updateEmployee_withValidData_ReturnsEmployee() {
        Employee employee = getValidEmployee();

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);

        testEntityManager.clear();

        Employee foundEmployee = testEntityManager.find(Employee.class, savedEmployee.getId());

        foundEmployee.setEmail("mariaeduarda@mail.com");

        employeeRepository.saveAndFlush(savedEmployee);

        testEntityManager.clear();

        Employee sut = testEntityManager.find(Employee.class, savedEmployee.getId());

        assertThat(sut).isNotNull();
        assertThat(sut).
            usingRecursiveComparison()
            .isEqualTo(foundEmployee);
    }

    @Test
    public void updateEmployee_withInvalidData_ThrowsException() {
        Employee employee = getValidEmployee();

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);

        testEntityManager.clear();

        Employee foundEmployee = testEntityManager.find(Employee.class, savedEmployee.getId());

        foundEmployee.setEmail("");

        assertThatThrownBy(() -> employeeRepository.saveAndFlush(foundEmployee)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void deleteEmployee_withExistingId_ReturnsEmpty() {
        Employee employee = getValidEmployee();

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);

        testEntityManager.clear();

        Employee foundEmployee = testEntityManager.find(Employee.class, savedEmployee.getId());

        employeeRepository.delete(foundEmployee);
        employeeRepository.flush();

        testEntityManager.clear();

        Employee emptyEmployee = testEntityManager.find(Employee.class, foundEmployee.getId());

        assertThat(emptyEmployee).isNull();
    }

    @Test
    public void deleteEmployee_withNonExistingId_DoesNotThrowException() {
        Employee employee = new Employee();
        employee.setId(999L);

        assertThatCode(() -> {
            employeeRepository.delete(employee);
            employeeRepository.flush();
        }).doesNotThrowAnyException();
    }
}
