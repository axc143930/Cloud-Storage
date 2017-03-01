# Cloud-Storage

1. The aim of this project is to implement a Server- Client model using Java and TCP/IP sockets where clients can upload files in a certain folder on their local file system on to the server. The server checks the file list and synchronizes them among all devices. 
2. The Server is implemented on AWS. A windows instance is created in AWS, the server code is run in Eclipse on AWS instance. 
3. There are two clients one on the laptop and other on an Android device. On the execution of the client code, the files in specific folder in local machine would be uploaded on to the AWS Server.
4. The Checksum is used to see if there are any modifications in the files at server and client.
5. Server sends the list of files it has to the client long with time modified and checksum. The client obtains the list and checks with its own list of files, their dates modified and checksum. The files that are not present in the server list are pushed into a files to send list. The files whose checksum differ from the ones in server are also pushed to the files to send list. 
6. The files in the files to send list are sent to the server.
