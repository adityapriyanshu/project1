package com.cts.main.services;

import java.util.List;
import com.cts.main.dtos.MenuItemDTO;
import com.cts.main.entities.MenuItem;

public interface MenuItemService {
	MenuItem addFoodItem(MenuItemDTO menuItemDTO);

	List<MenuItem> getAllItems();

	MenuItem updateFoodItemById(Long id, MenuItemDTO menuItemDTO);

	void deleteFoodItemById(Long id);
}
