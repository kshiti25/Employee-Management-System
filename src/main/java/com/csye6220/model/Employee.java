
package com.csye6220.model;

import com.csye6220.validator.NoSpecialCharacter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @Size(min = 2, message = "First name must be at least 2 characters")
    @NoSpecialCharacter
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2, message = "Last name must be at least 2 characters")
    @NoSpecialCharacter
    private String lastName;

    @Column(name = "email")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(name = "age")
    @Min(value = 0, message = "Age must be greater than or equal to 0")
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "salary")
    private BigDecimal salary;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    // ToString Method
    @Override
    public String toString() {
        return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email +
               ", age=" + age + ", gender=" + gender + ", salary=" + salary + "]";
    }
}