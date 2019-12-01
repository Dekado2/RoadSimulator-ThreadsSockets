:: creates bin directory if doesnt already exist.
@if not exist "%cd%\bin\" mkdir bin

:: copies other source folders contents into bin.
@if exist resources xcopy /e /q /y resources bin\ > nul
@if exist test xcopy /e /q /y test bin\ > nul

:: compiles server layers java files into bin folder.
@javac -classpath lib/json-simple-1.1.1.jar -sourcepath src src/server/*.java -d bin

:: runs compiled server main class.
@java -cp bin;lib/json-simple-1.1.1.jar server.Server

:: pauses execution and displays "Press any key to continue...".
@pause