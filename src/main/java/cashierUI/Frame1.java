package cashierUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.DataModel;
import cashierUI.Frame1;
import domain.Item;

public class Frame1 extends JFrame{
	private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField shiftStartField;
    private JTextField shiftEndField;
    private JButton shiftStartButton;
    private JButton shiftEndButton;
    private JButton loadButton;
    private JButton showButton;
    private JTextField codeField = new JTextField();
    private JTextField qtyField = new JTextField();
    private JTextField removeCodeField = new JTextField();
    private JButton addButton = new JButton("Add");
    private JButton removeButton = new JButton("Remove");
    
    public Frame1(DataModel model) {
    	setTitle("Cashier Shift Management");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
      
/////////////////////////////////Panel 1//////////////////////////////////////////////////
        
        JPanel shiftPanel = new JPanel();
        shiftPanel.setLayout(new GridLayout(5, 2, 5, 5));
        
        // Button to start the shift
        shiftStartButton = new JButton("Start Shift");
        // add action listener
        shiftStartButton.addActionListener(e -> {
        	String firstName = firstNameField.getText();
    		String lastName = lastNameField.getText();
    		
    		// check if first and last name field are valid
    		if (firstName.isEmpty() || lastName.isEmpty()) {
    			JOptionPane.showMessageDialog(Frame1.this, "Please enter both first name and last name.", "Error", JOptionPane.ERROR_MESSAGE);
    			return;
    		}
    		
    		// format the shift start date and time
    		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		shiftStartField.setText(simpleDate.format(new Date()));
    		
    		System.out.println("Casher added: " + firstName + " " + lastName);
    		
    		// save cashier's name
            model.setCashier(firstNameField.getText(), lastNameField.getText());
        });
        // add the button to the panel
        shiftPanel.add(shiftStartButton);
        
        // Button to end the shift
        shiftEndButton = new JButton("End Shift");
        // add action listener
        shiftEndButton.addActionListener(e -> {
        	SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		shiftEndField.setText(simpleDate.format(new Date()));
    		model.cartDispose();
        });
        // add the button to the panel
        shiftPanel.add(shiftEndButton);
        
        // create and add text field 
        shiftPanel.add(new JLabel("First Name: "));
        firstNameField = new JTextField(10);
        shiftPanel.add(firstNameField);
        shiftPanel.add(new JLabel("Last Name: "));
        lastNameField = new JTextField(10);
        shiftPanel.add(lastNameField);
        shiftPanel.add(new JLabel("Shift Start Time: "));
        shiftStartField = new JTextField(20);
        shiftStartField.setEditable(false);
        shiftPanel.add(shiftStartField);
        shiftPanel.add(new JLabel("Shift End Time: "));
        shiftEndField = new JTextField(20);
        shiftEndField.setEditable(false);
        shiftPanel.add(shiftEndField);
        
        // add the panel to the frame
        add(shiftPanel, BorderLayout.NORTH);
        
/////////////////////////////////Panel 2//////////////////////////////////////////////////
        
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        
        loadButton = new JButton("Load Inventory");
        showButton = new JButton("Show Products");
        
        // add ActionListener to load the inventory
        loadButton.addActionListener(e -> {
        	model.updateJSON();
        	System.out.println("Inventory was loaded.");
        });
        
        // add ActionListener to show the inventory
        showButton.addActionListener(e -> {
        	// create a new frame to show the inventory products
        	JFrame productFrame = new JFrame("Product List");
    		productFrame.setSize(600, 300);
    		productFrame.setLayout(new BorderLayout());
    		
    		JTextArea productArea = new JTextArea();
    		productArea.setEditable(false);
    		String itemCode = codeField.getText().trim();
    		
    		// Check if the inventory was loaded before show the list
    		if (model.inventory.isEmpty()) {
        		JOptionPane.showMessageDialog(this, "No inventory loaded, please load the inventory first.", "Error", JOptionPane.ERROR_MESSAGE);
    			return;
        	}
    		
    		// If no *  in the product code text field, list all products
    		if (!itemCode.contains("*")) {
    			// Format and show the product list
    			StringBuilder productList = new StringBuilder("Code\tName\t\tPrice\tDescription\n");
    			for (Map.Entry<String, Item> entry : model.inventory.entrySet()) {
    				Item item = entry.getValue();
    				productList.append(String.format("%-10s\t%-25s\t%-10s\t%-30s\n", entry.getKey(), item.getName(), item.getPrice(), item.getDescription()));
    			}
    			productArea.setText(productList.toString());
    		}
    		
    		// If product code contains *, list products start with the entered code
    		else {
    			String prefix = itemCode.replace("*", "");
    			boolean foundItem = false;
                StringBuilder productList = new StringBuilder("Code\tName\t\tPrice\tDescription\n");
    			
    			for (String productCode : model.inventory.keySet()) {
    				if (productCode.startsWith(prefix)) {
    					foundItem = true;
    					Item item = model.inventory.get(productCode);
    					productList.append(String.format("%-10s\t%-25s\t%-10s\t%-30s\n", productCode, item.getName(), item.getPrice(), item.getDescription()));
    				}
    			}
                productArea.setText(productList.toString());
    			
    			if (!foundItem) {
    				JOptionPane.showMessageDialog(this, "No matching products found for the given prefix", "Error", JOptionPane.ERROR_MESSAGE);
    				return;
    			}
    		}
    		
    		JScrollPane scrollPane = new JScrollPane(productArea);
    		
    		// close the frame when button is clicked
    		JButton closeButton = new JButton("Close");
    		closeButton.addActionListener(event -> productFrame.dispose());
    		
    		productFrame.add(scrollPane, BorderLayout.CENTER);
    		productFrame.add(closeButton, BorderLayout.SOUTH);
    		productFrame.setVisible(true);
        });
        
        // add buttons to the panel
        panel2.add(loadButton);
        panel2.add(showButton);
        
        // add the panel to the frame
        add(panel2, BorderLayout.CENTER);
        
/////////////////////////////////Panel 3//////////////////////////////////////////////////
        
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        JPanel addItemPanel = new JPanel();
        JPanel removeItemPanel = new JPanel();
        
        // format adding and removing function
        addItemPanel.setLayout(new GridLayout(1, 5));
        removeItemPanel.setLayout(new GridLayout(1, 3, 5, 5));
        addItemPanel.add(new JLabel("Item (code): "));
        addItemPanel.add(codeField);
        addItemPanel.add(new JLabel("QTY: "));
        addItemPanel.add(qtyField);
        addItemPanel.add(addButton);
        removeItemPanel.add(new JLabel("Item #: "));
        removeItemPanel.add(removeCodeField);
        removeItemPanel.add(removeButton);
        
        // ActionListener to add new items into the cart
        addButton.addActionListener(e ->{
        	String code = codeField.getText().trim();
        	String quantity = qtyField.getText().trim();
        	
        	// Check if both item code and quantity are entered
        	if (code.isEmpty() || quantity.isEmpty()) {
        		JOptionPane.showMessageDialog(this, "Please enter valid product code or quantity.", "Error", JOptionPane.ERROR_MESSAGE);
    			return;
        	}
        	
        	// Check if the entered item code exist
        	if (!model.inventory.containsKey(code)) { // entered code doesn't exist
        		JOptionPane.showMessageDialog(this, "The product code entered does not exist", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        	else { // entered code matches inventory
        		int qtyNumber = Integer.parseInt(quantity);
        		model.addItemToCart(code, qtyNumber);
        		codeField.setText("");
        		qtyField.setText("");
        	}
        });
        
        // ActionLintener to remove added items from the cart
        removeButton.addActionListener(e ->{
        	String removeCode = removeCodeField.getText().trim();
        	model.removeItem(removeCode);
        	removeCodeField.setText("");
        });
        
        itemPanel.add(addItemPanel, BorderLayout.NORTH);
        itemPanel.add(removeItemPanel, BorderLayout.SOUTH);
        add(itemPanel, BorderLayout.SOUTH);
    }
}
