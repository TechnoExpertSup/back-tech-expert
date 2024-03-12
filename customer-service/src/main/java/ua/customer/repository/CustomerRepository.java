package ua.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.customer.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
