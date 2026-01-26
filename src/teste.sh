#!/bin/bash
i=0
x=300
while [ $i -lt $x ]
do
nohup java ClientMain > /dev/null 2>&1 &
i=`expr $i + 1`
done

