# Portfolio
Examples of my Coding projects and Technical Papers for Computer Science/IT Classes.

My name is John Kucera. I am an undergraduate Computer Science major and Cybersecurity minor attending the University of Maryland Global Campus. My expected graduation date is May 2022. My current GPA is 4.0.

## Technical Skills & Abilities

**Languages/Web Technology:**
Java, Python, Python Flask, C/C++, JavaScript, HTML, CSS, Lex/Yacc (Linux), some SQL, some Perl

**IDEs:**
Netbeans, Visual Studio Code, Cloud9, Repl.it

**Tools/Applications:**
Git, AWS, MS Excel, MS PowerPoint, Adobe Photoshop, MS Publisher

**OS:**
Windows, Linux, MacOS

## Computer Science Projects

### [Multi-threaded Traffic Simulation GUI](./Multi-threadedTrafficSimulationGUI) (Java)
#### CMSC 335 Object-oriented and Concurrent Programming (Fall 2020)

Written in Java, this program uses multithreading to create a traffic simulation GUI and animation. Up to 5 cars travel in a straight line at random speeds and their positions are represented as (x,y) coordinates and animated in the GUI. The cars will move between 3-5 traffic lights, and they will start and stop according to the lights: green/yellow (go) and red (stop).

### [Login Validation Form Webpage](./LoginValidationWebpage) (Python/HTML)
#### SDEV 300 Building Secure Python Applications (Fall 2019)

This program uses Python Flask with HTML to create a log in webpage that prompts the user for username and password before granting access. Client is allowed 15 attempts to log in, after which throttling will occur to block the IP address for a period of time before allowing for retries. Exception handling and maintenance of a database of user information/IP addresses are utilized to process user inputs.

### [Mini Compiler](./MiniCompiler) (Flex/Bison/C++)
#### CMSC 430 Advanced Programming Languages (Spring 2020)

Written in C++ using two Linux tools: Flex, which generates lexical analyzers, and Bison, which generates parsers. Accepts input programs written in a custom language and compiles them. I wrote the compiler in four separate parts:
  * Lexical Analyzer: Breaks the syntaxes of the input program into tokens. Checks for invalid characters.
  * Syntax Analyzer: Takes token streams from lexical analyzer and checks for syntax errors.
  * Interpreter: Executes instructions from input program. Accepts function parameters as command line arguments.
  * Semantic Analyzer: Takes token streams from lexical analyzer and checks for semantic errors.

## Technical Papers

### [Vulnerability Memo for Security Devices](./VulnerabilityMemo.pdf)
#### CMIT 320 Network Security (Fall 2020)

This Vulnerability Memo reviews two IoT devices: a smart door lock and a smart security camera. Using CVE (Common Vulnerabilities and Exposures list) and NVD (NIST’s National Vulnerability Database), I first learned about the devices and their functions.  CVE and NVD helped me identify the devices’ vulnerabilities (their usage of insecure network protocols). Finally, I suggested possible solutions to fix some of the weak points and strengthen the systems.

### [PC Build Manual](./PCBuildManual.pdf) 
#### CMIT 202 Fundamentals of Computer Troubleshooting (Fall 2020)

This in-depth manual to building a PC from scratch contains steps for installing essential hardware such as motherboard, CPU, RAM, power supply, hard drive, and peripherals. The manual reviews the necessary BIOS settings for system configuration, walks the reader through steps to install Windows, and instructs how to configure settings such as Windows Backup and Windows Firewall. I have built 5 PCs in the past and created this manual to help build more.

### [Evolution of System Performance](./SystemPerformance-ResearchPaper.pdf) (Research)
#### CMIS 310 Computer Systems and Architecture (Fall 2020)

This research paper is an analysis of how computer technology has evolved over time to improve system performance. My research was mainly on RISC (Reduced Instruction Set Computer). I studied the similarities and differences among RISC implementations over the past two decades. I emphasized the impact of pipelining, cache memory, and virtual memory on system performance improvement when implemented with RISC.

## Experience
### INTERNSHIP | [National Institute of Standards and Technology (NIST)](https://www.nist.gov/) | Summer 2021
#### [Summer Undergraduate Research Fellowship (SURF)](https://www.nist.gov/surf/) | [Information Technology Laboratory](https://www.nist.gov/itl/)

  * Experimented with software tools that quantified the uncertainty caused by floating-point approximation errors. The tools I studied were CADNA, Verrou, and Verificarlo, which each have their own methods of estimating and analyzing round-off errors in floating-point arithmetic.
  * Tested these tools by linking them to a biological neural simulator that used floating-point arithmetic. In my trials, I modified the C/C++ code and parameters of the software tools and neural simulator to explore different scenarios. 
  * Illustrated findings by creating Python scripts to parse through and graph the simulator results to be used in my final presentation.
  * Wrote an abstract and gave a 10-minute presentation where I discussed the purpose and findings of my research.
  * Collaborated with researchers from James Madison University who offered their own biological neural simulator. I linked it with the CADNA tool to observe how the floating-point output would be affected and graphed these results using Python scripts. 

## Workshop

### [DC-Baltimore Perl Workshop | April 2019](https://dcbpw.org/dcbpw2019/)

The Perl Workshop provides in-depth talks about the Perl language: starting with the basics of the language, its history, its applications over the years, and ending with the potential of the Perl language in today's technology.

## Volunteer Work

### [UMD ECP (English Conversation Partners) Group Leader/Facilitator | February 2019 - Present](http://ecpumd.weebly.com/)

ECP is a program where facilitators such as myself volunteer to hold weekly conversation sessions with UMD international students and their families. The weekly meetups include English learning exercises (vocabulary, common expressions, slang) and conversations to gain confidence in speaking English. Normally I would have a classroom of 3-5 students. For the last 2 semesters, I have been using Google Meet to meet virtually with my students. For Spring 2021, there are 5 students in my session.
