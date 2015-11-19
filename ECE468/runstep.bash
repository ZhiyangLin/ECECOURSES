make clean
make compiler
java -cp lib/antlr.jar:classes/ Micro step6/fma.micro > fma.txt
java -cp lib/antlr.jar:classes/ Micro step6/fibonacci2.micro > fibonacci2.txt
java -cp lib/antlr.jar:classes/ Micro step6/factorial2.micro > factorial2.txt