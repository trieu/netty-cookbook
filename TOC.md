#Netty Cookbook- Recipes for building asynchronous event-driven network application

*Table of Contents*

## Chapter 1: Communicating in Asynchronous World with Netty
Introduction
Recipe 1.1 Building an asynchronous TCP server and client
Recipe 1.2 Sending hello message to server when connection is ready
Recipe 1.3 Receiving message asynchronously
Recipe 1.4 Get notification from Netty I/O operations
Recipe 1.5 Get notification from ChannelHandler states
Recipe 1.6 Data pipeline processing with ChannelHandler

## Chapter 2: Solving common network programming problems
Recipe 2.1 Getting the local SocketAddress and the remote SocketAddress
Recipe 2.2 Sending and receiving data in Stream-based TCP/IP Transport
Recipe 2.3 Sending data in POJO way with Custom Codec
Recipe 2.4 Listening multiple sockets in one Netty instance
Recipe 2.5 Counting Server Bandwidth in ChannelHandler
Recipe 2.6 Checking Heartbeat Server using UDP
Recipe 2.7 Using Stream Control Transmission Protocol (SCTP) codec
Recipe 2.8 Building simple FTP server
Recipe 2.9 Building server RPC (Remote Procedure Call) with Apache Avro
Recipe 2.10 Building HTTP file downloader

## Chapter 3: Netty for the Web
Recipe 3.1 Simple HTTP server with router
Recipe 3.2 Integrating Spring MVC 
Recipe 3.3 Serving static content
Recipe 3.4 Cross-Domain Ajax Requests with CorsHandler
Recipe 3.5 Enabling SPDY protocol for faster web
Recipe 3.6 Applying with Lambda Expressions in Java 8

## Chapter 4: Going Real Time Web
Introduction about real-time web
Recipe 4.1 Using WebSocket for streaming messages from Twitter
Recipe 4.2 Simple chat application with Netty STOMP Codec
Recipe 4.3 Displaying online users with Redis and netty-socketio
Recipe 4.4 RxNetty for filtering and pushing data in real-time

## Chapter 5: Asynchronous networking on mobile
Introduction
Recipe 5.1 Implementing lightweight network protocol for mobile app
Recipe 5.2 Using Netty on Android OS
Recipe 5.3 Asynchronous HTTP Client for Android app
Recipe 5.4 Implementing location-based app with netty-socketio and Lucene Spatial

## Chapter 6: Implementing Security, Encryption, and Authentication (10 pages)
Introduction
Recipe 6.1 Implementing HTTP Authentication Client and Server
Recipe 6.2 Implementing IP Filtering to protect your server
Recipe 6.3 Using SelfSignedCertificate to secure networking
Recipe 6.4 Managing distributed session with Memcached Binary protocol
Recipe 6.5 Setting up an HTTPS web server

## Chapter 7: High performance Web with HTTP/2 
Introduction about HTTP/2
Recipe 7.1 Using codec-http2 to improve the speed of Web
Recipe 7.2 Enabling multiple streams in single connection
Recipe 7.3 Setting priority in requests
Recipe 7.4 Compressing data response
Recipe 7.5 Enabling server push
Recipe 7.6 Building better static file server for high concurrency

## Chapter 8: Connecting Netty to Open Source Big Data Ecosystem
Introduction
Recipe 8.1 Building scalable log aggregation server with Netty and Apache Kafka
Recipe 8.2 Distributed HTTP event processing with Akka Actor
Recipe 8.3 Stream Data Processing with Netty and Java 8 Stream API
Recipe 8.4 Monitoring Social Data with Netty HTTP Client and Redis Queue
Recipe 8.5 Making real-time dashboard with Websocket and nvd3.js

## Chapter 9: Go live as real system
Introduction
Recipe 9.1 High availability with HAProxy 
Recipe 9.2 Up and running with Supervisord
Recipe 9.3 Using Metrics to capture application-level metrics
Recipe 9.4 Continuous performance test with ContiPerf 2
Recipe 9.5 Building code and deploy with Gradle and Groovy