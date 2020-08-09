@echo off
cd Image-in-Image-release\node
create_image.exe
IF %ERRORLEVEL%==0 (cd .. && handler.exe)
if %ERRORLEVEL%==0 (echo Your image is at: %CD%\result.png) ELSE (cd .. && echo Mission failed!)
cd ..
pause
