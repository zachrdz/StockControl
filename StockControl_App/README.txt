Team Members: 1

Zachary J. Rodriguez 
@01232135(hgv265)


How Everything Works:

I've seperated my "Modules" into seperate packages. 
Program can be launched by running MasterFrame.java

Each package contains the following folder structure:
-controllers	
-models
-views
-dao (Gateways)
-interfaces	

My Packages:
[app]
	[src/app/inventoryModule]
	inventoryModule
	

	[src/app/partsModule]
	partsModule
	

	[src/app/productTemplatesModule]
	productTemplatesModule

	[src/app/settings]
	settings

[testingPackage] 
All tests

Other:
MasterFrame.java   (Main Class, Run this to start program)
AppPreferences.xml (Contains database connection info & future preferences)


To start off, I have 5 DB gateway classes. Each gateway class is meant for it's own Model Class. 

InvGateway:          contains all calls to stored procedures that I will use to obtain my results from the Inventory tables in the database.
PartsGateway:        contains all calls to stored procedures that I will use to obtain my result from the Part tables in the database
ProductsGateway:     contains all calls to stored procedures that I will use to obtain my result from the Products tables in the database
ProductPartsGateway: contains all calls to stored procedures that I will use to obtain my result from the ProductPart tables in the database
UsersGateway:        contains all calls to stored procedures that I will use to obtain my result for Users from the database

InvTableModel:         utilizes the InvGateway class
PartTableModel:        utilizes the PartsGateway class
ProductsTableModel:    utilizes the ProductsGateway class
ProductPartsTableModel utilizes the ProductPartsGateway class
AuthenticatorModel     utilizes the UsersGateway class

These models are for the four table views that are shown in my program.  The views access them by their corresponding controllers.
The ProductPartsTableView is a JPanel added inside of the ProductsDetailView JInternalFrame.

(Users doesn't have a view for a Manager)
So, in total I have 4 main views that my models represent (3 main JInternalFrames + 1 main JPanel):
	-InventoryTableView     (JInternalFrame)
	-PartsTableView         (JInternalFrame)
	-ProductsTableView      (JInternalFrame)
	-ProductPartsTableView  (JPanel inside of the ProductsDetailView JInternalFrame)

Each view has an Edit/Detail & Add view that corresponds to it:
	-InventoryTableView 
		-InventoryAddView       (JInternalFrame)
		-InventoryDetailView    (JInternalFrame)
	-PartsTableView
		-PartsAddView           (JInternalFrame)
		-PartsDetailView        (JInternalFrame)
	-ProductTableView
		-ProductsAddView        (JInternalFrame)
		-ProductsDetailView     (JInternalFrame)
	-ProductPartsTableView
		-ProductPartsAddView    (JInternalFrame)
		-ProductPartsDetailView (JInternalFrame)


When I start my program, the main fires off the MasterFrame JDesktopPane which allows me to manage all my modules in the same JFrame.
Initially, no JInternal Frames are launched. All the modules share the same corresponding model instance.

The user can launch one of the 3 modules by selecting "Modules" from the Menu Bar & selecting one.
From here they can Edit & Add in the module by selecting the corresponding buttons to launch the views.

All tables allow options to add, edit, and remove items by utilizing the functions in their models.

I have 5 types of value Object classes I use in my program. 
-InvItem:            (Contains fields that are only located on the InvModule DB table)
-InvItemExt:         (Contains fields that are located on the InvModule DB table as well as INNER JOIN fields from the PartsModule DB table & ProductsModule DB table)
-PartItem:           (Contains fields that are only located on the PartsModule DB table)
-ProductItem:        (Contains fields that are only located on the ProductTemplatesModule DB table)
-ProductPartItem:    (Contains fields that are only located on the ProductTemplatesParts DB table)
-ProductPartItemExt: (Contains fields that are only located on the ProductTemplatesParts DB table as well as INNER JOIN fields from the PartsModule DB table)


I use an ArrayList of PartItem objects in displaying the Parts List table view.
I use an ArrayList of InvItemExt objects in displaying the Inventory List table view.
I use an ArrayList of ProductItem objects ing displaying the Product List table view
I use an ArrayList of ProductPartItemExt objects ing displaying the Product Parts List table view

On the database side I have logging tables for both the PartsModule, InvModule, ProductTemplatesModule & ProductTemplatesParts DB table.
These logging tables are updated and maintained by CASCADING with a foreign key on the logging tables.
These tables log when a record is updated, added or deleted from the corresponding table as well as adjust ID's that may get modified by users.

I use the timestamps given from these logs to keep a poll running on my program that checks for database changes by storing the time updated compared
to the latest timestamp in the logs. When the time updated is less than the logs, my program automatically updates with the new data. This allows multiple
instances of the program running at once.

I have cascading implemented on the ProductTemplatesParts DB Table to automatically remove records when the corresponding product is removed.

I've implemented pessimistic locking when editing records in the Inventory Module by using the timestamp from the log tables I mentioned above.

For Authentication I have an Authenticator Model/View/Controller/Gateway in the settings package that handles my login sessions
I created a series of tables to authorize role base logins

For the Adding products to the inventoryModule, I extended out the InvItemExt class to include the fields I needed for parts
I added a second drop down in the add and edit inventory item views

I the had to create 2 long transactions that I call using stored procedures with my gateway object that add and delete products from the inventory module
These add and delete stored procedure transactions take care of updating the associated parts quantities in the invModuleTable as well

I have Test cases that are basically aligned to each assignment, that I run to make sure my changes have taken effect and are working.