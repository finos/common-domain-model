REM sort-schemas.bat
rem Copyright (c) 2002-2015
REM Sort all content of Schemas


rem set NAME=%1
rem set OUTPUT=%2
rem
set NAME=account-type-1-0
set OUTPUT=%1
set JAVAEXE=java
set SAXONDIR=.
set SAXON8=%JAVAEXE% -jar %SAXONDIR%\saxon8.jar


set SRCFILE=..\fpml_source_data\%NAME%.xml
set DESTFILE=..\cdm_json_refdata\%OUTPUT%.json
echo Converting %OUTPUT% to json
if exist %DESTFILE% del %DESTFILE%
%SAXON8% -o %DESTFILE% %SRCFILE% convertToJson.xsl schemeName=%OUTPUT%

