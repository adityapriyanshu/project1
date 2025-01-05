package com.cts.main.services;

import java.util.List;
import java.util.Map;

import com.cts.main.dtos.CustomerOrderDTO;
import com.cts.main.entities.CustomerOrder;


public interface CustomerOrderService {
	List<CustomerOrder> getAllOrders();

	CustomerOrder getOrderById(Long id);

	CustomerOrder createOrder(CustomerOrderDTO orderDTO);

	CustomerOrder updateOrderById(Long id, CustomerOrderDTO orderDTO);

	void deleteOrder(Long id);

	List<Map<String, Object>> getAllOrdersForCooks();
}
