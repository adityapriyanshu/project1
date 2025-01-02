package com.cts.main.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cts.main.controllers.CustomerOrderController;
import com.cts.main.dtos.CustomerOrderDTO;
import com.cts.main.entities.CustomerOrder;
import com.cts.main.responses.ApiResponse;
import com.cts.main.services.CustomerOrderService;

public class CustomerOrderControllerTest {

    @Mock
    private CustomerOrderService customerOrderService;

    @InjectMocks
    private CustomerOrderController customerOrderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomerOrder() {
        List<CustomerOrder> orders = Arrays.asList(new CustomerOrder(), new CustomerOrder());
        when(customerOrderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<ApiResponse<List<CustomerOrder>>> response = customerOrderController.getAllCustomerOrder();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customers fetched successfully!", response.getBody().getStatus());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    public void testGetCustomerOrderById() {
        CustomerOrder order = new CustomerOrder();
        order.setId(1L);
        when(customerOrderService.getOrderById(1L)).thenReturn(order);

        ResponseEntity<ApiResponse<CustomerOrder>> response = customerOrderController.getCustomerOrderById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer order fetched successfully!", response.getBody().getStatus());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    public void testAddCustomerOrder() {
        CustomerOrderDTO orderDTO = new CustomerOrderDTO();
        orderDTO.setCustomerName("Test Customer");
        orderDTO.setCustomerPhoneNumber("1234567890");
        orderDTO.setCustomerTableNumber("5");
        orderDTO.setOrderItems(Map.of(1L, 2, 2L, 1));
        when(customerOrderService.createOrder(any(CustomerOrderDTO.class))).thenReturn(new CustomerOrder());
        
        ResponseEntity<ApiResponse<String>> response = customerOrderController.addCustomerOrder(orderDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer order created successfully!", response.getBody().getStatus());
    }

    @Test
    public void testUpdateCustomerOrderById() {
        CustomerOrderDTO orderDTO = new CustomerOrderDTO();
        orderDTO.setCustomerName("Updated Customer");
        orderDTO.setCustomerPhoneNumber("0987654321");
        orderDTO.setCustomerTableNumber("10");
        orderDTO.setOrderItems(Map.of(1L, 3, 2L, 2));

        when(customerOrderService.updateOrderById(eq(1L), any(CustomerOrderDTO.class))).thenReturn(new CustomerOrder());

        ResponseEntity<ApiResponse<String>> response = customerOrderController.updateCustomerOrderById(1L, orderDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer order updated successfully!", response.getBody().getStatus());
    }

    @Test
    public void testDeleteCustomerOrderById() {
        doNothing().when(customerOrderService).deleteOrder(1L);

        ResponseEntity<ApiResponse<String>> response = customerOrderController.deleteCustomerOrderById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer order deleted successfully!", response.getBody().getStatus());
    }

    @Test
    public void testGetAllOrdersForCooks() {
        List<Map<String, Object>> orders = List.of(Map.of("id", 1), Map.of("id", 2));
        when(customerOrderService.getAllOrdersForCooks()).thenReturn(orders);

        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = customerOrderController.getAllOrdersForCooks();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Orders fetched successfully!", response.getBody().getStatus());
        assertEquals(2, response.getBody().getData().size());
    }
}
