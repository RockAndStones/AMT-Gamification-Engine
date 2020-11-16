#!/bin/bash

mvn clean package -f ../../gamification-impl
cp ../../gamification-impl/target/gamification-impl-1.0.0.jar ./gamification-impl-1.0.0.jar

docker build -t rockandstone/gamification .
