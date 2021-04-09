SYSC 3303 Elevator Assignment: Group 7
Final Submission

Members:
 -> Martin Dimitrov -101111948
 -> Ammar Tosun - 101172948
 -> Erdem Yanikomeroglu - 101080085
 -> Alex Cameron - 101114698
 -> David Casciano - 101069255

(final report has been submitted as a pdf alongside this iteration)

Setup Instructions (processes created automatically):
	To run the full elevator system from a single class, run ProjectUDP.java.
	
	This class will create separate processes for each of our components as described in the second setup instruction,
	without all the extra work of starting 3 classes separately. 

	NOTE: When running this way you stop all 3 processes by closing the GUI, not by stopping in Eclipse
		stopping through eclipse may cause processes to linger and hog ports in the background,
		if this happens they must be killed via the task manager
	
Setup Instructions (processes created manually):
	To run iteration 4 elevator system there are 3 .java files which must be run in the following order:
		1. Scheduler.java
		2. ElevatorSubsystem.java
		3. FloorSubsystem.java
		
	The 3 classes will communicate via UDP and print their respected outputs in their individual consoles.
	
	
Testing can be done with the individual files in the test package, or by right clicking the test package
		Run as -> Junit Test
	to run all the tests at once
	
Diagrams for UML are available in UML Diagrams folder; state machines are located in state_machine_diagrams
Timing diagrams in timing_diagrams
	
Troubleshooting:
	If parts of the UI are getting cut off in the display window, make sure that the window is maximized and your OS's
	UI is not scaled above 100%

Fault format:
	14:05:35.6 FAULT 1 MOTOR_FAIL
	time FAULT car FAULT_TYPE(from enum)
	

Contributions:
	Martin Dimitrov 101111948
	- created new class for launching all processes at once
		(using processbuilders it creates 3 seperate processes from one class)	
	- bug fixes, making sure all the elevators behave properly after Faults
	- compiling final report documentation (contributions, setup instructions, layout)
	- added logfile output option in config
		
		
	Ammar Tosun 101172948
	- Helped preparing the final report by compiling the diagrams
		
	Erdem Yanikomeroglu 101080085
	- Modified Test.txt to test edge cases of system
	- Stress tested various aspects of the system such as large quantities of faults and requests
	- Added troubleshooting section to Readme
		
	Alex Cameron 101114698
	- Created GUI to represent the elevator system visually using the swing framework.
	- Implemented Model, Controller, View software engineering strategy to display model data on a user interface.
		
	David Casciano 101069255
		
		
		
Files:	
	README.txt	Test.txt	TestComm.txt
	
	Reports:
	report.txt
	
	UML Diagrams:
	EventTypes-Class-UML.png	UML_Sequence_Diagram.drawio
	EventTypes-Class-UML.ucls	UML_Sequence_Diagram_I3.png
	UML_Object_Diagram.png
	
	
	logs:
	elevatorLog.txt		floorLog.txt		schedulerLog.txt
	
	src/elevator:
	Box.java		ElevatorDoor.java	ElevatorState.java
	Elevator.java		ElevatorLamp.java	ElevatorSubsystem.java
	ElevatorButton.java	ElevatorMotor.java
	
	src/event:
	DirectionType.java			Event.java
	ElevatorApproachSensorEvent.java	EventType.java
	ElevatorArriveEvent.java		Fault.java
	ElevatorButtonPressEvent.java		FaultType.java
	ElevatorCallToMoveEvent.java		FloorButtonPressEvent.java
	ElevatorFaultUpdateEvent.java		NotificationEvent.java
	ElevatorTravelTimeoutEvent.java		NotificationEventType.java
	ElevatorTripUpdateEvent.java
	
	src/floor:
	Floor.java		FloorLamp.java		InputStream.java
	FloorButton.java	FloorSubsystem.java
	
	src/main:
	Configuration.java	ProjectUDP.java
	
	src/rpc:
	RpcHandler.java		RpcWorkerType.java
	RpcWorker.java		SerializationUtils.java
	
	src/scheduler:
	BoundedBuffer.java	ElevatorStatus.java	Scheduler.java
	ElevatorEventTimer.java	ElevatorTripUpdate.java	State.java
	ElevatorJobState.java	GUI
	
	src/scheduler/GUI:
	CarView.java			NotificationType.java
	ElevatorInfoView.java		NotificationView.java
	InputFileView.java		NotificationViewListener.java
	Notification.java		SchedulerController.java
	NotificationController.java	SchedulerView.java
	NotificationModel.java		SchedulerViewListener.java
	NotificationPanel.java
	
	src/test:
	ElevatorSubsystemTest.java	SerializationTest.java
	ElevatorTimerTest.java		StateMachineTest.java
	FaultEventTest.java		TestSuite.java
	FloorSubsystemTest.java
	
	state_machine_diagrams:
	ElevatorStateMachineDiagram.drawio
	ElevatorStateMachineDiagram.png
	ElevatorStateMachineDiagram_ITER4.png
	iteration2_scheduler_state_diagram.drawio
	scheduler_state_diagram.png
	
	timing_diagrams:
	ARRIVAL_SENSOR_FAIL_TimingDiagram.pdf	MOTOR_FAIL_TimingDiagram.png
	ARRIVAL_SENSOR_FAIL_TimingDiagram.png	NormalWorkingTimingDiagram.pdf
	DOOR_STUCK_TimingDiagram.pdf		NormalWorkingTimingDiagram.png
	DOOR_STUCK_TimingDiagram.png		TimingDiagrams.xlsx
	MOTOR_FAIL_TimingDiagram.pdf

