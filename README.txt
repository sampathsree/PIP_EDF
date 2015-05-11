This project is for Implementation PIP with Earliest Deadline First in java.

This contains three java files.
1. Main.java
2. Resource.java
3. Tasks.java

Comments are provided in the code.

Steps to execute:
1. Copy realm folder into local folder where java is running.
2. Update the link to input file in line number 29 of "Main.java", i.e., "a.txt"
3. Run Main.java

OR

Import the project into eclipse and follow steps 2 & 3 from above.



PS: If all the tasks are of non-zero phase, there would be idle time till the start of first job which is not printed. Please see Time column for instance where it starts.

Sample Input:
7
4
A 6 35 3 28 1 R2 1 2
B 4 30 4 30 1 R1 2 1
C 0 45 7 40 2 R1 1 3 R2 2 2
D 3 40 8 35 2 R1 1 4 R2 2 2

Sample Output:

Time	Job	Actions
1	C
2	C	locks R1,null,
3	D
4	B
5	B
6	C	locks R1,R2,	 Current Priority B
7	C	locks R1,R2,	 Unlock R1	 Unlock R2	 Current Priority B

