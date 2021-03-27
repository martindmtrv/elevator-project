SYSC 3303 Elevator Assignment: Group 7
Iteration: 4

Members:
 -> Martin Dimitrov -101111948
 -> Ammar Tosun - 101172948
 -> Erdem Yanikomeroglu - 101080085
 -> Alex Cameron - 101114698
 -> David Casciano - 101069255
	
Setup Instructions (Iteration 4 (With UDP)):
	To run iteration 4 elevator system there are 3 .java files which must be run in the following order:
		1. Scheduler.java
		2. ElevatorSubsystem.java
		3. FloorSubsystem.java
		
	The 3 classes will communicate via UDP and print their respected outputs in their individual consoles.
	
Setup Instructions (No UDP + All output in one console):
	Run Project.java input is fed in from the file specified in Configuration.java (there is alternative running 
	options in there as well).
	
	Testing can be done with the individual files in the test package, or by right clicking the test package
		Run as -> Junit Test
	to run all the tests at once
	
	Diagrams for UML are available in UML Diagrams folder; state machines are located in state_machine_diagrams
	Timing diagrams in timing_diagrams
	

Contributions:
	Martin 101111948
		- setup Fault events and types
		- brainstorming for what types of faults there are and how they should be handled
		- git setup and organization
		- timers / tests for elevator timeouts (to handle travel timeouts, this is a scheduler generated fault)
		- Optimized / fixed old tests
		
	Ammar Tosun 101172948
		- Timing diagram and updated state diagram with fault events
		
	Erdem Yanikomeroglu 101080085
		- Configured scheduler to inject and receive elevator faults
		- Experimented with new elevator states for faults
		
	Alex Cameron 101114698
		- Fault handling for elevator subsystem.
		- Created new events to notify scheduler of elevators in fault states allowing them not to be scheduled.
		
	David Casciano 101069255
		- Fault injection from the input stream
		- Created Test cases for the fault event
		
		
		
Files:	
	src/elevator:
	Box.java		ElevatorButton.java	ElevatorLamp.java	ElevatorState.java
	Elevator.java		ElevatorDoor.java	ElevatorMotor.java	ElevatorSubsystem.java
	
	src/event:
	DirectionType.java			ElevatorTripUpdateEvent.java
	ElevatorApproachSensorEvent.java	Event.java
	ElevatorArriveEvent.java		EventType.java
	ElevatorButtonPressEvent.java		Fault.java
	ElevatorCallToMoveEvent.java		FaultType.java
	ElevatorFaultUpdateEvent.java		FloorButtonPressEvent.java
	ElevatorTravelTimeoutEvent.java
	
	src/floor:
	Floor.java		FloorLamp.java		InputStream.java
	FloorButton.java	FloorSubsystem.java
	
	src/main:
	Configuration.java	Project.java
	
	src/rpc:
	RpcHandler.java		RpcWorker.java		RpcWorkerType.java	SerializationUtils.java
	
	src/scheduler:
	BoundedBuffer.java	ElevatorJobState.java	ElevatorTripUpdate.java	State.java
	ElevatorEventTimer.java	ElevatorStatus.java	Scheduler.java
	
	src/test:
	ElevatorSubsystemTest.java	FloorSubsystemTest.java		TestSuite.java
	ElevatorTimerTest.java		SerializationTest.java
	FaultEventTest.java		StateMachineTest.java
	
	UML Diagrams:
	EventTypes-Class-UML.png	UML_Object_Diagram.png		UML_Sequence_Diagram_I3.png
	EventTypes-Class-UML.ucls	UML_Sequence_Diagram.drawio
	
	state_machine_diagrams:
	ElevatorStateMachineDiagram.drawio		iteration2_scheduler_state_diagram.drawio
	ElevatorStateMachineDiagram.png			scheduler_state_diagram.png
	ElevatorStateMachineDiagram_ITER4.png
	
	timing_diagrams:
	ARRIVAL_SENSOR_FAIL_TimingDiagram.pdf	MOTOR_FAIL_TimingDiagram.png
	ARRIVAL_SENSOR_FAIL_TimingDiagram.png	NormalWorkingTimingDiagram.pdf
	DOOR_STUCK_TimingDiagram.pdf		NormalWorkingTimingDiagram.png
	DOOR_STUCK_TimingDiagram.png		TimingDiagrams.xlsx
	MOTOR_FAIL_TimingDiagram.pdf
	

	README.txt	
	Test.txt	
	TestComm.txt

