package com.breno_barbosa1.sistema_vendas.service;

import com.breno_barbosa1.sistema_vendas.dto.EmployeeDTO;
import com.breno_barbosa1.sistema_vendas.dto.EmployeeUpdateDTO;
import com.breno_barbosa1.sistema_vendas.entity.Employee;
import com.breno_barbosa1.sistema_vendas.exception.EmployeeNotFoundException;
import com.breno_barbosa1.sistema_vendas.mapper.EmployeeMapper;
import com.breno_barbosa1.sistema_vendas.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.breno_barbosa1.sistema_vendas.common.EmployeeConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void getEmployee_withExistingId_ReturnsEmployee() {
        Employee employee = getValidEmployee();
        EmployeeDTO employeeDTO = getValidEmployeeDTO();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO sut = employeeService.findById(1L);

        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(employeeDTO);

        verify(employeeRepository).findById(1L);
        verify(employeeMapper).employeeToEmployeeDTO(employee);
    }

    @Test
    public void getEmployee_withNonExistingId_ThrowsException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findById(1L)).isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    public void getEmployee_withExistingCpf_ReturnsEmployee() {
        Employee employee = getValidEmployee();
        EmployeeDTO employeeDTO = getValidEmployeeDTO();
        String cpf = employee.getCpf();

        when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(employee));
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO sut = employeeService.findByCpf(cpf);

        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(employeeDTO);

        verify(employeeRepository).findByCpf(cpf);
        verify(employeeMapper).employeeToEmployeeDTO(employee);
    }

    @Test
    public void getEmployee_withNonExistingCpf_ThrowsException() {
        when(employeeRepository.findByCpf(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findByCpf(any())).isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    public void createEmployee_withValidData_ReturnsEmployee() {
        Employee employee = getValidEmployee();
        EmployeeDTO employeeDTO = getValidEmployeeDTO();
        String password = employee.getPassword();
        String encodedPassword = "encoded_password";

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        EmployeeDTO sut = employeeService.create(employeeDTO);

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(employeeDTO);

        verify(employeeMapper).employeeDTOToEmployee(employeeDTO);
        verify(passwordEncoder).encode(password);
        verify(employeeRepository).save(any(Employee.class));
        assertThat(employee.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    public void updateEmployee_withValidData_ReturnsEmployee() {
        Employee employee = getValidEmployee();
        Employee updatedEmployee = getValidEmployeeUpdate();
        EmployeeDTO employeeDTO = getValidEmployeeDTO();
        EmployeeUpdateDTO updatedEmployeeDTO = getValidEmployeeDTOUpdate();

        employee.setId(1L);
        updatedEmployee.setId(1L);
        employeeDTO.setId(1L);
        updatedEmployeeDTO.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.employeeToEmployeeDTO(any(Employee.class))).thenReturn(employeeDTO);
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        EmployeeDTO sut = employeeService.update(updatedEmployeeDTO);

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(employeeDTO);

        verify(employeeMapper).employeeToEmployeeDTO(any(Employee.class));
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    public void deleteEmployee_withExistingId_ReturnsEmpty() {
        Employee employee = getValidEmployee();
        employee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        employeeService.delete(1L);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    }

    @Test
    public void deleteEmployee_withNonExistingId_ThrowsException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.delete(1L))
            .isInstanceOf(EmployeeNotFoundException.class);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).delete(any());
    }
}