SYSC 3303 Elevator Assignment: Group 7
Iteration: 2

Members:
 -> Martin Dimitrov -101111948
 -> Ammar Tosun - 101172948
 -> Erdem Yanikomeroglu - 101080085
 -> Alex Cameron - 101114698
 -> David Casciano - 101069255
	
Setup Instructions:
	Run Project.java input is fed in from the file specified in Configuration.java (there is alternative running 
	options in there as well).
	
	Testing can be done with TestSuite.java or all the individual files in the test package.
	
	Diagrams for UML are available in UML Diagrams folder; state machines are located in state_machine_diagrams
	

Contributions:
	Martin 101111948
		- RPC package setup and configuration
		- Serialization utils (for sending Events as bytes over UDP)
		- UDP setup and RpcWorker creation for sending / receiving
		- Hooked up and configured all ports for runnning as seperate processes
		
	Ammar Tosun 101172948
		
		
	Erdem Yanikomeroglu 101080085
		
		
	Alex Cameron 101114698
		
		
	David Casciano 101069255
		
		
Files:
TODO NEED TO UPDATE
	UML Diagrams:
		EventTypes-Class-UML.png
		EventTypes-Class-UML.ucls
		UML_Object_Diagram.png
		UML_Sequence_Diagram.drawio
		UML_Sequence_Diagram.png
	
	state_machine_diagrams:
		ElevatorStateMachineDiagram.drawio
		ElevatorStateMachineDiagram.png
		iteration2_scheduler_state_diagram.drawio
		scheduler_state_diagram.png
	
	src/elevator:
		Box.java		ElevatorDoor.java	ElevatorState.java
		Elevator.java		ElevatorLamp.java	ElevatorSubsystem.java
		ElevatorButton.java	ElevatorMotor.java
	
	src/event:
		DirectionType.java			ElevatorTripUpdateEvent.java
		ElevatorApproachSensorEvent.java	Event.java
		ElevatorArriveEvent.java		EventType.java
		ElevatorButtonPressEvent.java		FloorButtonPressEvent.java
		ElevatorCallToMoveEvent.java
	
	src/floor:
		Floor.java		FloorLamp.java		InputStream.java
		FloorButton.java	FloorSubsystem.java
	
	src/main:
		Configuration.java	Project.java
	
	src/scheduler:
		BoundedBuffer.java	ElevatorStatus.java	Scheduler.java
		ElevatorJobState.java	ElevatorTripUpdate.java	State.java
	
	src/test:
		ElevatorSubsystemTest.java	StateMachineTest.java
		FloorSubsystemTest.java		TestSuite.java

	Test.txt
	TestComm.txt
	README.txt

