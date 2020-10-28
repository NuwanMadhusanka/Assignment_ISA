package com.isa.grpc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Employee {

    @Id
    @NotNull(message = "EmployeeId must not be null")
    @Positive(message = "Employee's id must not be negative")
    private Integer employeeId;

    @NotBlank(message = "FirstName must not be blank")
    private String firstName;

    @NotBlank(message = "LastName must not be blank")
    private String lastName;

    @NotBlank(message = "Department name must not be blank")
    private String department;

    @NotBlank(message = "Team name must not be blank")
    private String team;

    @NotNull(message = "Join date must not be null")
    @PastOrPresent(message = "Join date must not be future date")
    private LocalDate joinDate;


    @Size(min = 10, max = 10, message = "Mobile number should be 10 digits")
    @Column(unique = true)
    private String mobile;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    public Employee() {
    }

    public Employee(@NotNull(message = "EmployeeId must not be null") @Positive(message = "Employee's id must not be negative") Integer employeeId, @NotBlank String firstName, @NotBlank(message = "LastName must not be blank") String lastName, @NotBlank(message = "Department name must not be blank") String department, @NotBlank(message = "Team name must not be blank") String team, @NotNull(message = "Join date must not be null") @PastOrPresent(message = "Join date must not be future date") LocalDate joinDate, @Size(min = 10, max = 10, message = "Mobile number should be 10 digits") String mobile, @Email(message = "Email should be valid") String email) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.team = team;
        this.joinDate = joinDate;
        this.mobile = mobile;
        this.email = email;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", department='" + department + '\'' +
                ", team='" + team + '\'' +
                ", joinDate=" + joinDate +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
