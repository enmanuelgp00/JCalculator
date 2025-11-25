javac -d build -classpath main\java main\java\calculator\Calculator.java
if %errorlevel% equ 0 ( jar -c -f jar\Calculator.jar --main-class calculator.Calculator -C build . )