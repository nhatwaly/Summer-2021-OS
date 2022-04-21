# Summer-2021-OS
Communication among 4 processes in the same network in UTD’s Linux server. The processes must be able to send and receive commands and execute those commands.
The file server.java is run first to set up the communication point for the clients. 
Then, run client.java 4 times to initiate 4 clients on UTD's linux server. 
The processes will then be able to communicate with each other using commands:

The inputs from the user are the command being handled at the processes. In this project, the only commands
to be implemented are as follows:

– Send message to another process: The following command should send a message to the specified
process:

send receiver id MESSAGE

(e.g.: ”send 1 Hello!” sends Hello! to process 1)

– Send message to all processes: The following command should send a message to all the other three
processes:

send 0 MESSAGE

(e.g.: ”send 0 Hello!” sends Hello! to all processes)

– Stop: sends a Stop message to all other processes, and marks own state as stopped. When a process has
received Stop messages from all other processes and its own state is stopped, the process can close all its
socket connections and exit.

