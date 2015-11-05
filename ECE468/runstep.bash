make clean
make compiler
java -cp lib/antlr.jar:classes/ Micro step5/EE468/input/test_for.micro > myfor.txt
java -cp lib/antlr.jar:classes/ Micro step5/EE468/input/test_if.micro > myif.txt
java -cp lib/antlr.jar:classes/ Micro step5/EE468/input/test_adv.micro > myadv.txt
java -cp lib/antlr.jar:classes/ Micro step5/EE468/input/step4_testcase.micro > step4_1.txt
java -cp lib/antlr.jar:classes/ Micro step5/EE468/input/step4_testcase2.micro > step4_2.txt