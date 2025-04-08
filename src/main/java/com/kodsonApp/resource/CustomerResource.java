package com.kodsonApp.resource;

import com.kodsonApp.domain.Customer;
import com.kodsonApp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/customer"})
@RequiredArgsConstructor
public class CustomerResource {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("/mobik/customer/customerID")).body(customerService.createCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<Page<Customer>> getCustomers(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(customerService.getAllCustomers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(customerService.getCustomer(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Customer>> getCustomerByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(customerService.getCustomerByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        customerService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
