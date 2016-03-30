#!/bin/bash
count=1;
while [ $count -lt 6 ]
do
  ./daily.sh $count
  ((count=count+1))
done

