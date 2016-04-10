#!/bin/bash
# Copyright Jason, Pat, Nicholas, Mirna
<<COMMENT
DESCRIPTION
The weekly script is a script that simulates a week of the Bank system.

HOW TO RUN
To run the weekly script, ensure that you are in the the Integration directory and run it with this command:
  ./weekly.sh

INFORMATION
This will run the daily script for all of the 5 days.
COMMENT
count=1;
while [ $count -lt 6 ]
do
  ./daily.sh $count
  ((count=count+1))
done

