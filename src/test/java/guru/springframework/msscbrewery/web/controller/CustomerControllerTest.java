package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    CustomerDto validCustomer;

    @BeforeEach
    public void setUp() {
        validCustomer = CustomerDto.builder()
                .id(UUID.randomUUID())
                .customerName("Guru")
                .build();
    }

    @Test
    void getCustomer() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).
                willReturn(validCustomer);
        mockMvc.perform(get("/api/v1/customer/"  + validCustomer.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validCustomer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is("Guru")));
    }

    @Test
    void handlePost() throws Exception {
        CustomerDto customerDto = validCustomer;
        customerDto.setId(null);
        CustomerDto savedDto = CustomerDto.builder().id(UUID.randomUUID()).customerName("GuruEv").build();
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        given(customerService.saveNewCustomer(any())).willReturn(savedDto);

        mockMvc.perform(post("/api/v1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    void handleUpdate() throws Exception {
        CustomerDto customerDto = validCustomer;
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(put("/api/v1/customer/" + validCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isNoContent());

        then(customerService).should().updateCustomer(any(), any());
    }

}