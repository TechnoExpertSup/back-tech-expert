package ua.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomerUpdateRequest {

    private String firstName;
    private String lastName;
}
