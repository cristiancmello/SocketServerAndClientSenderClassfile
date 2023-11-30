# Client/Server Impl with ServerSockets and Sockets

I recommend you to use Windows PowerShell.

## Running Server

```ps
cd Server
.\server.run.bat
```

Output (example):

```log
Server started. PID: 14312
Listening on port: 1234
```

## Running Client

- Client send `Person.class` file to Server

```ps
cd Client
.\client.run.bat
```

## Server tracing after Request from Client

```log
Connected accepted. Count = 1
Bytes available: 773
Magic bytes present.
Downloaded successfully
Declared methods: main,toString
Calling main()...
Hello, I'm Person!
```
