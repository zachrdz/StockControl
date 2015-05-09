Team Members: 1

Zachary J. Rodriguez 
@01232135(hgv265)


--Structure-------------------------------------------------------

	I've seperated my "Modules" into seperate packages. 
	Program can be launched by running Main.java from the core package

	Each package contains the following folder structure:
	-controllers	
	-dao (Gateways)	
	-models
	 -obj (Object classes)
	-views


	My Packages:
	[core]
		[mdi]

		[inventoryModule]

		[partsModule]

		[productTemplatesModule]
		
		[session]

		[settings]

	[testingPackage] 
	All tests

	Other:
	MainFrame.java     (Main Class, Run this to start program)
	MasterFrame.java   (Master Frame that shares resources throughout the program)
	AppPreferences.xml (Contains database connection info & future preferences)(Hard coded in EJBs)
	[remote] packages contain interfaces are implemented in an EJB

--Environment------------------------------------------------------

	OS:           Windows 7
	IDE:          Eclipse EE Luna
	Java:         JDK 1.8.0_45
	GlassFish:    4.1 Full Platform

	Jars used in GlassFish "domains/domain1/lib/ext" location:
		- jedis-2.6.2.jar
		- mysql-connector-java-5.1.34-bin.jar

	Note: These jars can be found in my "lib" folder in the EJB project
			
	Other Build Notes:
		Make sure to update Build path for "gf-client.jar" in the client project to your local file.
		Java Compiler compliance level: 1.8

--Documentation----------------------------------------------------

	Location: "documentation" folder under the client project
		
		- Class Diagrams
		- DB Diagrams
		- Access control plan

	
--Gateways---------------------------------------------------------

	To start off, I have 6 DB gateway classes. Each gateway class is meant for it's own Model Class. 

	InvGateway:          contains all calls to stored procedures that I will use to obtain my results from the Inventory tables in the database.
	PartsGateway:        contains all calls to stored procedures that I will use to obtain my result from the Part tables in the database
	ProductsGateway:     contains all calls to stored procedures that I will use to obtain my result from the Products tables in the database
	ProductPartsGateway: contains all calls to stored procedures that I will use to obtain my result from the ProductPart tables in the database

	These 2 are located on an EJB session Bean:

	(Stateless) UsersGateway:        contains all calls to stored procedures that I will use to obtain my result for Users from the database
	(Singleton) InvItemLogGateway:   contains calls to Redis datasource to add log entries and retrieve them

--Models---------------------------------------------------------

	InvTableModel:         utilizes the InvGateway class & InvItemLogGatewayRemote class
	PartTableModel:        utilizes the PartsGateway class
	ProductsTableModel:    utilizes the ProductsGateway class
	ProductPartsTableModel utilizes the ProductPartsGateway class

	This 1 is located on an EJB session Bean:

	(Stateless) AuthenticatorModel     utilizes the UsersGateway class

--Views---------------------------------------------------------

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
			-InvAddView             (JInternalFrame)
			-InvDetailView          (JInternalFrame)
		-PartsTableView
			-PartsAddView           (JInternalFrame)
			-PartsDetailView        (JInternalFrame)
		-ProductTableView
			-ProductsAddView        (JInternalFrame)
			-ProductsDetailView     (JInternalFrame)
		-ProductPartsTableView
			-ProductPartsAddView    (JInternalFrame)
			-ProductPartsDetailView (JInternalFrame)

	Other:
	Inventory Item Log View is not its own class, but rather it is implemented as a JList inside of InventoryDetailView.
	AuthenticatorView is located in the session package. This is my login box that takes in a username and password. (JInternalFrame)

--Functionality-------------------------------------------------------

	When I start my program, the main fires off the MasterFrame JDesktopPane which allows me to manage all my modules in the same JFrame.
	Initially, the user will be greeted with a login box when the program is launched. All the modules share the same corresponding model instance.

	The user can launch one of the 3 modules by selecting "Modules" from the Menu Bar & selecting one.
	From here they can Edit & Add in the module by selecting the corresponding buttons to launch the views.

	All tables allow options to add, edit, and remove items by utilizing the functions in their models.

--Objects-------------------------------------------------------

	I have multiple types of value Object classes I use in my program. 
	These can be found in the "models/obj" package of the corresponding module

	-InvItem:            (Contains fields that are only located on the InvModule DB table. Also has a log field that represents a string list of it's history from Redis DB)
	-InvItemExt:         (Contains fields that are located on the InvModule DB table as well as INNER JOIN fields from the PartsModule DB table & ProductsModule DB table)
	-InvItemLogRecord:   (Contains fields that make up a log record for when inventory items are updated or added)
	-PartItem:           (Contains fields that are only located on the PartsModule DB table)
	-ProductItem:        (Contains fields that are only located on the ProductTemplatesModule DB table)
	-ProductPartItem:    (Contains fields that are only located on the ProductTemplatesParts DB table)
	-ProductPartItemExt: (Contains fields that are only located on the ProductTemplatesParts DB table as well as INNER JOIN fields from the PartsModule DB table)
	-Function:           (Contains fields that are only located on the Functions DB table. Used to describe a User)
	-Role:               (Contains fields that are only located on the Roles DB table. Used to describe a User)
	-User:               (Contains fields that are only located on the Users DB table)
	-Session:            (Contains fields that represent a Users session, consumes Function, Role & User)

	I use an ArrayList of PartItem objects in displaying the Parts List table view.
	I use an ArrayList of InvItemExt objects in displaying the Inventory List table view.
	I use an ArrayList of ProductItem objects in displaying the Product List table view
	I use an ArrayList of ProductPartItemExt objects in displaying the Product Parts List table view

	I use an ArrayList of InvItemLogRecod objects in displaying the log entries inside of the InvDetailView

--Database-------------------------------------------------------

	I'm using my own cloud servers to host both of these data sources 

	MySQL  (Used in client program)
	Redis (Used in EJB)

	MySQL  Host Address: coderoot.co
	Redis Host Address: 45.55.142.106

	All of the MySQL queries that my gateways send are executed using stored procedures.

--Logging--------------------------------------------------------

	MySQL Logging:
		On the database side I have MySQL logging tables for both the PartsModule, InvModule, ProductTemplatesModule & ProductTemplatesParts DB table.
		These logging tables are updated and maintained by CASCADING with a foreign key on the logging tables.
		These tables log when a record is updated, added or deleted from the corresponding table as well as adjust ID's that may get modified by users.
		I have CASCADING implemented on the ProductTemplatesParts DB Table to automatically remove records when the corresponding product is removed.

	Reddis Logging:
		For the final assignment I also implemented logging for Inventory Items via Reddis and a session EJB. These logging records are more descriptive and 
		are used to display to the user when they edit an inventory item. They are stored as an ordered set.
			They are added using the jedis zadd() method and retrieved using zrevrangeWithScores() method.
		
		Structure:     [keyname(includes inventory ID)]  [Current time in milliseconds]  [Description of log event]
		Add String Ex: invLogItem.48                     1431128665194                   "Part added with quantity '4' to location 'Facility 1' by John Doe"

		When displaying this to the user, the time is converted back into a Data object and formatted to display as yyyy/MM/dd HH:mm:ss

--Locking--------------------------------------------------------

	I've implemented pessimistic locking when editing records in the Inventory Module by using the timestamp from the MySQL log tables I mentioned above.

--Authentication-------------------------------------------------

	For Authentication I have an Authenticator Model/View/Controller/Gateway in the session package that handles my login sessions
	I created a series of tables to authorize role base logins

	Hover over the users name and role type in the top right corner of the application to view their access policy restrictions.

	The Authenticator class itself it implemented in a session EJB as a Stateless bean.
	The class passes back a Session object upon proper authentication of username and password.

--Observers------------------------------------------------------

	For the final assignment I implemented a remote observer for the Inventory Item Log Gateway EJB. This observer notifies both the MasterFrame as well as the 
	InvItemDetailView when a change is made to the log via the gateway bean.

	When the MasterFrame is notified is calls on the InvTableModel to reload it's data since a inventory item has been added or altered.
	When the InvItemDetailView is notified it reloads it's log view for it's inventory item that is open.

	This is done via the callback method in core/inventoryModule/models/InvItemLogObserver
	In order to reload the log view in InvItemDetailView I had to make my function asynchronous, since it was executing an EJB method through the log gateway.

--EJBs-----------------------------------------------------------

	I currently have to session EJBs:

	(Stateless)  Authenticator
	(Singleton) InvItemLogGateway

	Authenticator Bean:
		Located in [session] package
		Used to validate credentials that are passed in by the client program. If verified successfully, a Session Object is returned, else null is returned.
		Utilizes MySQL UsersGateway that lives on the EJB project as well.

	InvItemLogGateway:
		Located in [invModule] package
		A Redis gateway that is used to add and retrieve log records(InvItemLogRecord objects) for inventory items that are created/altered in the client program.

--Products-------------------------------------------------------

	For the Adding products to the inventoryModule, I extended out the InvItemExt class to include the fields I needed for parts
	I added a second drop down in the add and edit inventory item views

	I the had to create 2 long transactions that I call using stored procedures with my gateway object that add, delete & update products from the inventory module
	These add and delete stored procedure transactions take care of updating the associated parts quantities in the invModuleTable as well

--Testing--------------------------------------------------------

	I have Test cases that are basically aligned to each assignment, that I run to make sure my changes have taken effect and are working.
	I didn't really have time to create any test cases for assignment5 and 6 yet

-----------------------------------------------------------------