@echo off
CLS

:: Local variables
SET SRCDIR=src
SET BINDIR=bin
SET BINOUT=game_javac.out
SET BINERR=game_javac.err
SET JARNAME=Game.jar
SET JAROUT=game_jar.out
SET JARERR=game_jar.err
SET DOCDIR=doc
SET DOCPACK=game
SET DOCOUT=game_javadoc.out
SET DOCERR=game_javadoc.err
SET MAINCLASSSRC=src/Game.java
SET MAINCLASSBIN=Game

ECHO "Compiling..."
javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC% -d %BINDIR% > %BINOUT% 2> %BINERR%

ECHO "Creating Jar..."
cd %BINDIR%
jar cvfe %JARNAME% %MAINCLASSBIN% . > %JAROUT% 2> %JARERR%

ECHO "Creating Javadoc..."
cd ..
javadoc -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% > %DOCOUT% 2> %DOCERR%

ECHO "Running Jar..."
java -jar %BINDIR%\%JARNAME%

ECHO "Script execution completed."
PAUSE
