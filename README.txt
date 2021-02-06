SYSC 3303 Elevator Assignment: Group 7
Iteration: 1

Members:
 -> Martin Dimitrov -101111948
 -> Ammar Tosun - 101172948
 -> Erdem Yanikomeroglu - 101080085
 -> Alex Cameron - 101114698
 -> David Casciano - 101069255
 
Files:
	BoundedBuffer.java			Event.java
	Configuration.java			EventType.java
	DirectionType.java			Floor.java
	Elevator.java				FloorButton.java
	ElevatorArriveEvent.java		FloorButtonPressEvent.java
	ElevatorButton.java			FloorLamp.java
	ElevatorButtonPressEvent.java		FloorSubsystem.java
	ElevatorDoor.java			FloorSubsystemTest.java
	ElevatorLamp.java			Iteration1.java
	ElevatorMotor.java			ElevatorSubsystem.java			
	Scheduler.java				ElevatorCalledEvent.java
	ElevatorMoveEvent.java			ElevatorSubsystemTest.java
	FloorSubsystemTest.java			Iteration1Test.java
	EventTypes-Class-UML.png		README.txt
	Test.txt					TestComm.txt
	UML_OBJECT_DIAGRAM.png		UML_Sequence_Diagram.png
	
Setup Instructions:

	To Run Tests:
		-> Navigate into Test Package
		-> Run Iteration1Test.java
		-> Run ElevatorSubsystemTest.java
		-> Run FloorSubsytemTest.java
	
	To Run The Project:
		-> Navigate to the Main Package
		-> Run Iteration1.java

Contributions:
	Martin 101111948
		- Git repo setup and organization of issues and tasks
		- Floor subsystem work creating all related classes
		- Events framework all classes regarding different events to send
		- Event UML
		
	Ammar Tosun 101172948
		- Elevator subsystem work creating all related classes
		- UML Sequence diagram
		
	Erdem Yanikomeroglu 101080085
		- Scheduler to handle passing events between subsystems
		
	Alex Cameron 101114698
		- Developed test cases using JUnit5 framework
		- Thread assembly
		
	David Casciano 101069255
		- Partially developed request class
		- UML Object diagram
		- README.txt
