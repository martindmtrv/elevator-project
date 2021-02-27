SYSC 3303 Elevator Assignment: Group 7
Iteration: 2

Members:
 -> Martin Dimitrov -101111948
 -> Ammar Tosun - 101172948
 -> Erdem Yanikomeroglu - 101080085
 -> Alex Cameron - 101114698
 -> David Casciano - 101069255
 
Files:
	
Setup Instructions:

	

Contributions:
	Martin 101111948
		- Scheduler logic for coordination and communication with elevator subsystem
		- Trip planning and updating by handling arrival sensor notifications
		- Managing elevator states via the scheduler (pre planning for later GUI implementation)
		
	Ammar Tosun 101172948
		
	Erdem Yanikomeroglu 101080085
		
	Alex Cameron 101114698
		- Implemented Arrival sensor 
		- Reworked elevator subsystem to ensure each elevator operate as individual threads with ability to communicate with other systems.
		- Implemented Loading and in-between-floor timing within elevator system and added time stamps in output
		
	David Casciano 101069255
		- Implemented Verbose feature that can be used will testing
		- Reworked FloorSubsystem, creating a new thread specifically for reading in requests
		- Updated Existing testing infrastructure
		- Created testing for new state machines
		- Updated Object UML (Decluttered and Simplified)
