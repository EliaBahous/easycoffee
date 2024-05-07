
# Café Management System Documentation
## Introduction
The Café Management System is a Java-based server application designed to automate various tasks in a small café, including managing the menu, handling table transactions, and generating bills.
Features
Add, remove, and update items on the menu.
Add, remove, and update items on individual tables.
Calculate the total bill for each table.
Close tables and mark them as 'closed' in the system.
Installation
Check the installation guide please.

- Managing Menu:
Use the provided API endpoints to add, remove, or update items on the menu.
- Managing Tables:
Use the provided API endpoints to add, remove, or update items on individual tables.
Close tables using the appropriate endpoint when customers leave.
- API Endpoints:
  - /take-order: POST - this can open a table and insert a new menu item for it.
  - /pay-bill: POST - this will close the table (consideration after approving the VISA)
  - /set-quantity: POST - this will update the quantity of items from the menu.
  - /set-discount: POST - this will update the discount for specified items in the menu.
  - /get-ordered-items: POST - this will return all the items related to the open table.
  - /get-number-of-tables: GET - return number of current tables
  - /get-items: POST: return items from the menu.
  - /cancel-order: POSt: request for cancel order.
  - /get-orders: POST: return all the orders for a specific table.
  - /add-new-item: POST: request to add new item for the meu
  - /add-table: POST : request to add new table.

- Testing
  - Black-box testing has been performed to ensure the correctness and reliability of the Café Management System. Refer to the provided test cases for more details.
  Change Quantity of Item from Menu:
    - Test Case 1: Verify that the quantity of an item can be increased.
    - Test Case 2: Verify that the quantity of an item can be decreased.
    - Test Case 3: Verify that the quantity of an item cannot be set to a negative value.
    - Test Case 4: Verify that an error is returned if the item does not exist in the menu.
  - Add Item from Menu to Table/Change Amount:
    - Test Case 1: Verify that an item can be added to a table.
    - Test Case 2: Verify that the quantity of an existing item on the table can be increased.
    - Test Case 3: Verify that the quantity of an existing item on the table can be decreased.
    - Test Case 4: Verify that the quantity of an existing item on the table cannot be set to a negative value.
    - Test Case 5: Verify that an error is returned if the item does not exist in the menu.
  - Close Table:
    - Test Case 1: Verify that the total bill is calculated correctly when closing the table.
    - Test Case 2: Verify that the table status is updated to 'closed' after closing the table.
    - Test Case 3: Verify that an error is returned if the table does not exist.
  - Summary
     - The Café Management System is a Java-based server application tailored for small cafés. It automates menu management, table transactions, and bill generation. Through this project, we've learned how to build a scalable and efficient system to streamline café operations, enhancing both staff productivity and customer experience.



## License
This project is licensed under the MIT License - see the LICENSE file for details.

